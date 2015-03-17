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
import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.content.DonatePanel;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Project;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.panels.SocialBookmarkPanel;
import edu.colorado.phet.website.panels.simulation.SimulationDisplayPanel;
import edu.colorado.phet.website.panels.sponsor.SimSponsorPanel;
import edu.colorado.phet.website.panels.sponsor.Sponsor;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

public class HTML5Panel extends PhetPanel {

    public HTML5Panel( String id, final PageContext context ) {
        super( id, context );

        add( DonatePanel.getLinker().getLink( "donate-link", context, getPhetCycle() ) );

        if ( getPhetCycle().isInstaller() ) {
            add( new WebMarkupContainer( "sim-sponsor-installer-js" ) );
        }
        else {
            add( new InvisibleComponent( "sim-sponsor-installer-js" ) );
        }

        if ( DistributionHandler.showSimSponsor( getPhetCycle() ) ) {
            // this gets cached, so it will stay the same for the sim (but will be different for different sims)
            add( new SimSponsorPanel( "html-sponsor", context, Sponsor.chooseRandomSimSponsor() ) );
        }
        else {
            add( new InvisibleComponent( "html-sponsor" ) );
        }

        add( new SocialBookmarkPanel( "social-bookmark-panel", context, getFullPath( context ), getPhetLocalizer().getString( "nav.html", this ) ) );

        if ( this.getLocale().equals( WebsiteConstants.ENGLISH ) ) {
            add( new InvisibleComponent( "html-translations-coming-soon" ) );
        }
        else {
            add( new WebMarkupContainer( "html-translations-coming-soon" ) );
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
                HibernateUtils.orderSimulations( simulations, englishLocale );
                return true;
            }
        } );

        add( new SimulationDisplayPanel( "simulation-display-panel", context, simulations ) );

//        add( new StaticImage( "html-video-thumbnail", Images.HTML5_VIDEO_THUMBNAIL_220, null ) );
    }

}