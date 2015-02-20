/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.simulations;

import org.apache.wicket.PageParameters;

import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.links.AbstractLinker;

/**
 * Main page for legacy simulations
 */
public class LegacySimulationPage extends AbstractSimulationPage {

    public LegacySimulationPage( PageParameters parameters ) {
        super( parameters, true );
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        mapper.addMap( "^simulation/legacy/([^/]+)$", LegacySimulationPage.class, new String[]{"simulation"} );
    }

    public static AbstractLinker getLinker( final String projectName, final String simulationName ) {
        // WARNING: don't change without also changing the old URL redirection
        return new AbstractLinker() {
            @Override
            public String getSubUrl( PageContext context ) {
                return "simulation/legacy/" + simulationName;
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