// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.components;

import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.templates.PhetPage;
import edu.colorado.phet.website.util.attributes.SimulationDownloadAppender;

/**
 * All needed behavior for creating a link to download a simulation
 */
public class SimulationDownloadLink extends RawLink {

    public SimulationDownloadLink( String id, LocalizedSimulation simulation ) {
        super( id, simulation.getDownloadUrl() );

        add( new SimulationDownloadAppender( simulation ) );
    }
}
