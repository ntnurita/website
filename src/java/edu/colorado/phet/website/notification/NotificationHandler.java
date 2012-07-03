/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.notification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.mail.BodyPart;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.event.PostCollectionUpdateEvent;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostUpdateEvent;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.WebsiteProperties;
import edu.colorado.phet.website.constants.NotificationEmails;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.content.simulations.SimulationPage;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.NotificationEvent;
import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.data.contribution.Contribution;
import edu.colorado.phet.website.data.contribution.ContributionComment;
import edu.colorado.phet.website.data.contribution.ContributionNomination;
import edu.colorado.phet.website.data.util.AbstractChangeListener;
import edu.colorado.phet.website.data.util.HibernateEventListener;
import edu.colorado.phet.website.newsletter.NewsletterUtils;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.translation.TranslationMainPage;
import edu.colorado.phet.website.util.EmailUtils;
import edu.colorado.phet.website.util.HtmlUtils;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.Result;
import edu.colorado.phet.website.util.hibernate.Task;
import edu.colorado.phet.website.util.hibernate.VoidTask;

import static edu.colorado.phet.website.util.hibernate.HibernateUtils.resultCatchTransaction;
import static edu.colorado.phet.website.util.hibernate.HibernateUtils.resultTransaction;
import static edu.colorado.phet.website.util.hibernate.HibernateUtils.wrapCatchTransaction;
import static edu.colorado.phet.website.util.hibernate.HibernateUtils.wrapTransaction;

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

            @Override
            public void onCollectionUpdate( Object object, PostCollectionUpdateEvent event ) {
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
        boolean success = wrapTransaction( session, new HibernateTask() {
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
    private static final String SIMULATION_NOTIFICATION_FROM = WebsiteConstants.PHET_NO_REPLY_EMAIL_ADDRESS;

    private static final String TRANSLATION_NOTIFICATION_FOOTER = "<p>Replying to this email will send the response to the translation creator(s).</p>";

    private static String getTranslationSubject( String action, int id, Locale locale ) {
        return "PhET Website Translation #" + id + " (" + LocaleUtils.localeToString( locale ) + ") " + action;
    }

    private static boolean sendInternalTranslationNotificationCore( String action, String body, int id, Locale locale, Collection<PhetUser> users ) {
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
        catch( MessagingException e ) {
            logger.warn( "Email failure on attempting to notify of translation " + action, e );
            return false;
        }
    }

    // call from within a web session
    public static void kickOffSimulationNotificationEmails( final String subject, final String body, final Simulation simulation ) {
        final PhetRequestCycle cycle = PhetRequestCycle.get();

        // launch notifications in a new thread
        if ( DistributionHandler.allowNotificationEmails( PhetRequestCycle.get() ) ) {
            ( new Thread() {
                @Override
                public void run() {
                    Session session = HibernateUtils.getInstance().openSession();
                    try {
                        NotificationHandler.sendNewSimulationNotification( session, subject, body, simulation, cycle );
                    }
                    finally {
                        session.close();
                    }
                }
            } ).start();
        }
    }

    private static void sendNewSimulationNotification( Session session,
                                                       final String subject,
                                                       final String body,
                                                       final Simulation simulation,
                                                       PhetRequestCycle cycle ) {
        if ( !DistributionHandler.allowNotificationEmails( cycle ) ) {
            logger.info( "not sending translation email because we are not on the production server" );
            return; // fail out gracefully if we are not allowed to send notification emails
        }

        wrapTransaction( session, new VoidTask() {
            public void run( Session session ) {

            }
        } );

        Result<List<PhetUser>> result = resultTransaction( session, new Task<List<PhetUser>>() {
            public List<PhetUser> run( Session session ) {
                final List result = session.createQuery( "select u from PhetUser as u where u.receiveSimulationNotifications = true" ).list();
                ArrayList<PhetUser> returnedArray = new ArrayList<PhetUser>() {{
                    addAll( result );
                }};
                for ( PhetUser user : returnedArray ) {
                    // update confirmation key if necessary
                    user.ensureHasConfirmationKey( session );
                }
                return returnedArray;
            }
        } );

        // grab information needed from the simulation in a new transaction
        Result<LocalizedSimulation> simResult = resultTransaction( session, new Task<LocalizedSimulation>() {
            public LocalizedSimulation run( Session session ) {
                // load the simulation in this transaction
                Simulation sim = (Simulation) session.load( Simulation.class, simulation.getId() );

                // extract the localized sim
                return sim.getEnglishSimulation();
            }
        } );

        LocalizedSimulation lsim = simResult.value;
        String simTitle = lsim.getTitle();
        String simPageUrl = StringUtils.makeUrlAbsolute( SimulationPage.getLinker( simulation ).getDefaultRawUrl() );

        for ( PhetUser user : result.value ) {
            try {
                EmailUtils.GeneralEmailBuilder message = new EmailUtils.GeneralEmailBuilder( subject, SIMULATION_NOTIFICATION_FROM );

                message.addRecipient( user.getEmail() );
                message.setBody( getSimulationNotificationMessage( simTitle, simPageUrl, body, user.getConfirmationKey() ) );

                logger.info( "Sending simulation notification to " + user.getEmail() );
                EmailUtils.sendMessage( message );
            }
            catch( MessagingException e ) {
                logger.warn( "Email failure on attempting to notify of simulation creation to user " + user.getEmail(), e );
            }

            // throttle notification sending
            try {
                Thread.sleep( 500 );
            }
            catch( InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }

    public static String getSimulationNotificationMessage( String simTitle, String simPageUrl, String body, String confirmationKey ) {
        return simTitle + " is available at " + simPageUrl + "\n\n" +
               body + "\n\n" +
               "Thanks,\n\n" +
               "The PhET Team\n\n" +
               "http://phet.colorado.edu\n\n" +
               "You received this e-mail because you signed up at our website at http://phet.colorado.edu or email us at\n" +
               "phethelp@colorado.edu. To opt out of these newsletters, please click on this link: " +
               NewsletterUtils.getUnsubscribeLink( PageContext.getNewDefaultContext(), confirmationKey ) +
               " or copy and paste the text into your browser.\n\n" +
               WebsiteConstants.EMAIL_ADDRESS_FOOTER;
    }

    /**
     * Send an email to all translators who are editors of a translation with the same locale
     */
    public static void sendCreationNotificationToTranslators( Session session, final Translation translation, final PhetUser owner, PhetRequestCycle cycle ) {
        if ( !DistributionHandler.allowNotificationEmails( cycle ) ) {
            logger.info( "not sending translation email because we are not on the production server" );
            return; // fail out gracefully if we are not allowed to send notification emails
        }

        String subject = getTranslationSubject( "created", translation.getId(), translation.getLocale() );

        final Set<PhetUser> translators = new HashSet<PhetUser>();

        wrapTransaction( session, new VoidTask() {
            public void run( Session session ) {
                List list = session.createQuery( "select t from Translation as t where t.locale = :locale" )
                        .setLocale( "locale", translation.getLocale() ).list();
                for ( Object o : list ) {
                    Translation otherTranslation = (Translation) o;
                    translators.addAll( otherTranslation.getAuthorizedUsers() );
                }
            }
        } );

        for ( PhetUser user : translators ) {
            try {
                EmailUtils.GeneralEmailBuilder message = new EmailUtils.GeneralEmailBuilder( subject, TRANSLATION_NOTIFICATION_FROM );

                String language = StringUtils.getEnglishLocaleTitle( session, translation.getLocale() );

                message.addRecipient( user.getEmail() );
                if ( user.equals( owner ) ) {
                    message.setBody( "<p>Thank you for initiating a new translation for the language " + language + " with the id #" + translation.getId() + ".</p>" +
                                     "<p>If you experience any trouble with the translation process, please contact us at <a href=\"phethelp@colorado.edu\">phethelp@colorado.edu</a>.</p>" +
                                     NewsletterUtils.THANKYOU_MESSAGE );
                }
                else {
                    if ( user.isTeamMember() ) {
                        // unnecessary message for PhET team members
                        continue;
                    }
                    message.setBody(
                            "<p>A new translation was created for the language " + language + " with the id #" + translation.getId() + "</p>" +
                            "<p>You received this email because you are marked as a translator for a translation with the same language. You can contact the author of this new translation through the PhET website translation manager.</p>" +
                            "<p>Since we can only publish one translation per language, we encourage you to collaborate on one translation in your language.</p>" +
                            NewsletterUtils.THANKYOU_MESSAGE
                    );
                }

                logger.info( "Sending automatic translation notification to " + user.getEmail() );
                EmailUtils.sendMessage( message );
            }
            catch( MessagingException e ) {
                logger.warn( "Email failure on attempting to notify of translation creation to user " + user.getEmail(), e );
            }
        }
    }

    public static boolean sendTranslationCreatedNotification( int id, Locale locale, PhetUser user ) {
        String url = "https://phet.colorado.edu" + TranslationMainPage.getLinker().getDefaultRawUrl();
        List<PhetUser> users = new LinkedList<PhetUser>();
        users.add( user );
        return sendInternalTranslationNotificationCore( "created", "<p>This can be accessed at <a href=\"" + url + "\">" + url + "</a>.</p>", id, locale, users );
    }

    public static boolean sendTranslationCreatedBasedOnNotification( int id, Locale locale, PhetUser user, int oldId ) {
        String url = "https://phet.colorado.edu" + TranslationMainPage.getLinker().getDefaultRawUrl();
        List<PhetUser> users = new LinkedList<PhetUser>();
        users.add( user );
        return sendInternalTranslationNotificationCore( "created based on #" + oldId, "<p>This can be accessed at <a href=\"" + url + "\">" + url + "</a>.</p>", id, locale, users );
    }

    public static boolean sendTranslationSubmittedNotification( int id, Locale locale, Collection<PhetUser> users ) {
        String url = StringUtils.makeUrlAbsolute( TranslationMainPage.getLinker().getDefaultRawUrl() );
        return sendInternalTranslationNotificationCore( "submitted", "<p>This can be accessed at <a href=\"" + url + "\">" + url + "</a>.</p>", id, locale, users );
    }

    public static boolean sendTranslationDeletedNotification( int id, Locale locale, Collection<PhetUser> users, PhetUser deleter ) {
        return sendInternalTranslationNotificationCore( "deleted", "<p>The translation was deleted by " + deleter.getEmail() + "</p>", id, locale, users );
    }

    public static boolean sendTranslationRequestForCollaboration( int id, Locale locale, List<PhetUser> users, PhetUser currentUser, String translatorMessageString ) {
        String localeName = StringUtils.getLocaleTitle( locale, WebsiteConstants.ENGLISH, PhetLocalizer.get() );
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
        catch( MessagingException e ) {
            logger.warn( "Email failure on sending collaboration request", e );
            return false;
        }
    }
}
