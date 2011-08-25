/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.panels.simulation;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.ResourceModel;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.event.PostCollectionUpdateEvent;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostUpdateEvent;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.borders.SmallOrangeButtonBorder;
import edu.colorado.phet.website.cache.EventDependency;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.components.RawBodyLabel;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.components.RawLink;
import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.content.DonatePanel;
import edu.colorado.phet.website.content.about.AboutLegendPanel;
import edu.colorado.phet.website.content.contribution.ContributionCreatePage;
import edu.colorado.phet.website.content.simulations.SimsByKeywordPage;
import edu.colorado.phet.website.content.simulations.SimulationChangelogPage;
import edu.colorado.phet.website.content.simulations.SimulationFAQPage;
import edu.colorado.phet.website.content.simulations.TranslatedSimsPage;
import edu.colorado.phet.website.data.Keyword;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Project;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.data.TeachersGuide;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.data.contribution.Contribution;
import edu.colorado.phet.website.data.util.AbstractChangeListener;
import edu.colorado.phet.website.data.util.HibernateEventListener;
import edu.colorado.phet.website.data.util.IChangeListener;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.panels.contribution.ContributionBrowsePanel;
import edu.colorado.phet.website.panels.sponsor.SimSponsorPanel;
import edu.colorado.phet.website.panels.sponsor.Sponsor;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.util.ClassAppender;
import edu.colorado.phet.website.util.HtmlUtils;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.SimpleTask;
import edu.colorado.phet.website.util.wicket.WicketUtils;

import static edu.colorado.phet.website.util.HtmlUtils.encode;

public class SimulationMainPanel extends PhetPanel {

    private String title;

    private boolean hasTeacherTips;

    private static final Logger logger = Logger.getLogger( SimulationMainPanel.class.getName() );

