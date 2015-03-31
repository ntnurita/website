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

//        StringUtils.deleteString( session, "simulationMainPanel.teachingResources" );
//        StringUtils.deleteString( session, "simulationMainPanel.mainTopics" );
//        StringUtils.deleteString( session, "simulationMainPanel.teachingIdeas" );

        StringUtils.addString( session, "simulationMainPanel.about", "About" );
        StringUtils.addString( session, "simulationMainPanel.topics", "Topics" );

        StringUtils.overwriteString( session, "simulationMainPanel.topics", "Content", "Topics" );

        StringUtils.overwriteString( session, "html5.translations-soon", "Our HTML5 simulations are currently English-only. Translations are one of our next steps, and we will contact translators once this is ready. Sorry for the inconvenience!",
                                     "All of our newest sims are written in HTML5 and run in a browser, but are currently only available in English. Enabling translations is one of our next steps. In the meantime, browse all of our translated sims <a {0}>here</a>." );

        StringUtils.addString( session, "simulationMainPanel.description", "Description" );
        StringUtils.addString( session, "simulationMainPanel.forTeachers", "For Teachers" );
        StringUtils.addString( session, "simulationMainPanel.activities", "Activities" );
        StringUtils.addString( session, "simulationMainPanel.translations", "Translations" );

        StringUtils.addString( session, "simulationMainPanel.shareAnActivity", "Share an Activity!" );
        StringUtils.addString( session, "simulationMainPanel.translateThisSim", "Translate this Sim" );
        StringUtils.addString( session, "simulationMainPanel.allSimsIn", "All sims in" );
        StringUtils.addString( session, "simulationMainPanel.translatorInfo", "Find more information <a {0}>for translators</a>" );

        StringUtils.addString( session, "simulationMainPanel.originalSim", "Original Sim and Translations" );
        StringUtils.addString( session, "simulationMainPanel.backToHTML", "Back to HTML5 Version" );

        StringUtils.overwriteString( session, "simulationMainPanel.originalSim", "Original Sim and Translations >>", "Original Sim and Translations"  );
        StringUtils.overwriteString( session, "simulationMainPanel.backToHTML", "Back to HTML5 Version >>", "Back to HTML5 Version" );

        StringUtils.addString( session, "simulationMainPanel.teacherTips", "Teacher Tips" );
        StringUtils.addString( session, "simulationMainPanel.and", "and" );
        StringUtils.addString( session, "simulationMainPanel.videoPrimer", "Video Primer" );
        StringUtils.overwriteString( session, "simulationMainPanel.videoPrimer", "and Video Primer", "Video Primer" );

        StringUtils.addString( session, "simulationMainPanel.signInPrompt", "Please sign in to watch the video primer" );

        StringUtils.overwriteString( session, "simulationMainPanel.teachersGuide", "The <a {0} target=\"_blank\">teacher's guide</a> (pdf) contains tips created by the PhET team.", "Overview of sim controls and insights into student thinking (<a {0} target=\"_blank\">Teacher Tips pdf</a>)" );

        StringUtils.overwriteString( session, "nav.teacherIdeas", "For Teachers", "Teaching Resources" );
        StringUtils.overwriteString( session, "home.teacherIdeasAndActivities", "For Teachers", "Teaching Resources" );


        StringUtils.overwriteString( session, "html5.crowdfundedProjects", "Support PhET's Annual Campaign:", "Support PhET:" );



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
