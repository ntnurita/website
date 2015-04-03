/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation.entities;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.panels.simulation.SimulationMainPanel;
import edu.colorado.phet.website.translation.PhetPanelFactory;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

public class SimulationMainEntity extends TranslationEntity {

    private static final Logger logger = Logger.getLogger( SimulationMainEntity.class.getName() );

    public SimulationMainEntity() {
        addString( "simulationMainPanel.translatedVersions" );
        addString( "simulationMainPanel.screenshot.alt", "{0} will be replaced by the title of the simulation" );
        addString( "simulationMainPanel.version", "{0} will be replaced by the version number" );
        addString( "simulationMainPanel.runOffline" );
        addString( "simulationMainPanel.runOnline" );
        addString( "simulationMainPanel.runHTML" );
        addString( "simulationMainPanel.worksInBrowsersTablets" );
        addString( "simulationMainPanel.frequentlyAskedQuestions" );
        addString( "simulationMainPanel.simulationHasFAQ" );
        addString( "simulationMainPanel.tipsForTeachers" );
        addString( "simulationMainPanel.teachersGuide" );
        addString( "simulationMainPanel.teachingResources" );
        addString( "simulationMainPanel.topics" );
        addString( "simulationMainPanel.mainTopics" );
        addString( "simulationMainPanel.keywords" );
        addString( "simulationMainPanel.teachingIdeas" );
        addString( "simulationMainPanel.submitActivities" );
        addString( "simulationMainPanel.relatedSimulations" );
        addString( "simulationMainPanel.softwareRequirements" );
        addString( "simulationMainPanel.sampleLearningGoals" );
        addString( "simulationMainPanel.credits" );
        addString( "simulationMainPanel.language" );
        addString( "simulationMainPanel.languageTranslated" );
        addString( "simulationMainPanel.simulationTitleTranslated" );
        addString( "simulationMainPanel.designTeam" );
        addString( "simulationMainPanel.thirdPartyLibraries" );
        addString( "simulationMainPanel.thanksTo" );
        addString( "simulationMainPanel.untranslatedMessage" );

        addString( "simulationMainPanel.seeBelow" );
        addString( "simulationMainPanel.seeBelow.content" );
        addString( "simulationMainPanel.seeBelow.tipsForTeachers" );
        addString( "simulationMainPanel.seeBelow.teachingIdeas" );
        addString( "simulationMainPanel.seeBelow.relatedSimulations" );
        addString( "simulationMainPanel.seeBelow.softwareRequirements" );
        addString( "simulationMainPanel.seeBelow.translatedVersions" );
        addString( "simulationMainPanel.seeBelow.credits" );

        addString( "simulationMainPanel.supportPhetButton" );

        addString( "simulationMainPanel.embed" );
        addString( "embed.direct.title" );
        addString( "embed.direct.instructions" );
        addString( "embed.indirect.title" );
        addString( "embed.indirect.instructions" );
        addString( "embed.close" );
        addString( "embed.clickToLaunch", "This text is shown over an embedded screenshot" );

        addString( "simulationDisplay.indexView" );
        addString( "simulationDisplay.thumbnailView" );

        addString( "changelog.backToSimulation" );

        // html5 sim page redesign
        addString( "simulationMainPanel.about" );
        addString( "simulationMainPanel.topics" );
        addString( "simulationMainPanel.description" );
        addString( "simulationMainPanel.forTeachers" );
        addString( "simulationMainPanel.activities" );
        addString( "simulationMainPanel.translations" );
        addString( "simulationMainPanel.teacherTips" );
        addString( "simulationMainPanel.videoPrimer" );
        addString( "simulationMainPanel.signInPrompt" );
        addString( "simulationMainPanel.submitYourOwn" );
        addString( "simulationMainPanel.youCan" );
        addString( "simulationMainPanel.shareAnActivity" );
        addString( "simulationMainPanel.translateThisSim" );
        addString( "simulationMainPanel.allSimsIn" );
        addString( "simulationMainPanel.translatorInfo" );
        addString( "simulationMainPanel.downloadOrRun" );
        addString( "simulationMainPanel.noActivities" );
        addString( "simulationMainPanel.originalSim" );
        addString( "simulationMainPanel.backToHTML" );

        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {

                            Session session = requestCycle.getHibernateSession();
                            LocalizedSimulation simulation = null;
                            Transaction tx = null;
                            try {
                                tx = session.beginTransaction();

                                simulation = HibernateUtils.getExampleSimulation( session, context.getLocale() );

                                tx.commit();
                            }
                            catch ( RuntimeException e ) {
                                logger.warn( "Exception: " + e );
                                if ( tx != null && tx.isActive() ) {
                                    try {
                                        tx.rollback();
                                    }
                                    catch ( HibernateException e1 ) {
                                        logger.error( "ERROR: Error rolling back transaction", e1 );
                                    }
                                    throw e;
                                }
                            }

                            return new SimulationMainPanel( id, simulation, context );
                        }
                    }, "Main Simulation Page" );
    }

    public String getDisplayName() {
        return "Simulation";
    }
}
