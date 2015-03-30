// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.content.simulations;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.components.RawLink;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Project;
import edu.colorado.phet.website.panels.IndexLetterLinks;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.panels.SocialBookmarkPanel;
import edu.colorado.phet.website.panels.simulation.SimulationDisplayPanel;
import edu.colorado.phet.website.panels.simulation.SimulationIndexPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

public class HTML5Panel extends PhetPanel {

    public HTML5Panel( String id, final PageContext context, boolean showIndex ) {
        super( id, context );

        String pagePath = "simulations/category/new";

        add( new SocialBookmarkPanel( "social-bookmark-panel", context, getFullPath( context ), getPhetLocalizer().getString( "nav.html", this ) ) );

        if ( this.getLocale().equals( WebsiteConstants.ENGLISH ) ) {
            add( new InvisibleComponent( "html-translations-coming-soon" ) );
        }
        else {
            add( new WebMarkupContainer( "html-translations-coming-soon" ) {{
                add( new LocalizedText( "not-translated-text", "html5.translations-soon", new Object[] {
                    CategoryPage.getAllSimsLinker().getHref( context, getPhetCycle() )
                } ) );
            }} );
        }

        final List<LocalizedSimulation> simulations = new LinkedList<LocalizedSimulation>();

        HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
            public boolean run( Session session ) {
                Locale englishLocale = LocaleUtils.stringToLocale( "en" );
                Query query = session.createQuery( "select l from LocalizedSimulation as l, Simulation as s, Project as p where (l.simulation = s AND s.project.type = :typeID AND l.locale = :english)" );
                query.setInteger( "typeID", Project.TYPE_HTML );
                query.setLocale( "english", englishLocale );
                Set resultSet = new HashSet( query.list() ); // ensure unique results
                simulations.addAll( resultSet );
                HibernateUtils.orderSimulations( simulations, context.getLocale() );
                return true;
            }
        } );

        if ( showIndex ) {
            SimulationIndexPanel indexPanel = new SimulationIndexPanel( "simulation-display-panel", context, simulations );
            add( indexPanel );

            add( new InvisibleComponent( "to-index-view" ) );
            add( new RawLink( "to-thumbnail-view", context.getPrefix() + pagePath ) );
            add( new IndexLetterLinks( "letter-links", context, indexPanel.getLetters() ) );
        }
        else {
            add( new SimulationDisplayPanel( "simulation-display-panel", context, simulations ) );

            add( new RawLink( "to-index-view", context.getPrefix() + pagePath + "/index" ) );
            add( new InvisibleComponent( "to-thumbnail-view" ) );
            add( new InvisibleComponent( "letter-links" ) );
        }
    }

}