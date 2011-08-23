/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * Contains strings that have been addedor modified since the last production deployment. If strings by those key names
 * don't exist they will be created.
 */
public class StringChanges {

    private static Logger logger = Logger.getLogger( StringChanges.class );

    public static void checkNewStrings() {
        Session session = HibernateUtils.getInstance().openSession();

        StringUtils.addString( session, "metadata.rights", "© 2011 University of Colorado. This simulation is available for free under either a Creative Commons Attribution license (http://creativecommons.org/licenses/by/3.0/us/) or the GNU General Public License (http://creativecommons.org/licenses/GPL/2.0/). Donations welcome and encouraged (http://phet.colorado.edu/en/donate)." );
        StringUtils.addString( session, "metadata.rightsGplOnly", "© 2011 University of Colorado. This simulation is available for free under the GNU General Public License (http://creativecommons.org/licenses/GPL/2.0/). Donations welcome and encouraged (http://phet.colorado.edu/en/donate)." );
        StringUtils.addString( session, "sponsors.jprofiler.desc", "For providing licenses of JProfiler" );

        StringUtils.addString( session, "simulationMainPanel.frequentlyAskedQuestions", "Frequently Asked Questions" );
        StringUtils.addString( session, "simulation.faq.title", "Frequently Asked Questions for {0}" );
        StringUtils.overwriteString( session, "simulation.faq.title", "Frequently Asked Questions for {0}", "FAQ for {0}" );
        StringUtils.addString( session, "simulationMainPanel.simulationHasFAQ", "This simulation has a list of <a {0}>Frequently Asked Questions</a> (<a {1}>pdf</a>)." );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    *----------------------------------------------------------------------------*/

}
