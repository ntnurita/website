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

        StringUtils.addString( session, "nav.html", "HTML5" );
        StringUtils.addString( session, "donate.donateNowBang", "Donate Now!" );
        StringUtils.overwriteString( session, "donate.donateNowBang", "Donate Now!", "Donate" );
        StringUtils.addString( session, "simulationMainPanel.runHTML", "Run in HTML5" );
        StringUtils.addString( session, "simulationMainPanel.worksInBrowsersTablets", "works in browsers/tablets" );
        StringUtils.addString( session, "html5.tryOurNewHTML5Sims", "TRY OUR NEW HTML5 SIMS" );
        StringUtils.addString( session, "html5.features-missing", "Your browser is missing features required to run our HTML5 simulations. Internet Explorer 9+ or recent versions of Firefox, Chrome and Safari are recommended." );
        StringUtils.addString( session, "html5.translations-soon", "Our HTML5 simulations are currently English-only. Translations are one of our next steps, and we will contact translators once this is ready. Sorry for the inconvenience!" );

        StringUtils.addString( session, "html5.crowdfundedProjects", "Support our new crowdfunded projects. Donate today:" );
        StringUtils.overwriteString( session, "html5.crowdfundedProjects", "Support our new crowdfunded projects. Donate today:", "Donate now:" );
        StringUtils.addString( session, "html5.buildTeachWithPhet", "Build \"Teach with PhET\"" );
        StringUtils.addString( session, "html5.bringCCK", "Bring Circuit Sim to iPad" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
