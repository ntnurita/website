/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation.entities;

import edu.colorado.phet.website.content.ClassroomUsePanel;
import edu.colorado.phet.website.content.TeacherIdeasPanel;
import edu.colorado.phet.website.content.contribution.ContributionGuidelinesPanel;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.translation.PhetPanelFactory;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;

public class ForTeachersEntity extends TranslationEntity {
    public ForTeachersEntity() {
        addString( "teacherIdeas.title" );

        addString( "teacherIdeas.guide.title" );
        addString( "teacherIdeas.guide.subtitle" );
        addString( "teacherIdeas.guide.similarActivities" );
        addString( "teacherIdeas.guide.authors" );
        addString( "teacherIdeas.guide.date" );
        addString( "teacherIdeas.guide.integration" );
        addString( "teacherIdeas.guide.specificLearningGoals.title" );
        addString( "teacherIdeas.guide.specificLearningGoals" );
        addString( "teacherIdeas.guide.encourageReasoning.title" );
        addString( "teacherIdeas.guide.encourageReasoning" );
        addString( "teacherIdeas.guide.priorKnowledge.title" );
        addString( "teacherIdeas.guide.priorKnowledge" );
        addString( "teacherIdeas.guide.worldExperiences.title" );
        addString( "teacherIdeas.guide.worldExperiences" );
        addString( "teacherIdeas.guide.collaborativeActivities.title" );
        addString( "teacherIdeas.guide.collaborativeActivities" );
        addString( "teacherIdeas.guide.minimalDirections.title" );
        addString( "teacherIdeas.guide.minimalDirections" );
        addString( "teacherIdeas.guide.monitorUnderstanding.title" );
        addString( "teacherIdeas.guide.monitorUnderstanding" );

        addString( "forTeachers.workshopFacilitatorsGuide.title" );
        addString( "forTeachers.tipsForUsingPhet.title" );
        addString( "forTeachers.planningToUsePhet.title" );
        addString( "forTeachers.usingPhetInLecture.title" );
        addString( "forTeachers.lectureDemo.title" );
        addString( "forTeachers.clickersDemo.title" );
        addString( "forTeachers.activitesDesign.title" );
        addString( "forTeachers.facilitatingActivities.title" );
        addString( "forTeachers.virtualWorkshop.title" );

        addString( "for-teachers.classroom-use.title" );

        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                            return new TeacherIdeasPanel( id, context );
                        }
                    }, "For Teachers" );

        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                            return new ContributionGuidelinesPanel( id, context );
                        }
                    }, "Contribution guidelines" );

        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                            return new ClassroomUsePanel( id, context );
                        }
                    }, "Classroom Use" );

    }

    public String getDisplayName() {
        return "For Teachers";
    }
}
