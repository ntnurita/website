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

        StringUtils.addString( session, "research.publications.other", "Other Work by PhET Researchers" );
        StringUtils.addString( session, "nav.html.licensing", "HTML5 Licensing" );
        StringUtils.addString( session, "html.licensing.title", "HTML5 Simulation Licensing Information" );
        StringUtils.addString( session, "html.licensing.comingSoon", "Information coming soon, please contact {0}." );

        // add strings for new trouble shooting pages
        StringUtils.addString( session, "nav.troubleshooting.mac", "Mac" );
        StringUtils.addString( session, "troubleshooting.mac.title", "Troubleshooting Mac" );
        StringUtils.addString( session, "nav.troubleshooting.windows", "Windows" );
        StringUtils.addString( session, "troubleshooting.windows.title", "Troubleshooting Windows" );
        StringUtils.addString( session, "nav.troubleshooting.mobile", "Tablets/Mobile Devices" );
        StringUtils.addString( session, "troubleshooting.mobile.title", "Troubleshooting Tablets/Mobile Devices" );

        // update some troubleshooting strings
        StringUtils.deleteString( session, "troubleshooting.main.q1.answer" );
        StringUtils.addString( session, "troubleshooting.main.q1.answer", "<p>Some of our simulations are Java Web Start based applications and others use Macromedia's Flash player. Flash comes with most computers while Java Web Start is a free application that can be downloaded from Sun Microsystems. To run the Java-based simulations you must have Java version 1.5 or higher installed on your computer.</p><p>To download the latest version of Java, please visit: <a {0}>https://www.java.com</a>.</p>" );

        StringUtils.deleteString( session, "about.p4" );
        StringUtils.addString( session, "about.p4", "All PhET simulations are freely available from the <a {0}>PhET website</a> and are easy to use and incorporate into the classroom. They are written in Java and Flash, and can be run using a standard web browser as long as Flash and Java are installed." );

        StringUtils.addString( session, "about.source-code.HTML5.header", "HTML5 Source Code" );

        StringUtils.deleteString( session, "about.source-code.java-flash.header" );
        StringUtils.addString( session, "about.source-code.java-flash.header", "Java/Flash/Flex Source Code (Legacy)" );

        StringUtils.deleteString( session, "about.source-code.p1" );
        StringUtils.deleteString( session, "about.source-code.p2" );
        StringUtils.deleteString( session, "about.source-code.p3" );
        StringUtils.deleteString( session, "about.source-code.p4" );

        StringUtils.addString( session, "about.source-code.p1", "Since 2013, the PhET sims have been developed in HTML5, which is the language we recommend." );

        StringUtils.addString( session, "about.source-code.p2", "The PhET HTML5 and Javascript source code lives on <a {0}>the PhET GitHub page</a>. " +
                                                                "For instructions on getting your machine set up to develop simulations using the PhET libraries, " +
                                                                "take a look at our PhET Development Overview document:" );

        StringUtils.addString( session, "about.source-code.p3", "Or join in the discussion at our Developing Interactive Simulations in HTML5 Google Group:" );
        StringUtils.addString( session, "about.source-code.p4", "This video is a quick guide to getting started developing with PhET on Windows. First, this video shows how to clone " +
                                                                "the PhET Libraries from GitHub. Next, it shows how to download and run a web server on your development machine. " +
                                                                "Finally, it shows how to launch an example simulation provided by PhET to test that everything is working nicely." );
        StringUtils.addString( session, "about.source-code.p5", "The PhET Java, Flash, and Flex source code lives in a Subversion repository. For instructions on how to browse the " +
                                                                "source code online or checkout the Subversion repository refer to our Legacy Java/Flash/Flex Source Code Document:" );

        StringUtils.deleteString( session, "home.playWithSims" );
        StringUtils.addString( session, "home.playWithSims", "Play with Simulations" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
