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

        StringUtils.deleteString( session, "installer.mostUpToDate" );
        StringUtils.addString( session, "installer.mostUpToDate", "Created {0,date,short}. Updates available <a {1}>online</a>." );

        StringUtils.deleteString( session, "teacherIdeas.guide.authors" );
        StringUtils.addString( session, "teacherIdeas.guide.authors", "PhET Interactive Simulations PhET Professional Development Team" );

        StringUtils.deleteString( session, "teacherIdeas.guide.date" );
        StringUtils.addString( session, "teacherIdeas.guide.date", "11/20/2014" );

        StringUtils.deleteString( session, "teacherIdeas.guide.diagramReasoning.title" );
        StringUtils.deleteString( session, "teacherIdeas.guide.diagramReasoning" );

        StringUtils.deleteString( session, "teacherIdeas.guide.title" );
        StringUtils.addString( session, "teacherIdeas.guide.title", "Creating PhET Interactive Simulations Activities" );
        StringUtils.addString( session, "teacherIdeas.guide.subtitle", "PhET's Approach to Guided Inquiry" );

        StringUtils.deleteString( session, "teacherIdeas.guide.integration" );
        StringUtils.addString( session, "teacherIdeas.guide.integration", "The PhET Interactive Simulations Project, <a href=\"http//phet.colorado.edu\">http//phet.colorado.edu</a> provides simulations (sims) specifically designed and tested to support student learning. However, what students do with the sims is as important as the simulations themselves. PhET sims may be used in many different types of activities but we believe the sims are most effective with <u>activities which use guided inquiry</u> enabling students to construct their own understanding. In order to productively support student exploration, we suggest:" );
        StringUtils.deleteString( session, "teacherIdeas.guide.specificLearningGoals" );
        StringUtils.addString( session, "teacherIdeas.guide.specificLearningGoals", "The learning goals need to be <u>specific and measurable</u>. It is important that the activity goals are well-defined for your particular standards and student population because each sim is designed to support many possible learning goals." );
        StringUtils.deleteString( session, "teacherIdeas.guide.minimalDirections" );
        StringUtils.addString( session, "teacherIdeas.guide.minimalDirections", "The sims are designed and tested to encourage students to <u>explore and use sense making</u>. Recipe-type directions can suppress active thinking, resulting in a focus on following directions and answering questions correctly. For example, in a sim about motion, avoid instructions such as \"set the gravity to zero;\" instead provide a challenge like, \"Find out what affects the speed of the Skater.\"" );
        StringUtils.deleteString( session, "teacherIdeas.guide.priorKnowledge" );
        StringUtils.addString( session, "teacherIdeas.guide.priorKnowledge", "Ask questions to <u>elicit</u> student ideas about the topic. For example, to start using a sim about dissolving ask: \"What might happen if you add a lot of salt to water?\" and \"Do you think it matters what solid you add to water?\" Guide students to use the simulation, and discussion with their partner, to test those ideas and resolve any inconsistencies." );
        StringUtils.deleteString( session, "teacherIdeas.guide.encourageReasoning" );
        StringUtils.addString( session, "teacherIdeas.guide.encourageReasoning", "The sims are designed to help students develop and assess their understanding and reasoning about science topics. The activity should be geared towards encouraging the student to operate in <u>learning mode</u> not <u>performance mode</u>. Emphasize questions that require making sense of the simulation topic and ideas, using words and diagrams, rather than questions with right/wrong answers. For example, \"Design an experiment to see what relationships you can find between how hard you push a box and how fast it will move. Make a data table and a graph to help explain your ideas,\" and \"How might your graph change if the box had a person sitting on it? Explain your reasoning.\"" );
        StringUtils.deleteString( session, "teacherIdeas.guide.worldExperiences" );
        StringUtils.addString( session, "teacherIdeas.guide.worldExperiences", "Students learn more when they can see that science is relevant to their everyday life. The sims often use images from everyday life, but where possible the activity should explicitly help them <u>relate science to their personal experience</u>. As you write questions, consider their interests, age, gender, and ethnicity and use friendly language. For example, when using a simulation that uses a sandwich metaphor for balancing chemical equations, you might ask, \"If you were talking to your friend, Rose, about making sandwiches, what would you tell her to do to figure out how many sandwiches she can make from 10 pieces of bread?\"" );
        StringUtils.deleteString( session, "teacherIdeas.guide.collaborativeActivities" );
        StringUtils.addString( session, "teacherIdeas.guide.collaborativeActivities", "The sims provide a common language and experience for students to collaboratively construct their understanding. Students can learn more when they <u>communicate their ideas</u> and reasoning to each other. Have students work in pairs or groups. Encourage students to share their ideas with their partner, working together to answer questions. Invite students to share ideas during whole class discussions." );
        StringUtils.deleteString( session, "teacherIdeas.guide.monitorUnderstanding" );
        StringUtils.addString( session, "teacherIdeas.guide.monitorUnderstanding", "Provide opportunities for students to check their own understanding. One way is to ask them to <u>predict</u> something based on their new knowledge and then <u>check</u> the prediction with the simulation." );

        StringUtils.addString( session, "teacherIdeas.guide.similarActivities", "Want to find activities that exemplify these strategies? Look for Gold Star activities when browsing activities at <a {0}>http://phet.colorado.edu/en/for-teachers/browse-activities</a>." );
        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
