/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.hibernate.Session;

import edu.colorado.phet.website.content.IndexPage;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.translation.TranslationListPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.TranslationUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.SimpleTask;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * This is used (linked from AdminMainPage). Don't let the IDE fool you. (or at least check there first)
 */
public class AdminTranslationsPage extends AdminPage {
    public AdminTranslationsPage( PageParameters parameters ) {
        super( parameters );

        // all translations
        final List<Translation> translations = new ArrayList<Translation>();

        // "published" translations (that have visible children)
        final List<Translation> publishedTranslations = new ArrayList<Translation>();

        // map from published parent to visible child
        final Map<Translation, Translation> visibleChildMap = new HashMap<Translation, Translation>();

        HibernateUtils.wrapTransaction( getHibernateSession(), new SimpleTask() {
            public void run( Session session ) {
                List trans = session.createQuery( "select t from Translation as t order by t.id" ).list();
                for ( Object o : trans ) {
                    Translation translation = (Translation) o;
                    translations.add( translation );

                    if ( translation.isVisible() && translation.getParent() != null ) {
                        // this is a visible child to a published translation
                        publishedTranslations.add( translation.getParent() );
                        visibleChildMap.put( translation.getParent(), translation );
                    }
                }
            }
        } );

        // sort the published translations by ID
        Collections.sort( publishedTranslations, new Comparator<Translation>() {
            public int compare( Translation a, Translation b ) {
                return new Integer( a.getId() ).compareTo( b.getId() );
            }
        } );

        // panel that allows quick re-publishing of translations
        add( new ListView<Translation>( "translation", publishedTranslations ) {
            @Override protected void populateItem( ListItem<Translation> item ) {
                final Translation parentTranslation = item.getModelObject();
                final Translation childTranslation = visibleChildMap.get( parentTranslation );

                item.add( new Label( "parent-id", "" + parentTranslation.getId() ) );
                item.add( new Label( "child-id", "" + childTranslation.getId() ) );
                item.add( new Label( "locale", parentTranslation.getPrettyEnglishLocaleString() ) );
                item.add( IndexPage.getLinker().getLink( "view-link", PageContext.getNewLocaleContext( parentTranslation.getLocale() ), getPhetCycle() ) );
                item.add( new Link( "update-link" ) {
                    @Override public void onClick() {
                        TranslationUtils.updatePublishedTranslation( getHibernateSession(), parentTranslation, childTranslation );

                        // reload a new copy of this page
                        setResponsePage( AdminTranslationsPage.class );
                    }
                } );
            }
        } );

        add( new TranslationListPanel( "translations", getPageContext(), translations ) );
    }

}