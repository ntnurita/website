// Copyright 2002-2013, University of Colorado
package edu.colorado.phet.website.services;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.hibernate.Session;

import edu.colorado.phet.common.phetcommon.model.property.Property;
import edu.colorado.phet.common.phetcommon.util.function.Function1;
import edu.colorado.phet.website.components.RawBodyLabel;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.metadata.MetadataUtils;
import edu.colorado.phet.website.metadata.PhetMetadataConverter;
import edu.colorado.phet.website.metadata.SimulationRecord;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;

import static edu.colorado.phet.common.phetcommon.util.FunctionalUtils.map;
import static edu.colorado.phet.common.phetcommon.util.FunctionalUtils.mkString;

/**
 * Allows direct requests for simulation metadata based on the simulation name and metadata format
 * <p/>
 * Example:
 * http://phet.colorado.edu/services/metadata/simulation-nodecl?simulation=density&format=nsdl_dc
 */
public class SimulationMetadataFormatServiceWithoutDeclaration extends WebPage {
    public SimulationMetadataFormatServiceWithoutDeclaration( PageParameters parameters ) {
        super( parameters );

        final String simulationName = parameters.getString( "simulation" );
        final String metadataFormat = parameters.getString( "format" );
        final Property<String> output = new Property<String>( "" );

        final String formatList = "phet-master," + mkString( map( MetadataUtils.javaFormatConverters(), new Function1<PhetMetadataConverter, Object>() {
            public String apply( PhetMetadataConverter converter ) {
                return converter.getToFormat();
            }
        } ), "," );

        if ( simulationName == null ) {
            // error: need simulation name
            output.set( "<error>simulation parameter is needed. please see http://phet.colorado.edu/services/project-sorted-simulations.csv for a list of valid simulation names</error>" );
        }
        else if ( metadataFormat == null ) {
            // error: need metadata format
            output.set( "<error>format parameter is needed. please use one of: " + formatList + "</error>" );
        }
        else {
            HibernateUtils.wrapTransaction( PhetRequestCycle.get().getHibernateSession(), new VoidTask() {
                public void run( Session session ) {

                    String badSimulationMessage = "<error>unknown simulation. please see http://phet.colorado.edu/services/project-sorted-simulations.csv for a list of valid simulation names</error>";
                    Simulation simulation;
                    try {
                        simulation = (Simulation) session.createQuery( "select s from Simulation as s where s.name = :name" ).setString( "name", simulationName ).uniqueResult();
                    }
                    catch( Exception e ) {
                        output.set( badSimulationMessage );
                        return;
                    }
                    if ( simulation == null ) {
                        output.set( badSimulationMessage );
                        return;
                    }

                    String masterFormat = MetadataUtils.simulationToMasterFormat( simulation );

                    for ( PhetMetadataConverter converter : MetadataUtils.javaFormatConverters() ) {
                        if ( converter.getToFormat().equals( metadataFormat ) ) {
                            SimulationRecord record = new SimulationRecord( masterFormat );
                            output.set( converter.convertRecord( record ).toString() );
                            return;
                        }
                    }

                    if ( metadataFormat.equals( "phet-master" ) ) {
                        output.set( masterFormat );
                        return;
                    }

                    output.set( "unknown format value. Should be one of: " + formatList + "</error>" );
                }
            } );
        }

        add( new RawBodyLabel( "text", output.get() ) );
    }
}
