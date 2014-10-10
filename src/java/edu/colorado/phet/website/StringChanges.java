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

        StringUtils.addString( session, "troubleshooting.mac.q1.title", "When I run simulations from the PhET Offline Website Installer, I am seeing a dialog that says " +

                                                                         "(or something similar). What does this mean?" );
        StringUtils.addString( session, "troubleshooting.mac.q2.title", "When I click \"run now\" to start the simulation all I get is a text file that opens?" );
        StringUtils.addString( session, "troubleshooting.mac.q3.title", "Why is a simulation with 3D graphics not launching / displaying correctly?" );
        StringUtils.addString( session, "troubleshooting.mac.q4.title", "When I open 'Molecule Shapes', 'Plate Tectonics', or 'Molecule Shapes: Basics', I get a dialog saying " +
                                                                        "\"This simulation was unable to start.\"" );
        StringUtils.addString( session, "troubleshooting.mac.q5.title", "When I open a Java sim I get a dialog saying the sim \"can't be opened because it is from an unidentified developer.\"" );
        StringUtils.addString( session, "troubleshooting.mac.q6.title", "When I click 'Run Now' from a sim page, nothing happens!" );

        StringUtils.deleteString( session, "troubleshooting.mac.q1.answer" );
        StringUtils.deleteString( session, "troubleshooting.mac.q2.answer" );
        StringUtils.deleteString( session, "troubleshooting.mac.q3.answer" );
        StringUtils.deleteString( session, "troubleshooting.mac.q4.answer" );
        StringUtils.deleteString( session, "troubleshooting.mac.q5.answer" );
        StringUtils.deleteString( session, "troubleshooting.mac.q6.answer.firefox" );
        StringUtils.deleteString( session, "troubleshooting.mac.q6.answer.chrome" );
        StringUtils.deleteString( session, "troubleshooting.mac.q6.answer.safari" );

        StringUtils.addString( session, "troubleshooting.mac.q1.answer", "<p> " +
                                                                        "The PhET simulations that are distributed with the installer include a \"digital certificate\" that verifies that " +
                                                                        "these simulations were actually created by PhET. This is a security measure that helps to prevent an unscrupulous " +
                                                                        "individual from creating applications that claim to be produced by PhET but are not. If the certificate acceptance " +
                                                                        "dialog says that the publisher is \"PhET, University of Colorado\", and the dialog also says that the signature was " +
                                                                        "validated by a trusted source, you can have a high degree of confidence that the application was produced by the " +
                                                                        "PhET team. " +
                                                                        "</p> " +
                                                                        "<p> " +
                                                                        "On most systems, it is possible to permanently accept the PhET certificate and thereby prevent this dialog from " +
                                                                        "appearing each time a simulation is run locally. Most Windows and Max OSX systems have a check box on the certificate " +
                                                                        "acceptance dialog that says \"Always trust content from this publisher\". Checking this box will configure your system " +
                                                                        "in such a way that the dialog will no longer appear when starting up PhET simulations. " +
                                                                        "</p>" );

        StringUtils.addString( session, "troubleshooting.mac.q2.answer", "<p> " +
                                                                         "This problem will affect mac users who recently installed Apple's \"Java for Mac OS X 10.5 Update 4\". The update will " +
                                                                         "typically be done via Software Update, or automatically. After installing this update, the problem appears: clicking " +
                                                                         "on JNLP files in Safari or FireFox caused the JNLP file to open in TextEdit, instead of starting Java Web Start. " +
                                                                         "</p> " +
                                                                         "<p> " +
                                                                         "The fix is:<br/> " +
                                                                         "1. Go to <a href=\"http://support.apple.com/downloads/Java_for_Mac_OS_X_10_5_Update_4\">http://support.apple.com/downloads/Java_for_Mac_OS_X_10_5_Update_4</a><br/> " +
                                                                         "2. Click Download to download a .dmg file<br/> " +
                                                                         "3. When the .dmg has downloaded, double.click on it (if it doesn't mount automatically)<br/> " +
                                                                         "4. Quit all applications<br/> " +
                                                                         "5. Run the update installer " +
                                                                         "</p>" );

        StringUtils.addString( session, "troubleshooting.mac.q3.answer", "<p> " +
                                                                         "If a simulation that uses 3D graphics is not launching or displaying correctly, updating your video card's " +
                                                                         "drivers may fix the issue. " +
                                                                         "</p> " +
                                                                         "<p> " +
                                                                         "Updating the video card driver can be done through the normal software update process (Available from the Apple menu " +
                                                                         "=> Software Update) " +
                                                                         "</p>" );

        StringUtils.addString( session, "troubleshooting.mac.q4.answer", "<p> " +
                                                                         "'Molecule Shapes', 'Plate Tectonics', and 'Molecule Shapes: Basics' rely on advanced graphics libraries (LWJGL) " +
                                                                         "that many OS and Java combinations don't support. " +
                                                                         "</p> " +
                                                                         "<p> " +
                                                                         "It shows up on ALL Mac OS X 10.8, many 10.6 and 10.7 computers. " +
                                                                         "</p> " +
                                                                         "<p> " +
                                                                         "Unfortunately, at this time the incompatibility problem is out of our hands and unless Oracle or Apple address this " +
                                                                         "issue, it is unlikely the sim will run successfully. " +
                                                                         "</p> " +
                                                                         "<p> " +
                                                                         "We are hoping incompatibilities like this will be a thing of the past once we move most of our sims to HTML5. " +
                                                                         "</p>" );

        StringUtils.addString( session, "troubleshooting.mac.q5.answer", "<p> " +
                                                                         "Gatekeeper, introduced in Mountain Lion (OS X 10.8), is designed to prevent potentially malicious apps from launching. " +
                                                                         "When you attempt to launch an app that doesn't meet certain criteria, Gatekeeper will block the launch. To override this, " +
                                                                         "you can modify Gatekeeper's default settings (but we don't always recommend it), or explicitly right/control.click the " +
                                                                         "application and choose Open. " +
                                                                         "</p> " +
                                                                         "<p> " +
                                                                         "1. Download the .jar file for each sim you would like to use. (Or download all of them)<br/> " +
                                                                         "2. In Finder, Control-click or right click the icon of the sim.<br/> " +
                                                                         "3. Select Open from the top of contextual menu that appears.<br/> " +
                                                                         "4. Click Open in the dialog box. If prompted, enter an administrator name and password.<br/> " +
                                                                         "</p> " +
                                                                         "<p> " +
                                                                         "The other option is to go into preferences and change the security settings: " +
                                                                         "System Preferences>Security and Privacy>General> Change 'Allow applications downloaded from' to \"Anywhere\"> Accept prompt \"Allow From " +
                                                                         "Anywhere\" " +
                                                                         "We don't advise this because it disables Gatekeeper. It is generally safer to maintain Gatekeeper and individually open the files. " +
                                                                         "</p>" );

        StringUtils.addString( session, "troubleshooting.mac.q6.answer.firefox", "<p><strong>Firefox</strong></p> " +
                                                                                 "<p> " +
                                                                                 "When opening a sim in Firefox, a dialog will popup asking you what Firefox should do with the file. Click Open with " +
                                                                                 "Java Web Start (default). " +
                                                                                 "</p> " +
                                                                                 "<p> " +
                                                                                 "If you then receive a message saying the sim \"can't be opened because it is from an unidentified developer.\" Proceed " +
                                                                                 "with the instructions in the appropriate FAQ and download the .jar file for that sim. " +
                                                                                 "</p>" );

        StringUtils.addString( session, "troubleshooting.mac.q6.answer.chrome", "<p><strong>Chrome</strong></p> " +
                                                                                "<p> " +
                                                                                "  When opening a sim in Chrome, the browser will automatically ask to either Discard or Keep the file. " +
                                                                                "  The prompt pops up at the bottom of the screen. " +
                                                                                "</p> {0}" +
                                                                                "<p> " +
                                                                                "Click 'Keep'. You will then have an icon displaying the newly " +
                                                                                "downloaded file. Click this to run the sim. " +
                                                                                "</p>{1}" +
                                                                                "<p> " +
                                                                                "If you then receive a message saying the sim \"can't be opened because it is from an unidentified developer.\" Proceed " +
                                                                                "with the instructions in the appropriate FAQ and download the .jar file for that sim. " +
                                                                                "</p>");

        StringUtils.addString( session, "troubleshooting.mac.q6.answer.safari", "<p><strong>Safari</strong></p> " +
                                                                                "<p> " +
                                                                                "When opening a sim in Safari, the browser will automatically download the .jnlp file after clicking \"Run Now\". " +
                                                                                "However, no notification is given. Click the Downloads {0}" +
                                                                                "button to show recent downloads. Click the latest .jnlp file " +
                                                                                "to run the sim. " +
                                                                                "</p>{1}" +
                                                                                "<p> " +
                                                                                "If you then receive a message saying the sim \"can't be opened because it is from an unidentified developer.\" Proceed " +
                                                                                "with the instructions in the appropriate FAQ and download the .jar file for that sim. " +
                                                                                "</p>");


        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
