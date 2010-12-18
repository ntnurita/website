package edu.colorado.phet.website.notification;

import java.util.*;

import javax.mail.BodyPart;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostUpdateEvent;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.WebsiteProperties;
import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.constants.NotificationEmails;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.data.NotificationEvent;
import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.data.contribution.Contribution;
import edu.colorado.phet.website.data.contribution.ContributionComment;
import edu.colorado.phet.website.data.contribution.ContributionNomination;
import edu.colorado.phet.website.data.util.AbstractChangeListener;
import edu.colorado.phet.website.data.util.HibernateEventListener;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.translation.TranslationMainPage;
import edu.colorado.phet.website.util.EmailUtils;
import edu.colorado.phet.website.util.HtmlUtils;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * Handles email notification of events that should be reviewed by the PhET team
 */
public class NotificationHandler {
    /**
     * We need to grab the mail handler configuration from the server parameters
     */
    private static WebsiteProperties websiteProperties;

    private static final Logger logger = Logger.getLogger( NotificationHandler.class.getName() );

    public static synchronized void initialize( WebsiteProperties properties ) {
        websiteProperties = properties;

        if ( !properties.hasMailParameters() ) {
            logger.warn( "Was unable to find mail server credentials. Will not start the notification handler" );
            return;
        }

        HibernateEventListener.addListener( ContributionNomination.class, new AbstractChangeListener() {
            @Override
            public void onInsert( Object object, PostInsertEvent event ) {
                NotificationEventType.onNominatedContribution( (ContributionNomination) object );
            }
        } );

        HibernateEventListener.addListener( Contribution.class, new AbstractChangeListener() {
            @Override
            public void onInsert( Object object, PostInsertEvent event ) {
                NotificationEventType.onNewContribution( (Contribution) object );
            }

            @Override
            public void onUpdate( Object object, PostUpdateEvent event ) {
                NotificationEventType.onUpdatedContribution( (Contribution) object );
            }
        } );

        HibernateEventListener.addListener( ContributionComment.class, new AbstractChangeListener() {
            @Override
            public void onInsert( Object object, PostInsertEvent event ) {
                NotificationEventType.onContributionComment( (ContributionComment) object );
            }
        } );
    }

    public static void sendNotifications() {
        final String mailHost = websiteProperties.getMailHost();
        final String mailUser = websiteProperties.getMailUser();
        final String mailPassword = websiteProperties.getMailPassword();
        final List<NotificationEvent> events = new LinkedList<NotificationEvent>();
        final List<PhetUser> usersToNotify = new LinkedList<PhetUser>();

        HibernateUtils.wrapSession( new HibernateTask() {
            public boolean run( org.hibernate.Session session ) {
                addEventsToList( session, events );
                List list = session.createQuery( "select u from PhetUser as u where u.teamMember = true and u.receiveWebsiteNotifications = true" ).list();
                for ( Object o : list ) {
                    usersToNotify.add( (PhetUser) o );
                }
                return true;
            }
        } );
        final List<String> emailAddresses = new ArrayList<String>();
        emailAddresses.add( "phetadmin@gmail.com" );
        for ( PhetUser user : usersToNotify ) {
            emailAddresses.add( user.getEmail() );
        }
        String body = eventsToString( events );
        final String subject = "[PhET Website] Notifications for Teaching Ideas";
        final ArrayList<BodyPart> additionalParts = new ArrayList<BodyPart>();//other than the message itself which is specified in body

        EmailUtils.sendMessage( mailHost, mailUser, mailPassword, emailAddresses, body, WebsiteConstants.PHET_NO_REPLY_EMAIL_ADDRESS, subject, additionalParts );
    }

    public static String getEventsString( org.hibernate.Session session ) {
        final List<NotificationEvent> events = new LinkedList<NotificationEvent>();
        boolean success = HibernateUtils.wrapTransaction( session, new HibernateTask() {
            public boolean run( org.hibernate.Session session ) {
                addEventsToList( session, events );
                return true;
            }
        } );
        if ( success ) {
            return eventsToString( events );
        }
        else {
            return "Error encountered in retrieving events";
        }
    }


