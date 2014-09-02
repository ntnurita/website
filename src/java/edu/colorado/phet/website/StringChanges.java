/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.content.forteachers.ActivitiesdesignPanel;
import edu.colorado.phet.website.content.forteachers.ClickersPanel;
import edu.colorado.phet.website.content.forteachers.LectureDemoPanel;
import edu.colorado.phet.website.content.forteachers.LectureOverviewPanel;
import edu.colorado.phet.website.content.forteachers.PlanningPanel;
import edu.colorado.phet.website.content.forteachers.TipsPanel;
import edu.colorado.phet.website.content.forteachers.VirtualWorkshopPanel;
import edu.colorado.phet.website.content.workshops.WorkshopFacilitatorsGuidePanel;
import edu.colorado.phet.website.menu.NavLocation;
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

        StringUtils.addString( session,"nav.workshopFacilitatorsGuide", "PhET Workshop Facilitators Guide");
        StringUtils.addString( session, "nav.tipsForUsingPhet", "Tips for Using PhET" );
        StringUtils.addString( session, "nav.planningToUsePhet", "Planning to Use PhET" );
        StringUtils.addString( session, "nav.usingPhetInLecture", "Using PhET in Lecture" );
        StringUtils.addString( session,"nav.lectureDemo", " Interactive Lecture Demonstrations" );
        StringUtils.addString( session, "nav.clickersDemo", "Using PhET with Clickers" );
        StringUtils.addString( session, "nav.activitesDesign", "Designing Effective Activities for use in K12" );
        StringUtils.addString( session, "nav.virtualWorkshop", "Take a Virtual PhET Workshop" );

        StringUtils.addString( session,"nav.workshopFacilitatorsGuide.title", "PhET Workshop Facilitators Guide");
        StringUtils.addString( session, "nav.tipsForUsingPhet.title", "Tips for Using PhET" );
        StringUtils.addString( session, "nav.planningToUsePhet.title", "Planning to Use PhET" );
        StringUtils.addString( session, "nav.usingPhetInLecture.title", "Using PhET in Lecture" );
        StringUtils.addString( session,"nav.lectureDemo.title", " Interactive Lecture Demonstrations" );
        StringUtils.addString( session, "nav.clickersDemo.title", "Using PhET with Clickers" );
        StringUtils.addString( session, "nav.activitesDesign.title", "Designing Effective Activities for use in K12" );
        StringUtils.addString( session, "nav.virtualWorkshop.title", "Take a Virtual PhET Workshop" );


        StringUtils.addString( session,"workshopFacilitatorsGuide.title", "PhET Workshop Facilitators Guide");
        StringUtils.addString( session, "tipsForUsingPhet.title", "Tips for Using PhET" );
        StringUtils.addString( session, "planningToUsePhet.title", "Planning to Use PhET" );
        StringUtils.addString( session, "usingPhetInLecture.title", "Using PhET in Lecture" );
        StringUtils.addString( session,"lectureDemo.title", " Interactive Lecture Demonstrations" );
        StringUtils.addString( session, "clickersDemo.title", "Using PhET with Clickers" );
        StringUtils.addString( session, "activitesDesign.title", "Designing Effective Activities for use in K12" );
        StringUtils.addString( session, "virtualWorkshop.title", "Take a Virtual PhET Workshop" );

        StringUtils.addString( session,"workshopFacilitatorsGuide", "PhET Workshop Facilitators Guide");
        StringUtils.addString( session, "tipsForUsingPhet", "Tips for Using PhET" );

        StringUtils.addString( session, "planningToUsePhet", "Planning to Use PhET" );
        StringUtils.addString( session, "usingPhetInLecture", "Using PhET in Lecture" );
        StringUtils.addString( session,"lectureDemo", "Interactive Lecture Demonstrations" );
        StringUtils.addString( session, "clickersDemo", "Using PhET with Clickers" );
        StringUtils.addString( session, "activitesDesign", "Designing Effective Activities for use in K12" );
        StringUtils.addString( session, "virtualWorkshop", "Take a Virtual PhET Workshop" );

        StringUtils.addString( session, "exampleworkshops", "Example Workshops" );
        StringUtils.addString( session, "nav.exampleworkshops.title", "Example Workshops" );
        StringUtils.addString( session, "nav.exampleworkshops", "Example Workshops" );
        StringUtils.addString( session, "exampleworkshops.title", "Example Workshops" );

        StringUtils.addString( session, "about.team", "People" );
        StringUtils.addString( session, "nav.about.team", "People" );
        StringUtils.addString( session, "about.team.title", "People" );

//        StringUtils.deleteString( session, "contribution.view.creativeCommons" );
//        StringUtils.addString( session, "contribution.view.creativeCommons", "I agree to submit this activity under the Creative Commons (required)" );

        // change navigation strings on home screen
        StringUtils.overwriteString( session, "home.submitActivity", "Contribute Activities", "Submit an Activity" );
        StringUtils.overwriteString( session, "home.workshops", "Workshops / Materials", "Workshops" );

        // add validation string for cc license
        StringUtils.addString( session, "contribution.edit.validation.mustHaveLicense", "Activities will not be accepted unless you agree to use the Creative Commons licence" );
        StringUtils.deleteString( session, "contribution.edit.validation.mustHaveLicence" );

        //  StringUtils.addString( session, "research.publications.other", "Other Work by PhET Researchers" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
