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
        StringUtils.deleteString( session, "clickersDemo" );
        StringUtils.deleteString( session,"lectureDemo");
        StringUtils.deleteString( session, "nav.clickersDemo" );
        StringUtils.deleteString( session,"nav.lectureDemo");
        StringUtils.deleteString( session, "nav.clickersDemo.title" );
        StringUtils.deleteString( session,"nav.lectureDemo.title");
        StringUtils.deleteString( session, "clickersDemo.title" );
        StringUtils.deleteString( session,"lectureDemo.title");

        StringUtils.deleteString( session, "nav.workshopFacilitatorsGuide" );
        StringUtils.deleteString( session, "nav.tipsForUsingPhet" );
        StringUtils.deleteString( session, "nav.planningToUsePhet" );
        StringUtils.deleteString( session, "nav.usingPhetInLecture" );
        StringUtils.deleteString( session, "nav.lectureDemo" );
        StringUtils.deleteString( session, "nav.clickersDemo" );
        StringUtils.deleteString( session, "nav.activitesDesign" );
        StringUtils.deleteString( session, "nav.virtualWorkshop" );

        StringUtils.addString( session, "nav.forTeachers.workshopFacilitatorsGuide", "PhET Workshop Facilitators Guide");
        StringUtils.addString( session, "nav.forTeachers.tipsForUsingPhet", "Tips for Using PhET" );
        StringUtils.addString( session, "nav.forTeachers.planningToUsePhet", "Planning to Use PhET" );
        StringUtils.addString( session, "nav.forTeachers.usingPhetInLecture", "Using PhET in Lecture" );
        StringUtils.addString( session, "nav.forTeachers.lectureDemo", " Interactive Lecture Demonstrations" );
        StringUtils.addString( session, "nav.forTeachers.clickersDemo", "Using PhET with Clickers" );
        StringUtils.addString( session, "nav.forTeachers.activitesDesign", "Designing Effective Activities for use in K12" );
        StringUtils.addString( session, "nav.forTeachers.virtualWorkshop", "Take a Virtual PhET Workshop" );

        StringUtils.deleteString( session, "nav.workshopFacilitatorsGuide.title" );
        StringUtils.deleteString( session, "nav.tipsForUsingPhet.title" );
        StringUtils.deleteString( session, "nav.planningToUsePhet.title" );
        StringUtils.deleteString( session, "nav.usingPhetInLecture.title" );
        StringUtils.deleteString( session, "nav.lectureDemo.title" );
        StringUtils.deleteString( session, "nav.clickersDemo.title" );
        StringUtils.deleteString( session, "nav.activitesDesign.title" );
        StringUtils.deleteString( session, "nav.virtualWorkshop.title" );

        StringUtils.deleteString( session, "nav.forTeachers.workshopFacilitatorsGuide.title");
        StringUtils.deleteString( session, "nav.forTeachers.tipsForUsingPhet.title" );
        StringUtils.deleteString( session, "nav.forTeachers.planningToUsePhet.title" );
        StringUtils.deleteString( session, "nav.forTeachers.usingPhetInLecture.title" );
        StringUtils.deleteString( session, "nav.forTeachers.lectureDemo.title" );
        StringUtils.deleteString( session, "nav.forTeachers.clickersDemo.title" );
        StringUtils.deleteString( session, "nav.forTeachers.activitesDesign.title" );
        StringUtils.deleteString( session, "nav.forTeachers.virtualWorkshop.title" );

        // remove un-prefixed for-teachers strings
        StringUtils.deleteString( session, "workshopFacilitatorsGuide.title" );
        StringUtils.deleteString( session, "tipsForUsingPhet.title" );
        StringUtils.deleteString( session, "planningToUsePhet.title" );
        StringUtils.deleteString( session, "usingPhetInLecture.title" );
        StringUtils.deleteString( session, "lectureDemo.title" );
        StringUtils.deleteString( session, "clickersDemo.title" );
        StringUtils.deleteString( session, "activitesDesign.title" );
        StringUtils.deleteString( session, "virtualWorkshop.title" );

        // add prefixed for-teachers strings
        StringUtils.addString( session, "forTeachers.workshopFacilitatorsGuide.title", "PhET Workshop Facilitators Guide");
        StringUtils.addString( session, "forTeachers.tipsForUsingPhet.title", "Tips for Using PhET" );
        StringUtils.addString( session, "forTeachers.planningToUsePhet.title", "Planning to Use PhET" );
        StringUtils.addString( session, "forTeachers.usingPhetInLecture.title", "Using PhET in Lecture" );
        StringUtils.addString( session, "forTeachers.lectureDemo.title", " Interactive Lecture Demonstrations" );
        StringUtils.addString( session, "forTeachers.clickersDemo.title", "Using PhET with Clickers" );
        StringUtils.addString( session, "forTeachers.activitesDesign.title", "Designing Effective Activities for use in K12" );
        StringUtils.addString( session, "forTeachers.virtualWorkshop.title", "Take a Virtual PhET Workshop" );

        StringUtils.addString( session, "nav.exampleworkshops", "Example Workshops" );
        StringUtils.addString( session, "exampleworkshops.title", "Example Workshops" );

        StringUtils.addString( session, "nav.about.team", "People" );
        StringUtils.addString( session, "about.team.title", "People" );

        // delete unused strings
        StringUtils.deleteString( session, "contribution.view.creativecommons" );
        StringUtils.deleteString( session, "contribution.edit.creativeCommons" );
        StringUtils.deleteString( session, "nav.exampleworkshops.title" );
        StringUtils.deleteString( session, "exampleworkshops" );
        StringUtils.deleteString( session, "virtualWorkshop" );
        StringUtils.deleteString( session, "activitesDesign" );
        StringUtils.deleteString( session, "usingPhetInLecture" );
        StringUtils.deleteString( session, "planningToUsePhet" );
        StringUtils.deleteString( session, "workshopFacilitatorsGuide" );
        StringUtils.deleteString( session, "about.team" );

        // change navigation strings on home screen
        StringUtils.overwriteString( session, "home.workshops", "Workshops / Materials", "Workshops" );

        StringUtils.deleteString( session, "home.submitActivity" );
        StringUtils.addString( session, "home.submitActivity", "Share your Activities" );

        StringUtils.deleteString( session, "nav.teacherIdeas.submit" );
        StringUtils.addString( session, "nav.teacherIdeas.submit", "Share your Activities" );

        // remove strings from TeacherIdeasPanel b/c the translation was scraped for now
        StringUtils.deleteString( session, "teacherIdeas.browseSection" );
        StringUtils.deleteString( session, "teacherIdeas.contributeSection" );
        StringUtils.deleteString( session, "teacherIdeas.adviceSection" );
        StringUtils.deleteString( session, "teacherIdeas.guidelinesSection" );
        StringUtils.deleteString( session, "teacherIdeas.guidelines" );
        StringUtils.deleteString( session, "teacherIdeas.exampleSection" );
        StringUtils.deleteString( session, "teacherIdeas.examples" );
        StringUtils.deleteString( session, "teacherIdeas.examples" );
        StringUtils.deleteString( session, "teacherIdeas.examples" );
        StringUtils.deleteString( session, "teacherIdeas.welcome" );
        StringUtils.deleteString( session, "teacherIdeas.start" );
        StringUtils.deleteString( session, "teacherIdeas.contribute" );

        StringUtils.deleteString( session, "teacherIdeas.examples.highSchool" );
        StringUtils.deleteString( session, "teacherIdeas.examples.modernPhysics" );
        StringUtils.deleteString( session, "teacherIdeas.examples.everydayPhysics" );

        // add validation string for cc license
        StringUtils.addString( session, "contribution.edit.validation.mustHaveLicense", "Activities will not be accepted unless you agree to use the Creative Commons license" );

        //  StringUtils.addString( session, "research.publications.other", "Other Work by PhET Researchers" );

        // Strings recently added from master branch
        StringUtils.addString( session, "research.publications.other", "Other Work by PhET Researchers" );
        StringUtils.addString( session, "nav.html.licensing", "HTML5 Licensing" );
        StringUtils.addString( session, "html.licensing.title", "HTML5 Simulation Licensing Information" );
        StringUtils.addString( session, "html.licensing.comingSoon", "Information coming soon, please contact {0}." );

        // update titles of browse and submit activities pages
        StringUtils.deleteString( session, "contribution.create.pageTitle" );
        StringUtils.deleteString( session, "contribution.search.title" );
        StringUtils.addString( session, "contribution.create.pageTitle", "Share your Activities" );
        StringUtils.addString( session, "contribution.search.title", "Browse Activities" );

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

        // update research strings

        StringUtils.deleteString( session, "research.intro" );
        StringUtils.addString( session, "research.intro", "The PhET simulation design principles are based on research on how students learn (Bransford et al., 2000) and from our simulation " +
                                                          "interviews (see <a {0}>PhET Design Process</a>). Between four and six think-aloud style interviews " +
                                                          "with individual students are done with each simulation. These interviews provide a rich data source for studying interface design and " +
                                                          "student learning. The <a {1}>PhET Look and Feel</a> briefly describes our interface design " +
                                                          "principles and a complete discussion is found in the pair of papers by <a {2}>Adams et al., 2008</a>." );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
