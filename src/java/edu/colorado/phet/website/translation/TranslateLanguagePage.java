/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation;

import java.util.Collections;
import java.util.Comparator;
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
import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.authentication.SignInPage;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.TranslationUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.Result;
import edu.colorado.phet.website.util.hibernate.SimpleTask;
import edu.colorado.phet.website.util.hibernate.VoidTask;
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

        String localeString = StringUtils.getLocaleTitle( locale, WebsiteConstants.ENGLISH, getPhetLocalizer() );

        add( new Label( "header", localeString ) );

        final List<Translation> translations = new LinkedList<Translation>();
        final PhetUser currentUser = PhetSession.get().getUser();
        HibernateUtils.wrapTransaction( getHibernateSession(), new SimpleTask() {
            public void run( Session session ) {
                List trans = session.createQuery( "select t from Translation as t where t.locale = :locale order by t.id" )
                        .setLocale( "locale", locale ).list();
                for ( Object o : trans ) {
                    Translation translation = (Translation) o;
                    if ( !currentUser.isTeamMember() ) {
                        // skip translations where team-members are the only ones with access (unless you are a team member)
                        if ( translation.isVisible() ) {
                            boolean nonAdminUser = false;
                            for ( Object user : translation.getAuthorizedUsers() ) {
                                if ( !( (PhetUser) user ).isTeamMember() ) {
                                    nonAdminUser = true;
                                }
                            }
                            if ( !nonAdminUser ) {
                                continue;
                            }
                        }

                        // don't show inactive ("deleted") translations to non-team-members
                        if ( !translation.isActive() ) {
                            continue;
                        }
                    }
                    translations.add( translation );
                }
            }
        } );
        // sort the translations so that "preferred" simulations are at the top
        HibernateUtils.resultCatchTransaction( getHibernateSession(), new SimpleTask() {
            public void run( final Session session ) {
                Collections.sort( translations, new Comparator<Translation>() {
                    public int compare( Translation a, Translation b ) {
                        boolean ba = a.isPublished( session );
                        boolean bb = b.isPublished( session );
                        if ( ba == bb ) { return 0; }
                        return ( ba ? -1 : 1 );
                    }
                } );
            }
        } );
        TranslationListPanel translationList = new TranslationListPanel( "translation-list-panel", getPageContext(), translations );

        add( translationList );

        add( new CreateTranslationForm( "create-new-translation-form" ) );

        add( new CopyTranslationForm( "create-version-translation-form", translationList.getTranslations() ) );

        if ( translations.isEmpty() ) {
            add( new InvisibleComponent( "no-strings-translated" ) );
        }
        else {
            add( new Label( "no-strings-translated", "This will create a translation with no strings translated." ) );
        }

    }

    private class CreateTranslationForm extends Form {

        public CreateTranslationForm( String id ) {
            super( id );
        }

        @Override
        protected void onSubmit() {
            Result<Translation> result = TranslationUtils.createNewTranslation( getHibernateSession(), locale );

            // if successful, go to the edit page for the translation
            if ( result.success ) {
                setResponsePage( TranslationEditPage.class, TranslationEditPage.createPageParameters( result.value ) );
            }
        }
    }

    private class CopyTranslationForm extends Form {

        private Translation translation; // this is used, just using the PropertyModel which fools Intellij with its reflection

        public CopyTranslationForm( String id, final List<Translation> translations ) {
            super( id );

            add( new DropDownChoice<Translation>( "translations", new PropertyModel<Translation>( this, "translation" ), translations ) );

            if ( translations.isEmpty() ) {
                setVisible( false );
            }
        }

        @Override
        protected void onSubmit() {
            // ignore if they haven't selected a result yet (it will be null)
            if ( translation == null ) {
                return;
            }

            Result<Translation> result = TranslationUtils.createCopiedTranslation( getHibernateSession(), translation );

            if ( result.success ) {
                setResponsePage( TranslationEditPage.class, TranslationEditPage.createPageParameters( result.value ) );
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
