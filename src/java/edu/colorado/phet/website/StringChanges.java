/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

import com.navnorth.learningregistry.util.StringUtil;

/**
 * Contains strings that have been addedor modified since the last production deployment. If strings by those key names
 * don't exist they will be created.
 */
public class StringChanges {

    private static Logger logger = Logger.getLogger( StringChanges.class );

    public static void checkNewStrings() {
        Session session = HibernateUtils.getInstance().openSession();

        StringUtils.addString( session, "troubleshooting.mac.q7.title", "When I click \"run now\" my computer asks me which application to use to open the file, but Java isn’t listed." );
        StringUtils.addString( session, "troubleshooting.mac.q7.answer", "<p> " +
                                                                         "In order to open a '.jnlp' file (the type of file used for “run now”), you will need to use Java Web Start. Java Web Start is part of " +
                                                                         "the default Java installation. " +
                                                                         "</p> " +
                                                                         "<p> " +
                                                                         "When you are prompted with the dialog asking for the app to use to open the file, click \"Choose program\", your finder window should " +
                                                                         "open. Press Cmd-Shift-G to open the go to folder. Paste this folder: /System/Library/CoreServices " +
                                                                         "This is where Java Web Start should be located on default mac installation. There should be an executable called 'Java-Web-Start'. " +
                                                                         "Select this app. " +
                                                                         "</p>" );

        StringUtils.addString( session, "nav.ipad-tablet", "iPad/Tablet" );
        StringUtils.addString( session, "nav.chromebook", "Chromebook" );
        StringUtils.addString( session, "nav.by-device", "By Device" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
