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

        /**
         * New sim page strings
         */
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
        StringUtils.addString( session, "simulationMainPanel.downloadOrRun", "Download or Run" );

        StringUtils.addString( session, "simulationMainPanel.originalSim", "Original Sim and Translations" );
        StringUtils.addString( session, "simulationMainPanel.backToHTML", "Back to HTML5 Version" );

        StringUtils.overwriteString( session, "simulationMainPanel.originalSim", "Original Sim and Translations >>", "Original Sim and Translations"  );
        StringUtils.overwriteString( session, "simulationMainPanel.backToHTML", "Back to HTML5 Version >>", "Back to HTML5 Version" );

        StringUtils.addString( session, "simulationMainPanel.teacherTips", "Teacher Tips" );
        StringUtils.addString( session, "simulationMainPanel.and", "and" );
        StringUtils.addString( session, "simulationMainPanel.videoPrimer", "Video Primer" );
        StringUtils.overwriteString( session, "simulationMainPanel.videoPrimer", "and Video Primer", "Video Primer" );

        StringUtils.addString( session, "simulationMainPanel.signInPrompt", "Please sign in to watch the video primer" );
        StringUtils.addString( session, "simulationMainPanel.noActivities", "Check out <a {0}>activities for the original sim</a> for ideas" );
        StringUtils.addString( session, "simulationMainPanel.moreActivities", "Browse <a {0}>more activities</a>." );

        StringUtils.overwriteString( session, "simulationMainPanel.teachersGuide", "The <a {0} target=\"_blank\">teacher's guide</a> (pdf) contains tips created by the PhET team.", "Overview of sim controls, model simplifications, and insights into student thinking (<a {0} target=\"_blank\">PDF</a>)." );
        StringUtils.overwriteString( session, "simulationMainPanel.teachersGuide", "Overview of sim controls and insights into student thinking (<a {0} target=\"_blank\">Teacher Tips pdf</a>)", "Overview of sim controls, model simplifications, and insights into student thinking (<a {0} target=\"_blank\">PDF</a>)." );

        StringUtils.overwriteString( session, "nav.teacherIdeas", "For Teachers", "Teaching Resources" );
        StringUtils.overwriteString( session, "home.teacherIdeasAndActivities", "For Teachers", "Teaching Resources" );

        StringUtils.overwriteString( session, "signIn.signIn", "Sign in:", "Sign in" );
        StringUtils.overwriteString( session, "signIn.email", "Email address:", "Email address" );
        StringUtils.overwriteString( session, "signIn.password", "Password:", "Password" );
        StringUtils.overwriteString( session, "home.teacherIdeasAndActivities", "For Teachers", "Teaching Resources" );

        /**
         * Sign in Strings
         */
        StringUtils.addString( session, "signIn.noAccount", "No account?" );
        StringUtils.addString( session, "signIn.needAccount", "Need an account?" );

        /**
         * Contribution Edit Strings
         */
        StringUtils.addString( session, "contribution.edit.activityTitle", "Activity Title" );
        StringUtils.addString( session, "contribution.edit.simulations.tip", "Choose the simulation(s) associated with your activity" );
        StringUtils.addString( session, "contribution.edit.simulations.selectAll", "Select All" );
        StringUtils.addString( session, "contribution.edit.answers.tip", "Please be aware that all users can see the answers if included." );
        StringUtils.addString( session, "contribution.edit.licensing", "Licensing Agreement" );
        StringUtils.addString( session, "contribution.edit.licensing.tip", "By submitting the the activity, you are agreeing to the <a href={0}>Licensing Agreement</a> above." );
        StringUtils.addString( session, "contribution.edit.uploadFiles", "Upload File(s)" );
        StringUtils.addString( session, "contribution.edit.uploadFiles.tip", "Please include an editable version of your activity." );
        StringUtils.addString( session, "contribution.edit.chooseFile", "Choose File" );
        StringUtils.addString( session, "contribution.edit.submitActivity", "Submit Activity" );
        StringUtils.addString( session, "contribution.edit.validation.mustHaveSubjects", "The activity must specify at least one subject" );
        StringUtils.addString( session, "contribution.create.alignActivity", "Align your activity to <br/><a href={0}>PhET Design Guidelines</a>" );
        StringUtils.overwriteString( session, "contribution.edit.title", "Title", "Activity Title" );

        StringUtils.deleteString( session, "tooltip.legend.goldStar" );
        StringUtils.addString( session, "tooltip.legend.goldStar", "A gold star is used to indicate high quality, inquiry-based activities that follow the PhET design guidelines and that teachers have found to be useful." );

        /**
         * New register page strings
         */
        StringUtils.addString( session, "profile.firstName", "First name" );
        StringUtils.addString( session, "profile.lastName", "Last name" );

        StringUtils.addString( session, "register.teacher", "Teacher" );
        StringUtils.addString( session, "register.student", "Student" );
        StringUtils.addString( session, "register.researcher", "Researcher" );
        StringUtils.addString( session, "register.translator", "Translator" );
        StringUtils.addString( session, "register.other", "Other" );
        StringUtils.addString( session, "register.generalSciences", "General Sciences" );
        StringUtils.addString( session, "register.earthScience", "Earth Science" );
        StringUtils.addString( session, "register.biology", "Biology" );
        StringUtils.addString( session, "register.physics", "Physics" );
        StringUtils.addString( session, "register.chemistry", "Chemistry" );
        StringUtils.addString( session, "register.astronomy", "Astronomy" );
        StringUtils.addString( session, "register.math", "Math" );
        StringUtils.addString( session, "register.years", "years" );
        StringUtils.addString( session, "register.newUser", "New user" );
        StringUtils.addString( session, "register.occasionalUser", "Occasional user (I've used a few sims)" );
        StringUtils.addString( session, "register.experiencedUser", "Experienced user (I regularly use sims in my classroom)" );
        StringUtils.addString( session, "register.powerUser", "Power user (I tell everyone about PhET!)" );



        /**
         * General strings
         */
        StringUtils.overwriteString( session, "html5.crowdfundedProjects", "Support PhET's Annual Campaign:", "Support PhET:" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
