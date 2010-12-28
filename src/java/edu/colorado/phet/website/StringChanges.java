/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * Contains strings that have been addedor modified since the last production deployment. If strings by those key names
 * don't exist they will be created.
 */
public class StringChanges {

    private static Logger logger = Logger.getLogger( StringChanges.class );

    public static void checkNewStrings() {
        Session session = HibernateUtils.getInstance().openSession();
        addString( session, "home.subscribeToNewsletter", "Subscribe to our newsletter" );
        addString( session, "newsletter.subscribe.email", "Email Address:" );
        addString( session, "newsletter.subscribe.submit", "Subscribe" );
        addString( session, "newsletter.validation.email.Required", "An email address is required" );
        addString( session, "newsletter.validation.email", "A valid email address is required" );
        addString( session, "newsletter.validation.attempts", "Too many newsletter attempts have been made. Please try again later" );
        addString( session, "newsletter.nowSubscribed", "{0} is now subscribed to the PhET newsletter." );
        addString( session, "newsletter.nowUnsubscribed", "{0} is now unsubscribed from the PhET newsletter." );
        addString( session, "newsletter.nowRegistered", "Thank you for registering! In a few seconds you will be redirected to your original page." );
        addString( session, "newsletter.toFinishRegistering", "To finish creating your account, we've sent a confirmation email to {0}. In the Email, please click on the link or copy and paste it into a browser in order to confirm your email." );
        addString( session, "newsletter.toFinishSubscribing", "To finish subscribing, we've sent a confirmation email to {0}. In the Email, please click on the link or copy and paste it into a browser in order to confirm your email." );
        addString( session, "newsletter.troubleshooting", "If you don't receive the email momentarily, please make sure your email filter allows emails sent from {0}. For additional help, please email {1}." );
        addString( session, "newsletter.awaitingConfirmation", "Awaiting Email Confirmation" );
        addString( session, "newsletter.confirmEmailSent.title", "Awaiting Email Confirmation" );
        //checkString( session, "newsletter.pastEditions", "<a {0}>Here are some past editions</a> of the PhET Newsletter to give you an idea of what you will receive." );
        addString( session, "newsletter.pleaseSignUp", "Please sign up if you would like to receive the PhET Newsletter. Once you have submitted your email address, you should receive a confirmation of your subscription via email." );
        addString( session, "newsletter.subscribeTo", "Subscribe to the Newsletter" );
        addString( session, "newsletter.subscribe.title", "Subscribe to PhET" );
        addString( session, "newsletter.confirmedEmail.title", "Confirmed PhET Email" );
        addString( session, "newsletter.confirmedEmail", "Email Confirmed" );
        addString( session, "newsletter.unsubscribed.title", "Unsubscribed from PhET Newsletter" );
        addString( session, "newsletter.unsubscribed", "Unsubscribed from PhET Newsletter" );

        addString( session, "newsletter.pleaseCheckSubscribing", "Please check your email and click on the link to complete your newsletter subscription." );
        addString( session, "newsletter.pleaseCheckRegistering", "Please check your email and click on the link to complete your account registration." );
        addString( session, "newsletter.redirection", "In a few seconds you will be redirected to your original page." );

        addString( session, "admin.keyword.create.duplicate", "Keyword ${0} already has the same value" );
        addString( session, "admin.keyword.create.exists", "The keyword already exists with the value ${0}" );

        addString( session, "troubleshooting.main.q15.title", "Can I run PhET simulations from a USB drive?" );
        addString( session, "troubleshooting.main.q15.answer", "<p>Yes, but there are some situations where problems may be encountered. If you only want to run a small number of simulations, the best approach is to download the individual JAR files (i.e. the files that are downloaded when you press the “Download” button on the simulation pages) and save them on your USB drive. This should work under most circumstances. If you want to install all simulations using the Offline Website Installer, there can be some complications. The reason for this is that some of the simulations – specifically the ones that run under Java – have hardcoded path information in their launch files. This is due to a limitation of the Java Network Launching Protocol. This limitation means that if you install the Offline Website Installer to your hard drive and then copy all of the installed files to a USB drive, there is a good chance that many of the simulations will not run. One possible alternative approach is to install the Offline Website Installer directly on to your USB drive. This should work in situations where the USB drive is assigned the same drive designation (e.g. “E:”) when attached to a new machine as was assigned when the Offline Website Installer was installed. If the USB drive is assigned a different drive designation, again, many of the simulations will not run.</p>" );

        addString( session, "troubleshooting.main.q16.title", "Where does the name \"PhET\" come from?" );
        addString( session, "troubleshooting.main.q16.answer", "<p>Our project originally focused on physics sims, and was hence named the *Ph*ysics *E*ducation *T*echnology project, or *PhET*. When we branched into chemistry, biology, mathematics and other areas, we decided to keep the name *PhET*, but to no longer describe it as an acronym.</p>" );

        overwriteString( session, "newsletter.toFinishSubscribing", "To finish subscribing, we've sent a confirmation email to {0}. In the Email, please click on the link or copy and paste it into a browser in order to confirm your email.", "Thank you for subscribing. An email has been sent to {0}." );
        overwriteString( session, "newsletter.toFinishRegistering", "To finish creating your account, we've sent a confirmation email to {0}. In the Email, please click on the link or copy and paste it into a browser in order to confirm your email.", "Thank you for registering. An email has been sent to {0}." );
        overwriteString( session, "newsletter.troubleshooting", "If you don't receive the email momentarily, please make sure your email filter allows emails sent from {0}. For additional help, please email {1}.", "For help, please email {0}." );
        overwriteString( session, "newsletter.nowSubscribed", "{0} is now subscribed to the PhET newsletter.", "Thank you for subscribing to the PhET newsletter! Your subscription is now complete." );
        overwriteString( session, "newsletter.nowRegistered", "Thank you for registering! In a few seconds you will be redirected to your original page.", "Thank you for registering! You account is now active." );
        overwriteString( session, "newsletter.pleaseSignUp", "Please sign up if you would like to receive the PhET Newsletter. Once you have submitted your email address, you should receive a confirmation of your subscription via email.", "Please sign up if you would like to receive the PhET Quarterly Newsletter. " );

        overwriteString( session, "newsletter.pleaseCheckSubscribing", "Please check your email and click on the link to complete your newsletter subscription.", "Please <strong>check your email and click on the link</strong> to complete your newsletter subscription." );
        overwriteString( session, "newsletter.pleaseCheckRegistering", "Please check your email and click on the link to complete your account registration.", "Please <strong>check your email and click on the link</strong> to complete your account registration." );

        overwriteString( session, "newsletter.toFinishSubscribing", "Thank you for subscribing. An email has been sent to {0}.", "Thank you for subscribing. An email has been sent to <strong>{0}</strong>." );
        overwriteString( session, "newsletter.toFinishRegistering", "Thank you for registering. An email has been sent to {0}.", "Thank you for registering. An email has been sent to <strong>{0}</strong>." );

        overwriteString( session, "simulations.translated.untranslated", "Untranslated Simulations", "Simulations not yet translated into {0}" );

        overwriteString( session, "newsletter.unsubscribed.title", "Unsubscribed to PhET Newsletters", "Unsubscribed from PhET Newsletter" );

        deleteString( session, "newsletter.pastEditions" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    *----------------------------------------------------------------------------*/

    public static void addString( Session session, String key, String value ) {
        String result = StringUtils.getStringDirect( session, key, PhetWicketApplication.getDefaultLocale() );
        if ( result == null ) {
            logger.warn( "Auto-setting English string with key=" + key + " value=" + value );
            StringUtils.setEnglishString( session, key, value );
        }
    }

    private static void overwriteString( Session session, String key, String oldValue, String newValue ) {
        String result = StringUtils.getStringDirect( session, key, PhetWicketApplication.getDefaultLocale() );
        if ( result == null ) {
            logger.warn( "Auto-setting English string with key=" + key + " value=" + newValue );
            StringUtils.setEnglishString( session, key, newValue );
        }
        else {
            if ( result.equals( oldValue ) ) {
                logger.warn( "Auto-setting English string with key=" + key + " value=" + newValue + " over old value " + oldValue );
                StringUtils.setEnglishString( session, key, newValue );
            }
        }
    }

    private static void deleteString( Session session, final String key ) {
        String result = StringUtils.getStringDirect( session, key, PhetWicketApplication.getDefaultLocale() );
        if ( result != null ) {
            logger.warn( "Deleting English string with key=" + key + " value=" + result );
            HibernateUtils.wrapTransaction( session, new HibernateTask() {
                public boolean run( Session session ) {
                    TranslatedString translatedString = (TranslatedString) session.createQuery( "select ts from TranslatedString as ts, Translation as t where (ts.translation = t and t.visible = true and t.locale = :locale and ts.key = :key)" )
                            .setLocale( "locale", PhetWicketApplication.getDefaultLocale() ).setString( "key", key ).uniqueResult();
                    if ( translatedString != null ) {
                        translatedString.getTranslation().removeString( translatedString );
                        session.delete( translatedString );
                    }
                    return true;
                }
            } );
        }
    }
}
