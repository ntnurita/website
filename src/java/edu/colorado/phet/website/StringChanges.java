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

        StringUtils.addString( session, "simulationMainPanel.description", "Description" );
        StringUtils.addString( session, "simulationMainPanel.forTeachers", "For Teachers" );
        StringUtils.addString( session, "simulationMainPanel.activities", "Activities" );
        StringUtils.addString( session, "simulationMainPanel.translations", "Translations" );

        StringUtils.addString( session, "simulationMainPanel.originalSim", "Original Sim and Translations >>" );
        StringUtils.addString( session, "simulationMainPanel.backToHTML", "Back to HTML5 Version >>" );

        StringUtils.addString( session, "simulationMainPanel.teacherTips", "Teacher Tips" );
        StringUtils.addString( session, "simulationMainPanel.and", "and" );
        StringUtils.addString( session, "simulationMainPanel.videoPrimer", "Video Primer" );
        StringUtils.overwriteString( session, "simulationMainPanel.videoPrimer", "and Video Primer", "Video Primer" );

        StringUtils.addString( session, "simulationMainPanel.signInPrompt", "Please sign in to watch the video primer" );

        StringUtils.overwriteString( session, "simulationMainPanel.teachersGuide", "The <a {0} target=\"_blank\">teacher's guide</a> (pdf) contains tips created by the PhET team.", "Overview of sim controls and insights into student thinking (<a {0} target=\"_blank\">Teacher Tips pdf</a>)" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
