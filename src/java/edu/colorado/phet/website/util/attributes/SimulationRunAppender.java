// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.util.attributes;

import edu.colorado.phet.website.data.LocalizedSimulation;

/**
 * Sends a "Simulation Run" event to Google Analytics
 */
public class SimulationRunAppender extends GoogleAnalyticsEventAppender {
    public SimulationRunAppender( LocalizedSimulation simulation ) {
        super( "Simulation Run", simulation.getSimulation().getName(), simulation.getLocaleString() );
    }
}
