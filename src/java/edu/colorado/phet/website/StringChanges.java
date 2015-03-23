/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * Contains strings that have been added or modified since the last production deployment. If strings by those key names
 * don't exist they will be created.
 */
public class StringChanges {

    private static Logger logger = Logger.getLogger( StringChanges.class );

    public static void checkNewStrings() {
        Session session = HibernateUtils.getInstance().openSession();

        StringUtils.addString( session, "about.accessibility", "Accessibility" );
        StringUtils.addString( session, "about.accessibility.title", "Accessibility" );
        StringUtils.addString( session, "nav.about.accessibility", "Accessibility" );

        StringUtils.addString( session, "faq.main.q2.answer.java-flash", "<p>The Java and Flash simulations will run on most PC, Mac, and Linux systems. Detailed system requirements for running the original sims are:</p><p><strong>Windows Systems</strong><br/>Intel Pentium processor<br/>Microsoft Windows XP/Vista/7<br/>256MB RAM minimum<br/>Approximately {1} MB available disk space (for full <a {0}>installation</a>)<br/>1024x768 screen resolution or better<br/>Latest version of Oracle Java<br/>Adobe Flash Player 9 or later<br/>Microsoft Internet Explorer 6 or later, Firefox 2 or later</p><p><strong>Macintosh Systems</strong><br/>G3, G4, G5 or Intel processor<br/>OS 10.5 or later<br/>256MB RAM minimum<br/>Approximately {2} MB available disk space (for full <a {0}>installation</a>)<br/>1024x768 screen resolution or better<br/>Latest version of Oracle Java<br/>Adobe Flash Player 9 or later<br/>Safari 2 or later, Firefox 2 or later</p><p><strong>Linux Systems</strong><br/>Intel Pentium processor<br/>256MB RAM minimum<br/>Approximately {3} MB disk space (for full <a {0}>installation</a>)<br/>1024x768 screen resolution or better<br/>Latest version of Oracle Java<br/>Adobe Flash Player 9 or later<br/>Firefox 2 or later<br/></p>" );
        StringUtils.addString( session, "faq.main.q2.answer.java-flash.header", "Java and Flash sims" );
        StringUtils.addString( session, "faq.main.q2.answer.html5.header", "HTML5 sims" );
        StringUtils.addString( session, "faq.main.q2.answer.html5", "<p>The new HTML5 sims can run on iPads, Chromebooks, as well as PC, Mac, and Linux systems.</p>" +
                                                                                "<p><strong>iPad</strong><br/>" +
                                                                                "iOS 7+<br/>" +
                                                                                "Safari<br/>" +
                                                                                "The HTML5 PhET sims are supported on iPad2 or later. For iPad compatible sims <a {0}>click here</a><br></p>" +
                                                                                "<p><strong>Android</strong><br/>" +
                                                                                "Not officially supported. Please contact {1} with troubleshooting issues.<br/>" +
                                                                                "If you are using the HTML5 sims on Android, we recommend using the latest version of Google Chrome and Android 4.1+</p>" +
                                                                                "<p><strong>Chromebook</strong><br/>" +
                                                                                "Latest version of Google Chrome<br/>" +
                                                                                "The HTML5 and Flash PhET sims are supported on all Chromebooks. For Chromebook compatible sims <a {2}>click here</a></p>" +
                                                                                "<p><strong>Windows Systems</strong><br/>" +
                                                                                "Microsoft Internet Explorer 9 or later, latest version of Firefox, latest version of Google Chrome</p>" +
                                                                                "<p><strong>Macintosh Systems</strong><br/>" +
                                                                                "OS 10.8.5 or later<br/>" +
                                                                                "Safari 6.1+, latest version of Firefox, latest version of Google Chrome</p>" +
                                                                                "<p><strong>Linux Systems</strong><br/>" +
                                                                                "Not officially supported. Please contact {1} with troubleshooting issues.</p>" );


        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
