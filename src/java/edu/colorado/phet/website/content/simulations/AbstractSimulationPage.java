// Copyright 2002-2015, University of Colorado
package edu.colorado.phet.website.content.simulations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.colorado.phet.website.cache.SimplePanelCacheEntry;
import edu.colorado.phet.website.content.NotFoundPage;
import edu.colorado.phet.website.data.Category;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.menu.NavLocation;
import edu.colorado.phet.website.metadata.SimulationPageMetadataPanel;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.panels.simulation.SimulationMainPanel;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

public abstract class AbstractSimulationPage extends PhetMenuPage {

    private static final Logger logger = Logger.getLogger( SimulationPage.class.getName() );

    /*
     * Maps from legacy sim names to html sim names and vice versa in the cases where they differ
     */
    public static final Map<String, String> LEGACY_TO_CURRENT_SIM_NAME = new HashMap<String, String>();
    public static final Map<String, String> CURRENT_SIM_NAME_TO_LEGACY = new HashMap<String, String>();

    static {
        LEGACY_TO_CURRENT_SIM_NAME.put( "balloons", "balloons-and-static-electricity" );
        LEGACY_TO_CURRENT_SIM_NAME.put( "travoltage", "john-travoltage" );

        CURRENT_SIM_NAME_TO_LEGACY.put( "balloons-and-static-electricity", "balloons" );
        CURRENT_SIM_NAME_TO_LEGACY.put( "john-travoltage", "travoltage" );
    }

    AbstractSimulationPage( PageParameters parameters, boolean isLegacy ) {
        super( parameters );

        String flavorName = parameters.getString( "simulation" );

        if ( !isLegacy && LEGACY_TO_CURRENT_SIM_NAME.containsKey( flavorName ) ) {
            flavorName = LEGACY_TO_CURRENT_SIM_NAME.get( flavorName );
        }
        if ( isLegacy && CURRENT_SIM_NAME_TO_LEGACY.containsKey( flavorName ) ) {
            flavorName = CURRENT_SIM_NAME_TO_LEGACY.get( flavorName );
        }

        LocalizedSimulation simulation = null;
        Set<NavLocation> locations = new HashSet<NavLocation>();

        Session session = getHibernateSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            simulation = HibernateUtils.getBestSimulation( session, getMyLocale(), flavorName, isLegacy );
            if ( simulation != null ) {
                for ( Object o : simulation.getSimulation().getCategories() ) {
                    Category category = (Category) o;
                    locations.add( category.getNavLocation( getNavMenu() ) );
                }
            }
            tx.commit();
        }
        catch( RuntimeException e ) {
            logger.warn( e );
            if ( tx != null && tx.isActive() ) {
                try {
                    tx.rollback();
                }
                catch( HibernateException e1 ) {
                    logger.error( "ERROR: Error rolling back transaction", e1 );
                }
                throw e;
            }
        }

        if ( simulation == null || !simulation.getSimulation().isVisible() ) {
            throw new RestartResponseAtInterceptPageException( NotFoundPage.class );
        }

        setMetaDescription( getLocalizer().getString( simulation.getSimulation().getDescriptionKey(), this ) );

        add( new SimulationPageMetadataPanel( "metadata-tags", getPageContext(), simulation ) );

        final LocalizedSimulation finalSim = simulation;
        PhetPanel simPanel = new SimplePanelCacheEntry( SimulationMainPanel.class, null, getPageContext().getLocale(), getMyPath(), getPhetCycle() ) {
            public PhetPanel constructPanel( String id, PageContext context ) {
                return new SimulationMainPanel( id, finalSim, context );
            }
        }.instantiate( "simulation-main-panel", getPageContext(), getPhetCycle() );

        add( simPanel );
        setTitle( (String) simPanel.getCacheParameter( "title" ) );

        initializeLocationWithSet( locations );

        this.getPhetCycle().setMinutesToCache( 15 );
    }
}
