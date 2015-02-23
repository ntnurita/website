// Copyright 2002-2015, University of Colorado
package edu.colorado.phet.website;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
            Project.synchronizeProject( docRoot, session, "html/" + pair._1, logger, true );
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
            FileUtils.writeString( new File( dir, name + ".xml" ), str );
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
}
