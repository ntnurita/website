// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.website.util;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.notification.NotificationHandler;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.Result;
import edu.colorado.phet.website.util.hibernate.SimpleTask;
import edu.colorado.phet.website.util.hibernate.Task;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * Utilities related to translations
 */
public class TranslationUtils {

    private static final Logger logger = Logger.getLogger( TranslationUtils.class.getName() );

    /**
     * Create a new (empty) translation with the specific locale.
     *
     * @param session Hibernate session
     * @param locale  Locale
     * @return Result, with translation if successful
     */
    public static Result<Translation> createNewTranslation( Session session, final Locale locale ) {
        Result<Translation> result = HibernateUtils.resultTransaction( session, new Task<Translation>() {
            public Translation run( Session session ) {
                PhetUser user = (PhetUser) session.load( PhetUser.class, PhetSession.get().getUser().getId() );

                Translation translation = new Translation( locale, user, null );

                session.save( translation );
                session.save( user );
                return translation;
            }
        } );

        if ( result.success ) {
            final Translation translation = result.value;

            logger.info( "Created translation: " + translation );

            final PhetUser user = PhetSession.get().getUser();
            final PhetRequestCycle cycle = PhetRequestCycle.get();

            // launch notifications in a new thread
            if ( DistributionHandler.allowNotificationEmails( PhetRequestCycle.get() ) ) {
                ( new Thread() {
                    @Override
                    public void run() {
                        Session session = HibernateUtils.getInstance().openSession();
                        try {
                            NotificationHandler.sendTranslationCreatedNotification( translation.getId(), locale, user );
                            NotificationHandler.sendCreationNotificationToTranslators( session, translation, user, cycle );
                        }
                        finally {
                            session.close();
                        }
                    }
                } ).start();
            }
        }

        return result;
    }

    public static Result<Translation> createCopiedTranslation( Session session, final Translation translation ) {
        Result<Translation> result = HibernateUtils.resultCatchTransaction( session, new Task<Translation>() {
            public Translation run( Session session ) {
                PhetUser user = (PhetUser) session.load( PhetUser.class, PhetSession.get().getUser().getId() );
                Translation newTranslation = new Translation( translation.getLocale(), user, translation );

                session.save( newTranslation );
                session.update( user );

                Translation oldTranslation = (Translation) session.load( Translation.class, translation.getId() );

                for ( Object o : oldTranslation.getTranslatedStrings() ) {
                    TranslatedString oldString = (TranslatedString) o;

                    TranslatedString newString = new TranslatedString();
                    newString.setCreatedAt( oldString.getCreatedAt() );
                    newString.setUpdatedAt( oldString.getUpdatedAt() );
                    newString.setKey( oldString.getKey() );
                    newString.setValue( oldString.getValue() );
                    newTranslation.addString( newString );

                    session.save( newString );
                }

                return newTranslation;
            }
        } );

        if ( result.success ) {
            final Translation newTranslation = result.value;

            logger.info( "Created translation: " + newTranslation + " based on " + translation );

            final PhetUser user = PhetSession.get().getUser();
            final PhetRequestCycle cycle = PhetRequestCycle.get();

            // launch notifications in a new thread
            if ( DistributionHandler.allowNotificationEmails( PhetRequestCycle.get() ) ) {
                ( new Thread() {
                    @Override
                    public void run() {
                        Session session = HibernateUtils.getInstance().openSession();
                        try {
                            NotificationHandler.sendTranslationCreatedBasedOnNotification( newTranslation.getId(), newTranslation.getLocale(), user, translation.getId() );
                            NotificationHandler.sendCreationNotificationToTranslators( session, newTranslation, user, cycle );
                        }
                        finally {
                            session.close();
                        }
                    }
                } ).start();
            }
        }

        return result;
    }

    public static boolean setTranslationVisibility( Session session, final Translation translation, final boolean visible ) {
        if ( translation.isVisible() == visible ) {
            // don't worry if the visibility didn't change.
            return false;
        }

        boolean success = HibernateUtils.wrapCatchTransaction( session, new SimpleTask() {
            public void run( Session session ) {
                Translation tr = (Translation) session.load( Translation.class, translation.getId() );

                if ( translation.isVisible() != tr.isVisible() ) {
                    // our in-memory object is stale. bail out now
                    throw new RuntimeException( "mismatch with translation visibility between in-memory and live version" );
                }

                if ( visible ) {
                    List otherTranslations = session.createQuery( "select t from Translation as t where t.visible = true and t.locale = :locale" )
                            .setLocale( "locale", translation.getLocale() ).list();

                    if ( !otherTranslations.isEmpty() ) {
                        throw new RuntimeException( "There is already a visible translation of that locale" );
                    }
                }

                tr.setVisible( visible );

                session.update( tr );
            }
        } );

        if ( success ) {
            translation.setVisible( visible );
            if ( visible ) {
                PhetWicketApplication.get().addTranslation( translation );
            }
            else {
                PhetWicketApplication.get().removeTranslation( translation );
            }
        }

        return success;
    }

    public static Result<Translation> updatePublishedTranslation( Session session, Translation publishedTranslation, final Translation oldTranslation ) {
        // 1st transaction: create the copy
        Result<Translation> result = createCopiedTranslation( session, publishedTranslation );

        if ( result.success ) {
            final Translation newTranslation = result.value;

            // then attempt switching the visible translation
            boolean swapSuccess = HibernateUtils.wrapCatchTransaction( session, new VoidTask() {
                public Void run( Session session ) {
                    // TODO: do the swap. check the swap code AND the notifications that trigger the translation add/removal. order could go bad
                    Translation oldChild = (Translation) session.load( Translation.class, oldTranslation.getId() );
                    Translation newChild = (Translation) session.load( Translation.class, newTranslation.getId() );

                    if ( !oldChild.isVisible() || newChild.isVisible() ) {
                        // something looks wrong before the switch
                        throw new RuntimeException( "translation visibility problem in publishing" );
                    }

                    oldChild.setVisible( false );
                    newChild.setVisible( true );

                    session.update( oldChild );
                    session.update( newChild );
                    return null;
                }
            } );

            // if successful, swap globally
            if ( swapSuccess ) {
                oldTranslation.setVisible( false );
                newTranslation.setVisible( true );

                PhetWicketApplication.get().removeTranslation( oldTranslation );
                PhetWicketApplication.get().addTranslation( newTranslation );
            }

            return new Result<Translation>( swapSuccess, newTranslation, null );
        }
        else {
            return result;
        }
    }
}
