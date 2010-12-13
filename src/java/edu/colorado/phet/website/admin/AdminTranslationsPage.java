package edu.colorado.phet.website.admin;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.hibernate.Session;

import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.translation.TranslationListPanel;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * This is used (linked from AdminMainPage). Don't let the IDE fool you. (or at least check there first)
 */
public class AdminTranslationsPage extends AdminPage {
    public AdminTranslationsPage( PageParameters parameters ) {
        super( parameters );

        final List<Translation> translations = new LinkedList<Translation>();
        HibernateUtils.wrapTransaction( getHibernateSession(), new VoidTask() {
            public Void run( Session session ) {
                List trans = session.createQuery( "select t from Translation as t order by t.id" ).list();
                for ( Object o : trans ) {
                    translations.add( (Translation) o );
                }
                return null;
            }
        } );

        add( new TranslationListPanel( "translations", getPageContext(), translations ) );
    }

}