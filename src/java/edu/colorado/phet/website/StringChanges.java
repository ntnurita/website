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

        StringUtils.addString( session, "troubleshooting.main.q19.title", "Why is a simulation with 3D graphics not launching / displaying correctly?");
        StringUtils.addString( session, "troubleshooting.main.q19.answer", "<p>If a simulation that uses 3D graphics is not launching or displaying correctly, updating your video card's drivers may fix the issue.</p><p>For Windows XP / Vista / 7:</p><ul><li>Press the Win + R keys.</li><li>Type \"DxDiag\" and press enter.</li><li>After the diagnostic tool opens, the video card name and manufacturer will be listed in the Display tab under Device.</li><li>Go to the driver manufacturer's website to find an up-to-date driver. The most common video card manufacturers are {0}, {1}, and {2}.</li><li>Follow all instructions to download and install the latest driver, then restart your computer.</li></ul><p>For Macs:</p><ul><li>Updating the video card driver can be done through the normal software update process (Available from the Apple menu => Software Update)</li></ul>");
        
        StringUtils.addString( session, "nav.troubleshooting.javaSecurity", "Java Security Advisory Information" );
        StringUtils.addString( session, "troubleshooting.javaSecurity.title", "----- TITLE HERE -----" );
        StringUtils.overwriteString( session, "troubleshooting.javaSecurity.title", "----- TITLE HERE -----", "PhET Information on the Java Security Advisory" );
        StringUtils.addString( session, "troubleshooting.javaSecurity.intro", "On January 10th, 2012, the United States Computer Emergency Readiness Team (US-CERT) released a <a {0}>bulletin regarding security issues with Java</a>. One of the recommendations in this bulletin is to disable Java in web browsers. In many cases, this will prevent running PhET simulations directly from the PhET web site." );
        StringUtils.addString( session, "troubleshooting.javaSecurity.steps", "If you are a user of PhET simulations and are currently running Java 7, we recommend that you take the following steps in order to configure your computer to run safely and yet still be able to use PhET simulations:" );
        StringUtils.addString( session, "troubleshooting.javaSecurity.upgradeStep", "<a {0}>Upgrade to the latest version of Java</a>." );
        StringUtils.addString( session, "troubleshooting.javaSecurity.disableStep", "Disable Java from running in web browsers. Note that this will prevent the \"Run Now\" option from launching simulations from the PhET site. <a {0}>This article</a> has information about how to perform this operation." );
        StringUtils.addString( session, "troubleshooting.javaSecurity.downloadStep", "Run the simulations by downloading them via the \"Download\" button, and then running locally." );
        StringUtils.addString( session, "troubleshooting.javaSecurity.monitoring", "PhET will continue to monitor the situation surrounding Java security issues and will issue an update when the problems are fully resolved." );
        StringUtils.addString( session, "troubleshooting.javaSecurity.questions", "If you have questions or concerns regarding this, please contact us at {0}." );
        StringUtils.addString( session, "troubleshooting.javaSecurity.thankyou", "Thank you for using and supporting PhET Interactive Simulations!" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
