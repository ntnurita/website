package edu.colorado.phet.website.translation;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.Form;
import org.hibernate.Session;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.authentication.SignInPage;
import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;
import edu.colorado.phet.website.util.links.AbstractLinker;

public class TranslationMainPage extends TranslationPage {

    private static final Logger logger = Logger.getLogger( TranslationMainPage.class.getName() );

    public TranslationMainPage( PageParameters parameters ) {
        super( parameters );

        if ( !PhetSession.get().isSignedIn() ) {
            throw new RestartResponseAtInterceptPageException( SignInPage.class );
        }

        final List<Translation> translations = new LinkedList<Translation>();
        final PhetUser user = PhetSession.get().getUser();
        HibernateUtils.wrapTransaction( getHibernateSession(), new VoidTask() {
            public Void run( Session session ) {
                List trans = session.createQuery( "select t from Translation as t order by t.id" ).list();
                for ( Object o : trans ) {
                    Translation translation = (Translation) o;
                    if ( user.isTeamMember() || translation.isUserAuthorized( user ) ) {
                        translations.add( translation );
                    }
                }
                return null;
            }
        } );
        TranslationListPanel translationList = new TranslationListPanel( "translation-list-panel", getPageContext(), translations );

        add( translationList );

        add( new PickLanguageForm( "pick-language-form" ) );
    }

    private class PickLanguageForm extends Form {
        private LocaleDropDownChoice localeChoice;

        public PickLanguageForm( String id ) {
            super( id );

            localeChoice = new LocaleDropDownChoice( "locales", getPageContext() );
            add( localeChoice );
        }

        @Override
        protected void onSubmit() {
            Locale locale = localeChoice.getLocale();
            if ( locale == null ) {
                // TODO: validate in future?
                return;
            }

            PageParameters params = new PageParameters();
            params.put( TranslateLanguagePage.TRANSLATION_LOCALE, LocaleUtils.localeToString( locale ) );

            setResponsePage( TranslateLanguagePage.class, params );
        }
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        mapper.addMap( "for-translators/website", TranslationMainPage.class, new String[] { } );
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
