package edu.colorado.phet.website.metadata;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.colorado.phet.website.PhetWicketApplication;

/**
 * A term in the LRE-0001 vocabulary. see LRE-LRE-0001.xml or LRE-0001.properties for examples.
 * Includes an ID and English translation
 */
public class LRETerm implements Serializable {
    public final String id; // usually integer-like, but keeping open for flexibility
    public final String englishCaption;

    private static final Properties properties = new Properties();

    private static final Logger logger = Logger.getLogger( LRETerm.class.getName() );

    private LRETerm( String id, String englishCaption ) {
        this.id = id;
        this.englishCaption = englishCaption;
    }

    /*---------------------------------------------------------------------------*
    * static functions
    *----------------------------------------------------------------------------*/

    public static void initialize() {
        try {
            properties.load( PhetWicketApplication.get().getServletContext().getResourceAsStream( "/internal/LRE-0001.properties" ) );
        }
        catch ( IOException e ) {
            // do not fail out
            logger.error( "Could not initialize LRETerms: ", e );
            throw new RuntimeException( e );
        }
    }

    public static LRETerm getTermFromId( String id ) {
        String english = properties.getProperty( id );
        return new LRETerm( id, english );
    }

    public static List<LRETerm> getSortedTerms() {
        List<LRETerm> result = new ArrayList<LRETerm>();
        for ( Object o : properties.keySet() ) {
            result.add( getTermFromId( (String) o ) );
        }
        Collections.sort( result, new Comparator<LRETerm>() {
            public int compare( LRETerm a, LRETerm b ) {
                return a.englishCaption.compareToIgnoreCase( b.englishCaption );
            }
        } );
        return result;
    }

    /*---------------------------------------------------------------------------*
    * equality based on IDs
    *----------------------------------------------------------------------------*/

    @Override public int hashCode() {
        return id.hashCode();
    }

    @Override public boolean equals( Object obj ) {
        return obj instanceof LRETerm && id.equals( ( (LRETerm) obj ).id );
    }
}
