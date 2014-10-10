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

        StringUtils.addString( session, "troubleshooting.windows.q1.title", "When I attempt to open a Java sim, Windows will try to open the sim with a program other than Java (Word, " +
                                                                            "Internet Explorer, etc.) or give me the message \"This XML file does not appear to have any style information " +
                                                                            "associated with it.\"");

        StringUtils.addString( session, "troubleshooting.windows.q2.title", "I am using Windows 8, when I try to open the sim I get a dialog asking me to find an app to run the sim." );
        StringUtils.addString( session, "troubleshooting.windows.q3.title", "I have Windows 2000 and can run Flash simulations but the Java based simulations do not work." );
        StringUtils.addString( session, "troubleshooting.windows.q4.title", "Why can't I completely uninstall the PhET sims that I downloaded from my Windows computer?" );
        StringUtils.addString( session, "troubleshooting.windows.q5.title", "Why is a simulation with 3D graphics not launching / displaying correctly?" );

        StringUtils.addString( session, "troubleshooting.windows.q1.answer", "<p> " +
                                                                            "Sometimes, the Java file associations can become broken. Follow these steps to re-associate both '.jnlp' and " +
                                                                            "'.jar' files to use Java: " +
                                                                            "</p> " +
                                                                            "<p> " +
                                                                            "1.  Download both a '.jar' and '.jnlp' from our website - \"Download\" and \"Run Now\", respectively.<br/> " +
                                                                            "2.  For each file, right click and choose 'Open With...\"<br/> " +
                                                                            "3.  Select \"Choose Default Program\"<br/> " +
                                                                            "4.  Check the \"Always use the selected program to open this kind of file\"<br/> " +
                                                                            "5.  Click the \"Browse...\" button<br/> " +
                                                                            "6.  For ',jnlp' select \"JavaWS.exe\" (located here on a default installation: C:\\Program Files (x86)\\Java\\jre7\\bin\\) " +
                                                                            "    For '.jar' select \"Java.exe\"<br/> " +
                                                                            "7.  Click \"Open\".<br/> " +
                                                                            "8.  Click \"OK\"<br/> " +
                                                                            "9.  The '.jnlp'/ '.jar' file should now open in Java.<br/> " +
                                                                            "</p>" );

        StringUtils.addString( session, "troubleshooting.windows.q2.answer", "<p> " +
                                                                             "There are two versions of Windows 8: 'RT' and 'Pro'. All of our simulations will work on a 'Pro' operating system. " +
                                                                             "The 'RT' version of Windows 8 doesn't allow Java to be installed, but you can use our new HTML5 simulations as well " +
                                                                             "as our Flash simulations. If you have the 'Pro' version of Windows 8, you can go here to install Java: " +
                                                                             "http://www.java.com/en/ and once that it finished you should be able to run all of the simulations. " +
                                                                             "</p> " +
                                                                             "<p> " +
                                                                             "The two versions of Windows 8 provide very different possibilities in terms of accessibility. You can find your " +
                                                                             "Windows version <a href=\"http://windows.microsoft.com/en-us/windows/which-operating-system\">http://windows.microsoft.com/en-us/windows/which-operating-system</a> " +
                                                                             "</p> " +
                                                                             "<p> " +
                                                                             "If you still encounter problems on the 'Pro' version after verifying that Java installed, it may be that the file " +
                                                                             "associations are not correctly pointing to Java to open '.jar' and '.jnlp' files. Please follow the instructions " +
                                                                             "above to re-associate the files with Java. " +
                                                                             "</p>" );

        StringUtils.addString( session, "troubleshooting.windows.q3.answer", "<p>Some Windows 2000 systems have been reported to lack part of the necessary Java configuration. These " +
                                                                             "systems will " +
                                                                             "typically start our Flash-based simulations reliably, but will appear to do nothing when launching our " +
                                                                             "Java-based " +
                                                                             "simulations.</p> " +
                                                                             " " +
                                                                             "<p><strong>To resolve this situation, please perform the following steps:</strong></p> " +
                                                                             " " +
                                                                             "<ol> " +
                                                                             "<li>From the desktop or start menu, open \"My Computer\"</li> " +
                                                                             " " +
                                                                             "<li>Click on the \"Folder Options\" item in the \"Tools\" menu</li> " +
                                                                             "<li>Click on the \"File Types\" tab at the top of the window that appears</li> " +
                                                                             "<li>Locate \"JNLP\" in the \"extensions\" column, and click once on it to select the item</li> " +
                                                                             "<li>Click on the \"change\" button</li> " +
                                                                             "<li>When asked to choose which program to use to open JNLP files, select \"Browse\"</li> " +
                                                                             "<li> " +
                                                                             "Locate the program \"javaws\" or \"javaws.exe\" in your Java installation folder (typically \"C:\\Program " +
                                                                             "Files\\Java\\j2re1.xxxx\\javaws\", where \"xxxx\" is a series of numbers indicating the software version; " +
                                                                             "choose " +
                                                                             "the latest version) " +
                                                                             "</li> " +
                                                                             " " +
                                                                             "<li>Select the program file and then click \"Open\" to use the \"javaws\" program to open JNLP files.</li> " +
                                                                             "</ol> " +
                                                                             " " +
                                                                             "<p>Java-based simulations should now function properly.</p> " +
                                                                             " " +
                                                                             "<p>Please contact us by email at <a {0}>phethelp@colorado.edu</a> " +
                                                                             "if you have any further difficulties.</p>" );
        StringUtils.addString( session, "troubleshooting.windows.q4.answer", "<p> " +
                                                                             "When a Java Web Start simulation is run in Windows, it is added to the list of programs in Control Panel " +
                                                                             "-> Add or Remove Programs. Due to a problem in Java Web Start, sometimes the item may remain in the list " +
                                                                             "even after the simulation has been removed, and Windows may report \"Unable to completely uninstall " +
                                                                             "application\". More information about this issue can be found at " +
                                                                             "{0}, " +
                                                                             "and the producers of Java have acknowledged " +
                                                                             "the problem and reported that they plan to fix it in an upcoming version of Java. " +
                                                                             "</p>" );
        StringUtils.addString( session, "troubleshooting.windows.q5.answer", "<p>If a simulation that uses 3D graphics is not launching or displaying correctly, updating your video card's drivers may fix the issue.</p> " +
                                                                             " " +
                                                                             "<p>For Windows XP / Vista / 7:</p> " +
                                                                             "<ol> " +
                                                                             "<li>Press the Win + R keys.</li> " +
                                                                             "<li>type \"DxDiag\" and press enter.</li> " +
                                                                             "<li>After the diagnostic tool opens, the video card name and manufacturer will be listed in the Display tab under Device.</li> " +
                                                                             "<li>Go to the driver manufacturer's website to find an up-to-date driver. The most common video card manufacturers are {0}, {1}, and {2} " +
                                                                             "</li> " +
                                                                             "<li>Follow all instructions to download and install the latest driver, then restart your computer.</li> " +
                                                                             "</ol>" );


        StringUtils.addString( session, "troubleshooting.mobile.p1", "The original PhET Java sims cannot run on Chromebooks, tablets like iPad or Android, or mobile phones. This is due to " +
                                                                     "their lack of support for Java. To resolve this problem we are re-programming the simulations to HTML5, which is " +
                                                                     "compatible with tablet devices, Chromebooks, and PC's." );
        StringUtils.addString( session, "troubleshooting.mobile.p2", "See our new HTML5 sims here: {0}" );
        StringUtils.addString( session, "troubleshooting.mobile.p3", "Sim compatibility, by device: {0}" );
        StringUtils.addString( session, "troubleshooting.mobile.p4", "Or watch a video about our new HTML5 efforts: {0}" );
        StringUtils.addString( session, "troubleshooting.mobile.p5", "The HTML5 interface is entirely web-based and will be easily embedded into any type of software that embeds HTML5 code." );
        StringUtils.addString( session, "troubleshooting.mobile.p6", "The plan is to port most of the sims eventually to HTML5, but there are likely a lot of less popular sims that will not " +
                                                                     "be ported. It may be several years before the majority of PhET sims are converted to HTML5.This is because porting the " +
                                                                     "sims to HTML5 is very expensive for us - it basically involves re-writing all of the code. Donations to the program are " +
                                                                     "helping and we are looking to apply for grants that would allow us to do more conversions (and write new sims in HTML5)." );
        StringUtils.addString( session, "troubleshooting.mobile.p7", "These next-generation simulations will not only be touch and tablet optimized but will also eventually include additional " +
                                                                     "improvements such as enhanced data input-output capabilities to support 3rd party customization." );

        StringUtils.addString( session, "troubleshooting.mac.faqs", "FAQs" );
        StringUtils.addString( session, "troubleshooting.windows.faqs", "FAQs" );




        StringUtils.deleteString( session, "troubleshooting.java.faqs" );
        StringUtils.deleteString( session, "troubleshooting.java.intro" );
        StringUtils.deleteString( session, "troubleshooting.flash.intro" );
        StringUtils.deleteString( session, "troubleshooting.flash.blankWindow" );
        StringUtils.deleteString( session, "troubleshooting.flash.olderVersions" );
        StringUtils.deleteString( session, "troubleshooting.javascript.q1.no" );
        StringUtils.deleteString( session, "troubleshooting.javascript.faqs" );
        StringUtils.deleteString( session, "troubleshooting.javascript.q1.yes" );
        StringUtils.deleteString( session, "troubleshooting.javascript.q1.title" );
        StringUtils.deleteString( session, "troubleshooting.javascript.q2.title" );
        StringUtils.deleteString( session, "troubleshooting.javascript.q3.title" );
        StringUtils.deleteString( session, "troubleshooting.javascript.q4.title" );
        StringUtils.deleteString( session, "troubleshooting.javascript.q5.title" );
        StringUtils.deleteString( session, "troubleshooting.javascript.q2.answer" );
        StringUtils.deleteString( session, "troubleshooting.javascript.q3.answer" );
        StringUtils.deleteString( session, "troubleshooting.javascript.intro" );
        StringUtils.deleteString( session, "troubleshooting.flash.toRun" );
        StringUtils.deleteString( session, "troubleshooting.javascript.q4.answer" );
        StringUtils.deleteString( session, "troubleshooting.javascript.q5.answer" );
        StringUtils.deleteString( session, "troubleshooting.java.q1.title" );
        StringUtils.deleteString( session, "troubleshooting.java.q2.title" );
        StringUtils.deleteString( session, "troubleshooting.java.q3.title" );
        StringUtils.deleteString( session, "troubleshooting.java.q4.title" );
        StringUtils.deleteString( session, "troubleshooting.java.q5.title" );
        StringUtils.deleteString( session, "troubleshooting.java.q6.title" );
        StringUtils.deleteString( session, "troubleshooting.java.q7.title" );
        StringUtils.deleteString( session, "troubleshooting.java.q8.title" );
        StringUtils.deleteString( session, "troubleshooting.java.q1.answer" );
        StringUtils.deleteString( session, "troubleshooting.java.q2.answer" );
        StringUtils.deleteString( session, "troubleshooting.java.q3.answer" );
        StringUtils.deleteString( session, "troubleshooting.java.q4.answer" );
        StringUtils.deleteString( session, "troubleshooting.java.q5.answer" );
        StringUtils.deleteString( session, "troubleshooting.java.q6.answer" );
        StringUtils.deleteString( session, "troubleshooting.java.q7.answer" );
        StringUtils.deleteString( session, "troubleshooting.java.q8.answer" );
        StringUtils.deleteString( session, "troubleshooting.flash.q1.title" );
        StringUtils.deleteString( session, "troubleshooting.flash.q1.answer" );
        StringUtils.deleteString( session, "troubleshooting.javascript.notify" );
        StringUtils.deleteString( session, "troubleshooting.javascript.notJava" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
