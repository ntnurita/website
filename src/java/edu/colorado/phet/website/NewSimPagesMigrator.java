// Copyright 2002-2015, University of Colorado
package edu.colorado.phet.website;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.common.phetcommon.util.FileUtils;
import edu.colorado.phet.common.phetcommon.util.Pair;
import edu.colorado.phet.website.data.Project;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * For migrating the old sim pages with new versions to the new HTML5 sim format
 */
public class NewSimPagesMigrator {

    private static File docRoot = PhetWicketApplication.get().getWebsiteProperties().getPhetDocumentRoot();
    private static Logger logger = Logger.getLogger( NewSimPagesMigrator.class );


    public static void migrateSims() {
        Session session = HibernateUtils.getInstance().openSession();

        List<Pair<String, String>> sims = new ArrayList();
        sims.add( new Pair<String, String>( "acid-base-solutions", "Acid Base Solutions" ) );
        sims.add( new Pair<String, String>( "balancing-act", "Balancing Act" ) );
        sims.add( new Pair<String, String>( "balancing-chemical-equations", "Balancing Chemical Equations" ) );
        sims.add( new Pair<String, String>( "balloons-and-static-electricity", "Balloons and Static Electricity" ) );
        sims.add( new Pair<String, String>( "beers-law-lab", "Beer's Law Lab" ) );
        sims.add( new Pair<String, String>( "build-an-atom", "Build an Atom" ) );
        sims.add( new Pair<String, String>( "concentration", "Concentration" ) );
        sims.add( new Pair<String, String>( "forces-and-motion-basics", "Forces and Motion: Basics" ) );
        sims.add( new Pair<String, String>( "fraction-matcher", "Fraction Matcher" ) );
        sims.add( new Pair<String, String>( "friction", "Friction" ) );
        sims.add( new Pair<String, String>( "graphing-lines", "Graphing Lines" ) );
        sims.add( new Pair<String, String>( "gravity-force-lab", "Gravity Force Lab" ) );
        sims.add( new Pair<String, String>( "john-travoltage", "John Travoltage" ) );
        sims.add( new Pair<String, String>( "molarity", "Molarity" ) );
        sims.add( new Pair<String, String>( "ohms-law", "Ohm's Law" ) );
        sims.add( new Pair<String, String>( "resistance-in-a-wire", "Resistance in a Wire" ) );
        sims.add( new Pair<String, String>( "under-pressure", "Under Pressure" ) );

        for ( Pair<String, String> pair : sims ) {
            logger.warn( "Attempting to add sim " + pair._2 );
            writeMetaXML( pair._1, pair._2 );
            Project.synchronizeProject( docRoot, session, "html/" + pair._1 );
            syncNewProject( session, pair._1 );
        }

        session.close();
    }

    /**
     * Write XML file with sim info and locales. Adapted from PhetProject.java in build-tools
     */
    public static void writeMetaXML( String name, String title ) {
        System.out.println( "Attempting to write meta XML for " + name );
        try {
            String str = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
                         "<project name=\"" + name + "\">\n";
            str += "<simulations>\n";
            str += "<simulation name=\"" + name + "\" locale=\"en\">\n";
            str += "<title><![CDATA[" + title + "]]></title>\n";
            str += "</simulation>\n";
            str += "</simulations>\n" +
                   "</project>";

            String dir = docRoot + "/sims/html/" + name;
            File xmlFile = new File( dir, name + ".xml" );
            xmlFile.setWritable( true );
            FileUtils.writeString( xmlFile, str );
        }
        catch( UnsupportedEncodingException e ) {
            e.printStackTrace();
        }
        catch( FileNotFoundException e ) {
            logger.warn( "File Not Found Exception", e );
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
    }

    public static void syncNewProject( Session session, String simName ) {
        List<Simulation> slist = session.createQuery( "select s from Simulation as s where s.name = :name" ).setString( "name", simName ).list();

        Simulation simulation = null;
        Simulation legacySim = null;

        for ( Simulation s : slist ) {
            if ( s.getProject().getType() == Project.TYPE_HTML ) {
                simulation = s;
            }
            else {
                legacySim = s;
            }
        }

        if ( simulation != null ) {
            simulation.getProject().setVisible( true );
            simulation.setSimulationVisible( true );
        }
        else {
            logger.error( "ERROR: No sim of type html found with name " + simName );
        }

        if ( legacySim == null ) {
            String legacyName = simName.contains( "travoltage" ) ? "travoltage" : simName.contains( "balloons" ) ? "balloons" : null;
            List<Simulation> legacyList = session.createQuery( "select s from Simulation as s where s.name = :name" ).setString( "name", legacyName ).list();
            if ( legacyList.size() == 1 ) {
                legacySim = legacyList.get( 0 );
            }
        }

        if ( legacySim != null && simulation != null ) {
            simulation.setAlignments( new HashSet( legacySim.getAlignments() ) );
            simulation.setCategories( new HashSet( legacySim.getCategories() ) );
            simulation.setRelatedSimulations( new LinkedList( legacySim.getRelatedSimulations() ) );
            simulation.setScienceLiteracyMapKeys( new HashSet( legacySim.getScienceLiteracyMapKeys() ) );
            simulation.setSecondaryAlignments( new HashSet( legacySim.getSecondaryAlignments() ) );
            simulation.setTopics( new LinkedList( legacySim.getTopics()) );
            simulation.setKeywords( new LinkedList( legacySim.getKeywords()) );
            simulation.setHighGradeLevel( legacySim.getHighGradeLevel() );
            simulation.setLowGradeLevel( legacySim.getLowGradeLevel() );
            simulation.setDesignTeam( legacySim.getDesignTeam() );
            simulation.setThanksTo( legacySim.getThanksTo() );
            simulation.setHighGradeLevel( legacySim.getHighGradeLevel() );
            simulation.setGuidanceRecommended( legacySim.isGuidanceRecommended() );
        }
        else {
            logger.error( "ERROR: No legacy sim found with name " + simName );
        }
    }
}
