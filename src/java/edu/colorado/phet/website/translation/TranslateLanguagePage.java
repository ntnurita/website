package edu.colorado.phet.website.translation;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.hibernate.Session;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.authentication.SignInPage;
import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.notification.NotificationHandler;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.*;
import edu.colorado.phet.website.util.links.AbstractLinker;

public class TranslateLanguagePage extends TranslationPage {

    private Locale locale;

    public static final String TRANSLATION_LOCALE = "translationLocale";
    private static final Logger logger = Logger.getLogger( TranslateLanguagePage.class.getName() );

    public TranslateLanguagePage( PageParameters parameters ) {
        super( parameters );

        if ( !PhetSession.get().isSignedIn() ) {
            throw new RestartResponseAtInterceptPageException( SignInPage.class );
        }

        locale = LocaleUtils.stringToLocale( parameters.getString( TRANSLATION_LOCALE ) );

        String localeString = StringUtils.getLocaleTitle( locale, PhetWicketApplication.getDefaultLocale(), getPhetLocalizer() );

        add( new Label( "header", localeString ) );

        final List<Translation> translations = new LinkedList<Translation>();
        final PhetUser currentUser = PhetSession.get().getUser();
        HibernateUtils.wrapTransaction( getHibernateSession(), new VoidTask() {
            public Void run( Session session ) {
                List trans = session.createQuery( "select t from Translation as t where t.locale = :locale order by t.id" )
                        .setLocale( "locale", locale ).list();
                for ( Object o : trans ) {
                    Translation translation = (Translation) o;
                    // skip translations where team-members are the only ones with access (unless you are a team member)
                    if ( !currentUser.isTeamMember() && translation.isVisible() ) {
                        boolean nonAdminUser = false;
                        for ( Object user : translation.getAuthorizedUsers() ) {
                            if ( !( (PhetUser) user ).isTeamMember() ) {
                                nonAdminUser = true;
                            }
                        }
                        if ( nonAdminUser ) {
                            continue;
                        }
                    }
                    translations.add( translation );
                }
                return null;
            }
        } );
        TranslationListPanel translationList = new TranslationListPanel( "translation-list-panel", getPageContext(), translations );

        add( translationList );

        add( new CreateTranslationForm( "create-new-translation-form" ) );

        add( new CopyTranslationForm( "create-version-translation-form", translationList.getTranslations() ) );

    }

    private class CreateTranslationForm extends Form {

        public CreateTranslationForm( String id ) {
            super( id );
        }

        @Override
        protected void onSubmit() {
            Result<Translation> result = HibernateUtils.resultTransaction( getHibernateSession(), new Task<Translation>() {
                public Translation run( Session session ) {
                    Translation translation = new Translation();
                    translation.setLocale( locale );
                    translation.setVisible( false );

                    PhetUser user = (PhetUser) session.load( PhetUser.class, PhetSession.get().getUser().getId() );
                    translation.addUser( user );

                    session.save( translation );
                    session.save( user );
                    return translation;
                }
            } );

            if ( result.success ) {
                Translation translation = result.value;

                logger.info( "Created translation: " + translation );

                PageParameters params = new PageParameters();
                params.put( TranslationEditPage.TRANSLATION_ID, translation.getId() );
                params.put( TranslationEditPage.TRANSLATION_LOCALE, LocaleUtils.localeToString( locale ) );

                NotificationHandler.sendTranslationCreatedNotification( translation.getId(), locale, PhetSession.get().getUser() );

                setResponsePage( TranslationEditPage.class, params );
            }
        }
    }

    private class CopyTranslationForm extends Form {

        private Translation translation;

        public CopyTranslationForm( String id, final List<Translation> translations ) {
            super( id );

            add( new DropDownChoice<Translation>( "translations", new PropertyModel<Translation>( this, "translation" ), translations ) );
        }

        @Override
        protected void onSubmit() {

            if ( translation == null ) {
                return;
            }

            final Translation ret[] = new Translation[1];

            boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                public boolean run( Session session ) {
                    Translation newTranslation = new Translation();
                    newTranslation.setLocale( translation.getLocale() );
                    newTranslation.setVisible( false );

                    PhetUser user = (PhetUser) session.load( PhetUser.class, PhetSession.get().getUser().getId() );
                    newTranslation.addUser( user );

                    session.save( newTranslation );
                    session.save( user );
                    ret[0] = newTranslation;

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

                    return true;
                }
            } );

            if ( success ) {

                logger.info( "Created translation: " + ret[0] + " based on " + translation );

                PageParameters params = new PageParameters();
                params.put( TranslationEditPage.TRANSLATION_ID, ret[0].getId() );
                params.put( TranslationEditPage.TRANSLATION_LOCALE, LocaleUtils.localeToString( ret[0].getLocale() ) );

                NotificationHandler.sendTranslationCreatedBasedOnNotification( ret[0].getId(), ret[0].getLocale(), PhetSession.get().getUser(), translation.getId() );

                setResponsePage( TranslationEditPage.class, params );
            }
        }
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        mapper.addMap( "for-translators/website", TranslateLanguagePage.class, new String[] { } );
    }

    public static AbstractLinker getLinker() {
        return new AbstractLinker() {
            @Override
            public String getSubUrl( PageContext context ) {
                return "for-translators/website";
            }
        };
    }

}
