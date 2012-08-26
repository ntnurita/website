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

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