    private static void addEventsToList( org.hibernate.Session session, List<NotificationEvent> events ) {
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.HOUR, -24 * 7 );
        List list = session.createQuery( "select ne from NotificationEvent as ne where ne.createdAt >= :date" )
                .setDate( "date", cal.getTime() ).list();
        for ( Object o : list ) {
            events.add( (NotificationEvent) o );
        }
    }

    private static String eventsToString( List<NotificationEvent> events ) {
        String body = "<p>The following events occurred in the last week:</p>";

        body += "<ul>";

        for ( NotificationEvent event : events ) {
            body += "<li>";
            body += event.toString();
            body += "</li>";
        }

        body += "</ul>";

        body += "<p>There were " + events.size() + " events.</p>";

        body += "<br/><p>Mailed automatically by the PhET website</p>";
        return body;
    }

    private static final String TRANSLATION_NOTIFICATION_FROM = WebsiteConstants.PHET_NO_REPLY_EMAIL_ADDRESS;

    private static final String TRANSLATION_NOTIFICATION_FOOTER = "<p>Replying to this email will send the response to the translation creator(s).</p>";

    private static String getTranslationSubject( String action, int id, Locale locale ) {
        return "Website Translation #" + id + " (" + LocaleUtils.localeToString( locale ) + ") " + action;
    }

    private static boolean sendTranslationNotificationCore( String action, String body, int id, Locale locale, Collection<PhetUser> users ) {
        if ( !PhetRequestCycle.get().isForProductionServer() ) {
            logger.info( "not sending translation email because we are not on the production server" );
            return true; // fail out gracefully if we are not the production server
        }
        try {
            String subject = getTranslationSubject( action, id, locale );
            EmailUtils.GeneralEmailBuilder message = new EmailUtils.GeneralEmailBuilder( subject, TRANSLATION_NOTIFICATION_FROM );

            for ( String email : NotificationEmails.TRANSLATION_NOTIFICATIONS ) {
                message.addRecipient( email );
            }
            message.setBody(
                    "<p>Translation " + action + " for the locale " + locale + " with the ID #" + id + "</p>" +
                    body + TRANSLATION_NOTIFICATION_FOOTER
            );
            for ( PhetUser user : users ) {
                message.addReplyTo( user.getEmail() );
            }

            return EmailUtils.sendMessage( message );
        }
        catch ( MessagingException e ) {
            logger.warn( "Email failure on attempting to notify of translation " + action, e );
            return false;
        }
    }

    public static boolean sendTranslationCreatedNotification( int id, Locale locale, PhetUser user ) {
        String url = EmailUtils.makeUrlAbsolute( TranslationMainPage.getLinker().getDefaultRawUrl() );
        List<PhetUser> users = new LinkedList<PhetUser>();
        users.add( user );
        return sendTranslationNotificationCore( "created", "<p>This can be accessed at <a href=\"" + url + "\">" + url + "</a>.</p>", id, locale, users );
    }

    public static boolean sendTranslationCreatedBasedOnNotification( int id, Locale locale, PhetUser user, int oldId ) {
        String url = EmailUtils.makeUrlAbsolute( TranslationMainPage.getLinker().getDefaultRawUrl() );
        List<PhetUser> users = new LinkedList<PhetUser>();
        users.add( user );
        return sendTranslationNotificationCore( "created based on #" + oldId, "<p>This can be accessed at <a href=\"" + url + "\">" + url + "</a>.</p>", id, locale, users );
    }

    public static boolean sendTranslationSubmittedNotification( int id, Locale locale, Collection<PhetUser> users ) {
        String url = EmailUtils.makeUrlAbsolute( TranslationMainPage.getLinker().getDefaultRawUrl() );
        return sendTranslationNotificationCore( "submitted", "<p>This can be accessed at <a href=\"" + url + "\">" + url + "</a>.</p>", id, locale, users );
    }

    public static boolean sendTranslationDeletedNotification( int id, Locale locale, Collection<PhetUser> users ) {
        return sendTranslationNotificationCore( "deleted", "<p>The translation was deleted by " + PhetSession.get().getUser().getEmail() + "</p>", id, locale, users );
    }

    public static boolean sendTranslationRequestForCollaboration( int id, Locale locale, List<PhetUser> users, PhetUser currentUser, String translatorMessageString ) {
        String localeName = StringUtils.getLocaleTitle( locale, PhetWicketApplication.getDefaultLocale(), PhetLocalizer.get() );
        logger.info( "Sending collaboration request from user " + currentUser.getEmail() + " at IP " + PhetRequestCycle.get().getHttpServletRequest().getRemoteAddr() );
        try {
            String subject = "PhET translator requests access to your " + localeName + " website translation #" + id;
            EmailUtils.GeneralEmailBuilder message = new EmailUtils.GeneralEmailBuilder( subject, TRANSLATION_NOTIFICATION_FROM );

            for ( PhetUser user : users ) {
                message.addRecipient( user.getEmail() );
            }
            message.addReplyTo( currentUser.getEmail() );

            String body = "<p>The translator &quot;" + HtmlUtils.encode( currentUser.getName() ) + "&quot; &lt;" + HtmlUtils.encode( currentUser.getEmail() ) + "&gt; has requested to collaborate on your " + localeName + " website translation #" + id + ":</p>";
            if ( translatorMessageString != null && translatorMessageString.length() > 0 ) {
                body += "<p style='border: 1px solid #888; padding: 2em;'>" + HtmlUtils.encode( translatorMessageString ) + "</p>";
            }
            body += "<p>Replying to this email should send your message to the person who requested to collaborate.</p>"
                    + "<p>If you choose to, you can give this translator permission to edit your translation by logging in, going to the website translation area, clicking 'edit' on your translation, and scrolling down to the 'User Access' area.</p>"
                    + "<p style='color: #666666;'>Your email address has not been given out to this translator.</p>";
            body = body.replaceAll( "\n", "<br/>" );
            message.setBody( body );

            return EmailUtils.sendMessage( message );
        }
        catch ( MessagingException e ) {
            logger.warn( "Email failure on sending collaboration request", e );
            return false;
        }
    }
}
