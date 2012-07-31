// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.components;

import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.util.attributes.ClassAppender;
import edu.colorado.phet.website.util.attributes.SimulationRunAppender;

/**
 * All needed behavior for creating a link to run a simulation
 */
public class SimulationRunLink extends RawLink {

    public SimulationRunLink( String id, LocalizedSimulation simulation ) {
        super( id, simulation.getRunUrl() );

        add( new SimulationRunAppender( simulation ) );

        if ( simulation.getSimulation().getProject().isFlash() ) {
            // make Flash links open in a new window / tab
            add( new ClassAppender( "external" ) );
        }
    }
}