    public SimulationMainPanel( String id, final LocalizedSimulation simulation, final PageContext context ) {
        super( id, context );

        String simulationVersionString = simulation.getSimulation().getProject().getVersionString();

        add( new Label( "simulation-main-title", simulation.getTitle() ) );

        //add( HeaderContributor.forCss( CSS.SIMULATION_MAIN ) );

        if ( simulation.getLocale().equals( context.getLocale() ) ) {
            add( new InvisibleComponent( "untranslated-sim-text" ) );
        }
        else {
            add( new LocalizedText( "untranslated-sim-text", "simulationMainPanel.untranslatedMessage" ) );
        }

        RawLink link = new RawLink( "simulation-main-link-run-main", simulation.getRunUrl() );
        link.add( new StaticImage( "simulation-main-screenshot", simulation.getSimulation().getImage(), StringUtils.messageFormat( getPhetLocalizer().getString( "simulationMainPanel.screenshot.alt", this ), new Object[] {
                encode( simulation.getTitle() )
        } ) ) );
        add( link );

        //add( new Label( "simulation-main-description", simulation.getDescription() ) );
        add( new LocalizedText( "simulation-main-description", simulation.getSimulation().getDescriptionKey() ) );
        add( new LocalizedText( "simulationMainPanel.version", "simulationMainPanel.version", new Object[] {
                HtmlUtils.encode( simulationVersionString ),
                SimulationChangelogPage.getLinker( simulation ).getHref( context, getPhetCycle() )
        } ) );
        add( new LocalizedText( "simulationMainPanel.kilobytes", "simulationMainPanel.kilobytes", new Object[] {
                simulation.getSimulation().getKilobytes()
        } ) );

        SmallOrangeButtonBorder orangeButton = new SmallOrangeButtonBorder( "orange-button", context );
        add( orangeButton );
        orangeButton.add( DonatePanel.getLinker().getLink( "support-link", context, getPhetCycle() ) );

        /*---------------------------------------------------------------------------*
        * rating icons
        *----------------------------------------------------------------------------*/

        if ( simulation.getSimulation().isUnderConstruction() ) {
            Link uclink = AboutLegendPanel.getLinker().getLink( "rating-under-construction-link", context, getPhetCycle() );
            uclink.add( new StaticImage( "rating-under-construction-image", Images.UNDER_CONSTRUCTION_SMALL, getPhetLocalizer().getString( "tooltip.legend.underConstruction", this ) ) );
            add( uclink );
        }
        else {
            add( new InvisibleComponent( "rating-under-construction-link" ) );
        }

        if ( simulation.getSimulation().isGuidanceRecommended() ) {
            Link uclink = AboutLegendPanel.getLinker().getLink( "rating-guidance-recommended-link", context, getPhetCycle() );
            uclink.add( new StaticImage( "rating-guidance-recommended-image", Images.GUIDANCE_RECOMMENDED_SMALL, getPhetLocalizer().getString( "tooltip.legend.guidanceRecommended", this ) ) );
            add( uclink );
        }
        else {
            add( new InvisibleComponent( "rating-guidance-recommended-link" ) );
        }

        if ( simulation.getSimulation().isClassroomTested() ) {
            Link uclink = AboutLegendPanel.getLinker().getLink( "rating-classroom-tested-link", context, getPhetCycle() );
            uclink.add( new StaticImage( "rating-classroom-tested-image", Images.CLASSROOM_TESTED_SMALL, getPhetLocalizer().getString( "tooltip.legend.classroomTested", this ) ) );
            add( uclink );
        }
        else {
            add( new InvisibleComponent( "rating-classroom-tested-link" ) );
        }

        /*---------------------------------------------------------------------------*
        * teacher's guide
        *----------------------------------------------------------------------------*/

        final List<TeachersGuide> guides = new LinkedList<TeachersGuide>();
        HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
            public boolean run( Session session ) {
                List li = session.createQuery( "select tg from TeachersGuide as tg where tg.simulation = :sim" )
                        .setEntity( "sim", simulation.getSimulation() ).list();
                if ( !li.isEmpty() ) {
                    guides.add( (TeachersGuide) li.get( 0 ) );
                }
                return true;
            }
        } );
        if ( !guides.isEmpty() ) {
            add( new LocalizedText( "guide-text", "simulationMainPanel.teachersGuide", new Object[] {
                    guides.get( 0 ).getLinker().getHref( context, getPhetCycle() )
            } ) );
//            Label visLabel = new Label( "tips-for-teachers-visible", "" );
//            visLabel.setRenderBodyOnly( true ); // don't make anything appear
//            add( visLabel );
            hasTeacherTips = true;
        }
        else {
            // make the teachers guide text (and whole section) invisible
            add( new InvisibleComponent( "guide-text" ) );
//            add( new InvisibleComponent( "tips-for-teachers-visible" ) );

            hasTeacherTips = false;
        }

        /*---------------------------------------------------------------------------*
        * contributions
        *----------------------------------------------------------------------------*/

        if ( DistributionHandler.displayContributions( getPhetCycle() ) ) {
            final List<Contribution> contributions = new LinkedList<Contribution>();
            HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                public boolean run( Session session ) {
                    List list = session.createQuery( "select c from Contribution as c where :simulation member of c.simulations and c.approved = true" )
                            .setEntity( "simulation", simulation.getSimulation() ).list();
                    for ( Object o : list ) {
                        Contribution contribution = (Contribution) o;
                        contributions.add( contribution );

                        // we need to read levels
                        contribution.getLevels();

                        // we also need to read the types
                        contribution.getTypes();

                        for ( Object x : contribution.getSimulations() ) {
                            Simulation sim = (Simulation) x;

                            // we need to be able to read these to determine the localized simulation title later
                            sim.getLocalizedSimulations();
                        }
                    }
                    return true;
                }
            } );
            add( new ContributionBrowsePanel( "contributions-panel", context, contributions, false ) );
