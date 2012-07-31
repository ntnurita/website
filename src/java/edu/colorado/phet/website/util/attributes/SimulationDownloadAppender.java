// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.util.attributes;

import edu.colorado.phet.website.data.LocalizedSimulation;

/**
 * Sends a "Simulation Download" event to Google Analytics
 */
public class SimulationDownloadAppender extends GoogleAnalyticsEventAppender {
    public SimulationDownloadAppender( LocalizedSimulation simulation ) {
        super( "Simulation Download", simulation.getSimulation().getName(), simulation.getLocaleString() );
    }
}
