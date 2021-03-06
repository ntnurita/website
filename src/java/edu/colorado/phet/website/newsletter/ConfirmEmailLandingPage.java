/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.newsletter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.hibernate.Session;

import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.content.ErrorPage;
import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.hibernate.HibernateResult;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.Result;
import edu.colorado.phet.website.util.hibernate.Task;
import edu.colorado.phet.website.util.hibernate.TaskException;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

/**
 * Where the user lands when they click on the "confirm subscription" link from the confirmation email
 */
public class ConfirmEmailLandingPage extends PhetMenuPage {

    private static Logger logger = Logger.getLogger( ConfirmEmailLandingPage.class );

    public ConfirmEmailLandingPage( PageParameters parameters ) {
        super( parameters );
        setTitle( getLocalizer().getString( "newsletter.confirmedEmail.title", this ) );

        final String confirmationKey = parameters.getString( "key" );
        final String destination = parameters.getString( "destination" );
        final HibernateResult<Boolean> wasConfirmed = new HibernateResult<Boolean>();

        Result<PhetUser> userResult = HibernateUtils.resultTransaction( getHibernateSession(), new Task<PhetUser>() {
            public PhetUser run( Session session ) {
                PhetUser user = PhetUser.getUserFromConfirmationKey( getHibernateSession(), confirmationKey );
                if ( user != null ) {
                    wasConfirmed.setValue( user.isConfirmed() );
                    user.setConfirmed( true );
                    session.update( user );
                    return user;
                }
                else {
                    throw new TaskException( "user not found for confirmationKey: " + confirmationKey );
                }
            }
        } );
        if ( userResult.success ) {
            boolean emailSuccess;
            // if newsletter-only, OR if the user is already fully registered
            if ( userResult.value.isNewsletterOnlyAccount() || wasConfirmed.getValue() ) {
                emailSuccess = NewsletterUtils.sendNewsletterWelcomeEmail( getPageContext(), userResult.value );
            }
            else {
                emailSuccess = NewsletterUtils.sendUserWelcomeEmail( getPageContext(), userResult.value );

                if ( !wasConfirmed.getValue() ) {
                    // only sign in the user if they were not confirmed before. this should happen at most once per user 
                    PhetSession.get().signInWithoutPassword( getPhetCycle(), userResult.value.getId() );
                }
            }

            // if user doesn't have a good confirmation key for unsubscribing, generate one
            userResult.value.ensureHasConfirmationKey( getHibernateSession() );

            // then send them the current copy of the newsletter if they are subscribing to it AND if they were NOT confirmed before
            // we don't want to get many copies of it
            if ( userResult.value.isReceiveEmail() && !wasConfirmed.getValue() ) {
                NewsletterSender newsletterSender = new NewsletterSender();

                // double-check that we are sending out automated newsletters in this way
                if ( newsletterSender.allowAutomatedNewsletterEmails() ) {
                    newsletterSender.sendNewsletters( Arrays.asList( userResult.value ) );
                }
            }

            if ( !emailSuccess ) {
                // we are still OK if email fails, since this only lets them know about the success. Don't fail out.
                //ErrorPage.redirectToErrorPage();
            }
        }
        else {
            ErrorPage.redirectToErrorPage();
        }

        logger.info( userResult.value.getEmail() + " subscribed" );

        add( new ConfirmEmailLandingPanel( "main-panel", getPageContext(), userResult.value, destination, wasConfirmed.getValue() ) );

        hideSocialBookmarkButtons();
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        mapper.addMap( "^confirm-email$", ConfirmEmailLandingPage.class );//Wicket automatically strips the "?key=..."
    }

    public static RawLinkable getLinker( final String key ) {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return "confirm-email?key=" + key;
            }
        };
    }

    public static RawLinkable getLinker( final String key, final String destination ) {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                try {
                    return "confirm-email?key=" + key + "&destination=" + URLEncoder.encode( destination, "UTF-8" );
                }
                catch ( UnsupportedEncodingException e ) {
                    throw new RuntimeException( e ); // this really shouldn't happen
                }
            }
        };
    }
}
