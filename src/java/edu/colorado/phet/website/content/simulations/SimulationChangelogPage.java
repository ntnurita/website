package edu.colorado.phet.website.content.simulations;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.content.NotFoundPage;
import edu.colorado.phet.website.data.Category;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.menu.NavLocation;
import edu.colorado.phet.website.panels.simulation.SimulationChangelogPanel;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.HibernateUtils;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.links.AbstractLinker;

public class SimulationChangelogPage extends PhetMenuPage {

    private static final Logger logger = Logger.getLogger( SimulationChangelogPage.class.getName() );

    public SimulationChangelogPage( PageParameters parameters ) {
        super( parameters );

        String projectName = parameters.getString( "project" );
        String flavorName = parameters.getString( "flavor", projectName );

        LocalizedSimulation simulation = null;
        Set<NavLocation> locations = new HashSet<NavLocation>();

        // TODO: refactor to be same as SimulationPage
        Session session = getHibernateSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            simulation = HibernateUtils.getBestSimulation( session, getMyLocale(), projectName, flavorName );
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

        if ( simulation == null ) {
            throw new RestartResponseAtInterceptPageException( NotFoundPage.class );
        }

        boolean displayDev = PhetSession.get().isSignedIn() && PhetSession.get().getUser().isTeamMember();
        add( new SimulationChangelogPanel( "simulation-changelog-panel", simulation, getPageContext(), displayDev ) );

        // TODO: localize title
        addTitle( "Changelog for PhET Simulation " + simulation.getTitle() );

        initializeLocationWithSet( locations );

    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        // TODO: change URLS so this doesn't get gobbled by SimulationPage
        mapper.addMap( "^simulation/changelog/([^/]+)(/([^/]+))?", SimulationChangelogPage.class, new String[]{"project", null, "flavor"} );
    }

    public static AbstractLinker getLinker( final String projectName, final String simulationName ) {
        // WARNING: don't change without also changing the old URL redirection
        return new AbstractLinker() {
            @Override
            public String getSubUrl( PageContext context ) {
                String str = "simulation/changelog/" + projectName;
                if ( !projectName.equals( simulationName ) ) {
                    str += "/" + simulationName;
                }
                return str;
            }
        };
    }

    public static AbstractLinker getLinker( final Simulation simulation ) {
        return getLinker( simulation.getProject().getName(), simulation.getName() );
    }

    public static AbstractLinker getLinker( final LocalizedSimulation localizedSimulation ) {
        return getLinker( localizedSimulation.getSimulation() );
    }

}