//            Label visLabel = new Label( "teacher-ideas-visible", "" );
//            visLabel.setRenderBodyOnly( true ); // don't make anything appear
//            add( visLabel );
        }
        else {
            add( new InvisibleComponent( "contributions-panel" ) );
//            add( new InvisibleComponent( "teacher-ideas-visible" ) );
        }

        /*---------------------------------------------------------------------------*
        * translations
        *----------------------------------------------------------------------------*/

        List<LocalizedSimulation> simulations = HibernateUtils.getLocalizedSimulationsMatching( getHibernateSession(), null, simulation.getSimulation().getName(), null );
        HibernateUtils.orderSimulations( simulations, context.getLocale() );

        List<LocalizedSimulation> otherLocalizedSimulations = new LinkedList<LocalizedSimulation>();

        // TODO: improve model?
        for ( final LocalizedSimulation sim : simulations ) {
            if ( !sim.getLocale().equals( simulation.getLocale() ) ) {
                otherLocalizedSimulations.add( sim );
            }
        }

        // TODO: allow localization of locale display names
        ListView simulationList = new ListView<LocalizedSimulation>( "simulation-main-translation-list", otherLocalizedSimulations ) {
            protected void populateItem( ListItem<LocalizedSimulation> item ) {
                LocalizedSimulation simulation = item.getModelObject();
                Locale simLocale = simulation.getLocale();
                RawLink runLink = new RawLink( "simulation-main-translation-link", simulation.getRunUrl() );
                RawLink downloadLink = new RawLink( "simulation-main-translation-download", simulation.getDownloadUrl() );
                String defaultLanguageName = simLocale.getDisplayName( context.getLocale() );
                String languageName = ( (PhetLocalizer) getLocalizer() ).getString( "language.names." + LocaleUtils.localeToString( simLocale ), this, null, defaultLanguageName, false );
                item.add( runLink );
                if ( DistributionHandler.displayJARLink( getPhetCycle(), simulation ) ) {
                    item.add( downloadLink );
                }
                else {
                    item.add( new InvisibleComponent( "simulation-main-translation-download" ) );
                }
                item.add( new Label( "simulation-main-translation-title", simulation.getTitle() ) );
                Link lang1 = TranslatedSimsPage.getLinker( simLocale ).getLink( "language-link-1", context, getPhetCycle() );
                item.add( lang1 );
                Link lang2 = TranslatedSimsPage.getLinker( simLocale ).getLink( "language-link-2", context, getPhetCycle() );
                item.add( lang2 );
                lang1.add( new Label( "simulation-main-translation-locale-name", languageName ) );
                lang2.add( new Label( "simulation-main-translation-locale-translated-name", simLocale.getDisplayName( simLocale ) ) );

                WicketUtils.highlightListItem( item );
            }
        };
        add( simulationList );

        /*---------------------------------------------------------------------------*
        * run / download links
        *----------------------------------------------------------------------------*/

        // TODO: move from direct links to page redirections, so bookmarkables will be minimized
        RawLink runOnlineLink = new RawLink( "run-online-link", simulation.getRunUrl() );
        add( runOnlineLink );
        if ( simulation.getSimulation().getProject().isFlash() ) {
            // make Flash links open in a new window / tab
            runOnlineLink.add( new ClassAppender( "external" ) );
        }
        add( new RawLink( "run-offline-link", simulation.getDownloadUrl() ) );

        if ( getPhetCycle().isInstaller() ) {
            add( new InvisibleComponent( "embed-button" ) );
        }
        else {
            add( new WebMarkupContainer( "embed-button" ) );
        }

        final String directEmbedText = simulation.getDirectEmbeddingSnippet();
        String indirectEmbedText = simulation.getClickToLaunchSnippet( getPhetLocalizer().getString( "embed.clickToLaunch", this ) );
        if ( directEmbedText != null ) {
            add( new Label( "direct-embed-text", directEmbedText ) );
        }
        else {
            add( new InvisibleComponent( "direct-embed-text" ) );
        }
        add( new Label( "indirect-embed-text", indirectEmbedText ) {{
            if ( directEmbedText == null ) {
                // if we can't directly embed, set our markup ID so that this text is automatically selected
                setMarkupId( "embeddable-text" );
                setOutputMarkupId( true );
            }
        }} );

        /*---------------------------------------------------------------------------*
        * keywords / topics
        *----------------------------------------------------------------------------*/

        List<Keyword> keywords = new LinkedList<Keyword>();
        List<Keyword> topics = new LinkedList<Keyword>();

        Transaction tx = null;
        try {
            Session session = getHibernateSession();
            tx = session.beginTransaction();

            Simulation sim = (Simulation) session.load( Simulation.class, simulation.getSimulation().getId() );
            //System.out.println( "Simulation keywords for " + sim.getName() );
            for ( Object o : sim.getKeywords() ) {
                Keyword keyword = (Keyword) o;
                keywords.add( keyword );
                //System.out.println( keyword.getKey() );
            }
            for ( Object o : sim.getTopics() ) {
                Keyword keyword = (Keyword) o;
                topics.add( keyword );
                //System.out.println( keyword.getKey() );
            }

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

        ListView topicList = new ListView<Keyword>( "topic-list", topics ) {
            protected void populateItem( ListItem<Keyword> item ) {
                Keyword keyword = item.getModelObject();
                item.add( new RawLabel( "topic-label", new ResourceModel( keyword.getKey() ) ) );
            }
        };
        add( topicList );
        if ( topics.isEmpty() ) {
            topicList.setVisible( false );
        }

        ListView keywordList = new ListView<Keyword>( "keyword-list", keywords ) {
            protected void populateItem( ListItem<Keyword> item ) {
                Keyword keyword = item.getModelObject();
                Link link = SimsByKeywordPage.getLinker( keyword.getSubKey() ).getLink( "keyword-link", context, getPhetCycle() );
//                Link link = new StatelessLink( "keyword-link" ) {
//                    public void onClick() {
//                        // TODO: fill in keyword links!
//                    }
//                };
                link.add( new RawLabel( "keyword-label", new ResourceModel( keyword.getKey() ) ) );
                item.add( link );
            }
        };
        add( keywordList );
        if ( keywords.isEmpty() ) {
            keywordList.setVisible( false );
        }

        /*---------------------------------------------------------------------------*
        * system requirements
        *----------------------------------------------------------------------------*/

        if ( simulation.getSimulation().isJava() ) {
            add( new Label( "windows-req", "Sun Java 1.5.0_15 or later" ) );
            add( new Label( "mac-req", "Sun Java 1.5.0_19 or later" ) );
            add( new Label( "linux-req", "Sun Java 1.5.0_15 or later" ) );
        }
        else if ( simulation.getSimulation().isFlash() ) {
            add( new Label( "windows-req", "Macromedia Flash 9 or later" ) );
            add( new Label( "mac-req", "Macromedia Flash 9 or later" ) );
            add( new Label( "linux-req", "Macromedia Flash 9 or later" ) );
        }
        else {
            add( new InvisibleComponent( "windows-req" ) );
            add( new InvisibleComponent( "mac-req" ) );
            add( new InvisibleComponent( "linux-req" ) );
        }

        // so we don't emit an empty <table></table> that isn't XHTML Strict compatible
        if ( otherLocalizedSimulations.isEmpty() ) {
            simulationList.setVisible( false );
        }

        PhetLocalizer localizer = (PhetLocalizer) getLocalizer();

        /*---------------------------------------------------------------------------*
        * title
        *----------------------------------------------------------------------------*/

        // we initialize the title in the panel. then whatever page that wants to adopt this panel's "title" as the page
        // title can
        List<String> titleParams = new LinkedList<String>();
        titleParams.add( simulation.getEncodedTitle() );
        for ( Keyword keyword : keywords ) {
            titleParams.add( localizer.getString( keyword.getKey(), this ) );
        }

        if ( keywords.size() < 3 ) {
            title = simulation.getEncodedTitle();
        }
        else {
            try {
                title = StringUtils.messageFormat( localizer.getString( "simulationPage.title", this ), titleParams.toArray() );
            }
            catch ( RuntimeException e ) {
                e.printStackTrace();
                title = simulation.getEncodedTitle();
            }
        }

        addCacheParameter( "title", title );

        /*---------------------------------------------------------------------------*
        * related simulations
        *----------------------------------------------------------------------------*/

        List<LocalizedSimulation> relatedSimulations = getRelatedSimulations( simulation );
        if ( relatedSimulations.isEmpty() ) {
            add( new InvisibleComponent( "related-simulations-panel" ) );
            add( new InvisibleComponent( "related-simulations-visible" ) );
        }
        else {
            add( new SimulationDisplayPanel( "related-simulations-panel", context, relatedSimulations ) );
            add( new RawBodyLabel( "related-simulations-visible", "" ) ); // visible but shows nothing, so the related simulations "see below" shows up
        }

        /*---------------------------------------------------------------------------*
        * more info (design team, libraries, thanks, etc
        *----------------------------------------------------------------------------*/

        List<String> designTeam = new LinkedList<String>();
        List<String> libraries = new LinkedList<String>();
        List<String> thanks = new LinkedList<String>();
        List<String> learningGoals = new LinkedList<String>();

        String rawDesignTeam = simulation.getSimulation().getDesignTeam();
        if ( rawDesignTeam != null ) {
            for ( String item : rawDesignTeam.split( "<br/>" ) ) {
                if ( item != null && item.length() > 0 ) {
                    designTeam.add( item );
                }
            }
        }

        String rawLibraries = simulation.getSimulation().getLibraries();
        if ( rawLibraries != null ) {
            for ( String item : rawLibraries.split( "<br/>" ) ) {
                if ( item != null && item.length() > 0 ) {
                    libraries.add( item );
                }
            }
        }

        String rawThanks = simulation.getSimulation().getThanksTo();
        if ( rawThanks != null ) {
            for ( String item : rawThanks.split( "<br/>" ) ) {
                if ( item != null && item.length() > 0 ) {
                    thanks.add( item );
                }
            }
        }

        String rawLearningGoals = getLocalizer().getString( simulation.getSimulation().getLearningGoalsKey(), this );
        if ( rawLearningGoals != null ) {
            for ( String item : rawLearningGoals.split( "<br/>" ) ) {
                if ( item != null && item.length() > 0 ) {
                    learningGoals.add( item );
                }
            }
        }

        ListView designView = new ListView<String>( "design-list", designTeam ) {
            protected void populateItem( ListItem<String> item ) {
                String str = item.getModelObject();
                item.add( new Label( "design-item", str ) );
            }
        };
        if ( designTeam.isEmpty() ) {
            designView.setVisible( false );
        }
        add( designView );

        ListView libraryView = new ListView<String>( "library-list", libraries ) {
            protected void populateItem( ListItem<String> item ) {
                String str = item.getModelObject();
                item.add( new Label( "library-item", str ) );
            }
        };
        if ( libraries.isEmpty() ) {
            libraryView.setVisible( false );
        }
        add( libraryView );

        ListView thanksView = new ListView<String>( "thanks-list", thanks ) {
            protected void populateItem( ListItem<String> item ) {
                String str = item.getModelObject();
                item.add( new Label( "thanks-item", str ) );
            }
        };
        if ( thanks.isEmpty() ) {
            thanksView.setVisible( false );
        }
        add( thanksView );

        // TODO: consolidate common behavior for these lists
        ListView learningGoalsView = new ListView<String>( "learning-goals", learningGoals ) {
            protected void populateItem( ListItem<String> item ) {
                String str = item.getModelObject();
                item.add( new RawLabel( "goal", str ) );
            }
        };
        if ( learningGoals.isEmpty() ) {
            learningGoalsView.setVisible( false );
        }
        add( learningGoalsView );

        addDependency( new EventDependency() {

            private IChangeListener projectListener;
            private IChangeListener stringListener;
            private IChangeListener teacherGuideListener;

            @Override
            protected void addListeners() {
                projectListener = new AbstractChangeListener() {
                    public void onUpdate( Object object, PostUpdateEvent event ) {
                        if ( HibernateEventListener.getSafeHasChanged( event, "visible" ) ) {
                            invalidate();
                        }
                    }
                };
                teacherGuideListener = new AbstractChangeListener() {
                    @Override
                    public void onInsert( Object object, PostInsertEvent event ) {
                        TeachersGuide guide = (TeachersGuide) object;
                        if ( guide.getSimulation().getId() == simulation.getSimulation().getId() ) {
                            invalidate();
                        }
                    }

                    @Override
                    public void onUpdate( Object object, PostUpdateEvent event ) {
                        TeachersGuide guide = (TeachersGuide) object;
                        if ( guide.getSimulation().getId() == simulation.getSimulation().getId() ) {
                            invalidate();
                        }
                    }

                    @Override
                    public void onCollectionUpdate( Object object, PostCollectionUpdateEvent event ) {
                        TeachersGuide guide = (TeachersGuide) object;
                        if ( guide.getSimulation().getId() == simulation.getSimulation().getId() ) {
                            invalidate();
                        }
                    }

                    @Override
                    public void onDelete( Object object, PostDeleteEvent event ) {
                        TeachersGuide guide = (TeachersGuide) object;
                        if ( guide.getSimulation().getId() == simulation.getSimulation().getId() ) {
                            invalidate();
                        }
                    }
                };
                stringListener = createTranslationChangeInvalidator( context.getLocale() );
                HibernateEventListener.addListener( Project.class, projectListener );
                HibernateEventListener.addListener( TranslatedString.class, stringListener );
                HibernateEventListener.addListener( Simulation.class, getAnyChangeInvalidator() );
                HibernateEventListener.addListener( LocalizedSimulation.class, getAnyChangeInvalidator() );
            }

            @Override
            protected void removeListeners() {
                HibernateEventListener.removeListener( Project.class, projectListener );
                HibernateEventListener.removeListener( TranslatedString.class, stringListener );
                HibernateEventListener.removeListener( Simulation.class, getAnyChangeInvalidator() );
                HibernateEventListener.removeListener( LocalizedSimulation.class, getAnyChangeInvalidator() );
            }
        } );

        if ( DistributionHandler.showSimSponsor( getPhetCycle() ) ) {
            // this gets cached, so it will stay the same for the sim (but will be different for different sims)
            add( new SimSponsorPanel( "pearson-sponsor", context, Sponsor.chooseRandomSimSponsor() ) );
        }
        else {
            add( new InvisibleComponent( "pearson-sponsor" ) );
        }

        if ( getPhetCycle().isInstaller() ) {
            add( new WebMarkupContainer( "sim-sponsor-installer-js" ) );
        }
        else {
            add( new InvisibleComponent( "sim-sponsor-installer-js" ) );
        }

        add( new LocalizedText( "submit-a", "simulationMainPanel.submitActivities", new Object[] {
                ContributionCreatePage.getLinker().getHref( context, getPhetCycle() )
        } ) );

        /*---------------------------------------------------------------------------*
        * FAQ
        *----------------------------------------------------------------------------*/

        if ( simulation.getSimulation().isFaqVisible() && simulation.getSimulation().getFaqList() != null ) {
            add( new LocalizedText( "faq-text", "simulationMainPanel.simulationHasFAQ", new Object[] {
                    SimulationFAQPage.getLinker( simulation ).getHref( context, getPhetCycle() ),
                    simulation.getSimulation().getFaqList().getPDFLinker( getMyLocale() ).getHref( context, getPhetCycle() )
            } ) );
        }
        else {
            add( new InvisibleComponent( "faq-text" ) );
        }
    }

    public List<LocalizedSimulation> getRelatedSimulations( final LocalizedSimulation simulation ) {
        final List<LocalizedSimulation> ret = new LinkedList<LocalizedSimulation>();
        HibernateUtils.wrapCatchTransaction( getHibernateSession(), new SimpleTask() {
            public void run( Session session ) {
                LocalizedSimulation lsim = (LocalizedSimulation) session.load( LocalizedSimulation.class, simulation.getId() );
                for ( Object o : lsim.getSimulation().getRelatedSimulations() ) {
                    Simulation related = (Simulation) o;
                    ret.add( related.getBestLocalizedSimulation( getMyLocale() ) );
                }
            }
        } );
        return ret;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getStyle( String key ) {
        if ( key.equals( "style.simulation-main-panel-content-header-clear" ) ) {
            // IE6 fix so we can clear only when the tips are available
            if ( hasTeacherTips ) {
                return "simulation-main-panel-content-header-clear";
            }
            else {
                return "simulation-main-panel-content-header";
            }
        }
        return super.getStyle( key );
    }
}