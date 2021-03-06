/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.menu;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.common.phetcommon.util.PhetLocales;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.authentication.panels.ChangePasswordSuccessPanel;
import edu.colorado.phet.website.content.ClassroomUsePanel;
import edu.colorado.phet.website.content.DonatePanel;
import edu.colorado.phet.website.content.ForTranslatorsPanel;
import edu.colorado.phet.website.content.IndexPage;
import edu.colorado.phet.website.content.ResearchPanel;
import edu.colorado.phet.website.content.TeacherIdeasPanel;
import edu.colorado.phet.website.content.TranslationUtilityPanel;
import edu.colorado.phet.website.content.about.AboutContactPanel;
import edu.colorado.phet.website.content.about.AboutLegendPanel;
import edu.colorado.phet.website.content.about.AboutLicensingPanel;
import edu.colorado.phet.website.content.about.AboutMainPanel;
import edu.colorado.phet.website.content.about.AboutNewsPanel;
import edu.colorado.phet.website.content.about.AboutSourceCodePanel;
import edu.colorado.phet.website.content.about.AboutSponsorsPanel;
import edu.colorado.phet.website.content.about.AboutTeamPanel;
import edu.colorado.phet.website.content.about.AccessibilityPanel;
import edu.colorado.phet.website.content.about.HTMLLicensingPanel;
import edu.colorado.phet.website.content.contribution.ContributionBrowsePage;
import edu.colorado.phet.website.content.contribution.ContributionCreatePage;
import edu.colorado.phet.website.content.contribution.ContributionGuidelinesPanel;
import edu.colorado.phet.website.content.contribution.ContributionManagePage;
import edu.colorado.phet.website.content.forteachers.ActivitiesdesignPanel;
import edu.colorado.phet.website.content.forteachers.ClickersPanel;
import edu.colorado.phet.website.content.forteachers.FacilitatingActivitiesPanel;
import edu.colorado.phet.website.content.forteachers.LectureDemoPanel;
import edu.colorado.phet.website.content.forteachers.LectureOverviewPanel;
import edu.colorado.phet.website.content.forteachers.PlanningPanel;
import edu.colorado.phet.website.content.forteachers.TipsPanel;
import edu.colorado.phet.website.content.forteachers.VirtualWorkshopPanel;
import edu.colorado.phet.website.content.getphet.FullInstallPanel;
import edu.colorado.phet.website.content.getphet.OneAtATimePanel;
import edu.colorado.phet.website.content.getphet.RunOurSimulationsPanel;
import edu.colorado.phet.website.content.search.SearchResultsPage;
import edu.colorado.phet.website.content.simulations.CategoryPage;
import edu.colorado.phet.website.content.simulations.HTML5Page;
import edu.colorado.phet.website.content.simulations.TranslatedSimsPage;
import edu.colorado.phet.website.content.troubleshooting.GeneralFAQPanel;
import edu.colorado.phet.website.content.troubleshooting.JavaSecurity;
import edu.colorado.phet.website.content.troubleshooting.TroubleshootingMacPanel;
import edu.colorado.phet.website.content.troubleshooting.TroubleshootingMainPanel;
import edu.colorado.phet.website.content.workshops.ExampleWorkshopsPanel;
import edu.colorado.phet.website.content.troubleshooting.TroubleshootingMobilePanel;
import edu.colorado.phet.website.content.troubleshooting.TroubleshootingWindowsPanel;
import edu.colorado.phet.website.content.workshops.UgandaWorkshopPhotosPanel;
import edu.colorado.phet.website.content.workshops.UgandaWorkshopsPanel;
import edu.colorado.phet.website.content.workshops.WorkshopFacilitatorsGuidePanel;
import edu.colorado.phet.website.content.workshops.WorkshopsPanel;
import edu.colorado.phet.website.data.Category;
import edu.colorado.phet.website.data.util.AbstractCategoryListener;
import edu.colorado.phet.website.data.util.CategoryChangeHandler;
import edu.colorado.phet.website.test.FaqTestPage;
import edu.colorado.phet.website.translation.TranslationMainPage;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * Initializes and handles the navigation locations (NavLocation)s. Builds from the database during startup, and then
 * uses events to keep the locations synchronized with any possible category changes.
 */
public class NavMenu implements Serializable {

    /**
     * Map of nav location key => nav location, for fast lookup
     */
    private HashMap<String, NavLocation> cache = new HashMap<String, NavLocation>();

    /**
     * List of all nav locations
     */
    private List<NavLocation> locations = new LinkedList<NavLocation>();

    /**
     * Lists nav locations that correspond to simulation categories. For instance the location for "Physics" is in this,
     * but the location for "PhET Translation Utility" isn't.
     */
    private List<NavLocation> locationsBelowCategories = new LinkedList<NavLocation>();

    /**
     * The nav location shown as "Simulations". Essentially the root of categories.
     */
    private NavLocation simulations;

    private static final Logger logger = Logger.getLogger( NavMenu.class.getName() );

    // TODO: now that we have possible runtime modification of locations with changes of categories, something must be done about synchronization

    // TODO: add more docs

    public NavMenu() {
        NavLocation home = new NavLocation( null, "home", IndexPage.getLinker() );
        addMajorLocation( home );

        simulations = new NavLocation( null, "simulations", CategoryPage.getDefaultLinker() );
        addMajorLocation( simulations );

        NavLocation forTeachers = new NavLocation( null, "teacherIdeas", TeacherIdeasPanel.getLinker() ) {{
            // Nav Locations

            // Planning to Use PhET
            NavLocation planningPanel = new NavLocation( this, "forTeachers.planningToUsePhet", PlanningPanel.getLinker() );
            addLocation(planningPanel);

            // Using PhET in Lecture
            NavLocation usingPhetInLecturePanel = new NavLocation( this, "forTeachers.usingPhetInLecture", LectureOverviewPanel.getLinker() );
            addLocation(usingPhetInLecturePanel);

            // PhET as an (Interactive) Lecture Demonstration
            NavLocation lectureDemoPanel = new NavLocation( this, "forTeachers.lectureDemo", LectureDemoPanel.getLinker() );
            addLocation(lectureDemoPanel);

            // Using PhET with Clickers and Peer Instruction
            NavLocation clickersDemoPanel = new NavLocation( this, "forTeachers.clickersDemo", ClickersPanel.getLinker() );
            addLocation(clickersDemoPanel);

            // Designing Effective Activities for use in K12
            NavLocation activitiesDesignPanel = new NavLocation( this, "forTeachers.activitesDesign", ActivitiesdesignPanel.getLinker() );
            addLocation(activitiesDesignPanel);

            // Facilitating PhET Activities for the K12 Classroom
            NavLocation facilitatingActivitiesPanel = new NavLocation( this, "forTeachers.facilitatingActivities", FacilitatingActivitiesPanel.getLinker() );
            addLocation(facilitatingActivitiesPanel);

            // Take a Virtual PhET Workshop
            NavLocation virtualWorkshopPanel = new NavLocation( this, "forTeachers.virtualWorkshop", VirtualWorkshopPanel.getLinker() );
            addLocation(virtualWorkshopPanel);

            NavLocation exampleWorkshops = new NavLocation( this, "exampleworkshops", ExampleWorkshopsPanel.getLinker() );
            addLocation(exampleWorkshops);

            NavLocation teacherIdeasEdit = new NavLocation( this, "teacherIdeas.edit", ContributionBrowsePage.getLinker() );
            addLocation( teacherIdeasEdit );

            NavLocation teacherIdeasGuide = new NavLocation( this, "teacherIdeas.guide", ContributionGuidelinesPanel.getLinker() );
            addLocation( teacherIdeasGuide );

            NavLocation aboutLegend = new NavLocation( this, "about.legend", AboutLegendPanel.getLinker() );
            addLocation( aboutLegend );
//            addChild( aboutLegend );

            // orphan!
            NavLocation classroomUse = new NavLocation( this, "for-teachers.classroom-use", ClassroomUsePanel.getLinker() );
            addLocation( classroomUse );

            // Nav Locations with addChild - this determines order or links appearing in sidebar

            // Tips for Using PhET
            NavLocation tipsForUsingPhetPanel = new NavLocation( this, "forTeachers.tipsForUsingPhet", TipsPanel.getLinker() );
            addLocation( tipsForUsingPhetPanel );
            addChild( tipsForUsingPhetPanel );

            // Browse Activities
            NavLocation teacherIdeasBrowse = new NavLocation( this, "teacherIdeas.browse", ContributionBrowsePage.getLinker() );
            addLocation( teacherIdeasBrowse );
            addChild( teacherIdeasBrowse );

            // Submit an Activity
            NavLocation teacherIdeasSubmit = new NavLocation( this, "teacherIdeas.submit", ContributionCreatePage.getLinker() );
            addLocation( teacherIdeasSubmit );
            addChild( teacherIdeasSubmit );

            // My Activities
            NavLocation teacherIdeasManage = new NavLocation( this, "teacherIdeas.manage", ContributionManagePage.getLinker() );
            addLocation( teacherIdeasManage );
            addChild( teacherIdeasManage );

            // Workshops
            NavLocation workshops = new NavLocation( this, "workshops", WorkshopsPanel.getLinker() );
            addLocation( workshops );
            addChild( workshops );
        }};
        addMajorLocation( forTeachers );

        NavLocation getPhet = new NavLocation( null, "get-phet", RunOurSimulationsPanel.getLinker() );
        addMajorLocation( getPhet );

        NavLocation online = new NavLocation( getPhet, "get-phet.on-line", CategoryPage.getDefaultLinker() );
        addLocation( online );
        getPhet.addChild( online );

        NavLocation fullInstall = new NavLocation( getPhet, "get-phet.full-install", FullInstallPanel.getLinker() );
        addLocation( fullInstall );
        getPhet.addChild( fullInstall );

        NavLocation oneAtATime = new NavLocation( getPhet, "get-phet.one-at-a-time", OneAtATimePanel.getLinker() );
        addLocation( oneAtATime );
        getPhet.addChild( oneAtATime );

        NavLocation troubleshooting = new NavLocation( null, "troubleshooting.main", TroubleshootingMainPanel.getLinker() );
        addMajorLocation( troubleshooting );

        NavLocation troubleshootingMac = new NavLocation( troubleshooting, "troubleshooting.mac", TroubleshootingMacPanel.getLinker() );
        addLocation( troubleshootingMac );
        troubleshooting.addChild( troubleshootingMac );

        NavLocation troubleshootingWindows = new NavLocation( troubleshooting, "troubleshooting.windows", TroubleshootingWindowsPanel.getLinker() );
        addLocation( troubleshootingWindows );
        troubleshooting.addChild( troubleshootingWindows );

        NavLocation troubleshootingMobile = new NavLocation( troubleshooting, "troubleshooting.mobile", TroubleshootingMobilePanel.getLinker() );
        addLocation( troubleshootingMobile );
        troubleshooting.addChild( troubleshootingMobile );

        NavLocation faq = new NavLocation( null, "faqs", GeneralFAQPanel.getLinker() );
        faq.setUseNavigationStringForBreadcrumb( false ); // use nav.breadcrumb.faqs for breadcrumbs
        addMajorLocation( faq );

        NavLocation forTranslators = new NavLocation( null, "forTranslators", ForTranslatorsPanel.getLinker() );
        addMajorLocation( forTranslators );

        NavLocation translationUtility = new NavLocation( forTranslators, "forTranslators.translationUtility", TranslationUtilityPanel.getLinker() );
        addLocation( translationUtility );
        forTranslators.addChild( translationUtility );

        NavLocation translateWebsite = new NavLocation( forTranslators, "forTranslators.website", TranslationMainPage.getLinker() );
        addLocation( translateWebsite );
        forTranslators.addChild( translateWebsite );

        NavLocation donate = new NavLocation( null, "donate", DonatePanel.getLinker() );
        addMajorLocation( donate );

        NavLocation research = new NavLocation( null, "research", ResearchPanel.getLinker() );
        addMajorLocation( research );

        NavLocation about = new NavLocation( null, "about", AboutMainPanel.getLinker() );
        addMajorLocation( about );

        NavLocation aboutTeam = new NavLocation( about, "about.team", AboutTeamPanel.getLinker() );
        addLocation( aboutTeam );
        about.addChild( aboutTeam );
        
        NavLocation aboutSourceCode = new NavLocation( about, "about.source-code", AboutSourceCodePanel.getLinker() );
        addLocation( aboutSourceCode );
        about.addChild( aboutSourceCode );

        NavLocation aboutNews = new NavLocation( about, "about.news", AboutNewsPanel.getLinker() );
        addLocation( aboutNews );
        about.addChild( aboutNews );

        NavLocation aboutLicensing = new NavLocation( about, "about.licensing", AboutLicensingPanel.getLinker() );
        addLocation( aboutLicensing );
        about.addChild( aboutLicensing );

        NavLocation aboutContact = new NavLocation( about, "about.contact", AboutContactPanel.getLinker() );
        addLocation( aboutContact );
        about.addChild( aboutContact );

        NavLocation aboutSponsors = new NavLocation( about, "sponsors", AboutSponsorsPanel.getLinker() );
        addLocation( aboutSponsors );
        about.addChild( aboutSponsors );

        NavLocation htmlLicensing = new NavLocation( null, "html.licensing", HTMLLicensingPanel.getLinker() );
        addLocation( htmlLicensing );

        NavLocation accessibility = new NavLocation( null, "about.accessibility", AccessibilityPanel.getLinker() );
        addLocation( accessibility );

        // unconnected locations (TODO: doc how these work)

        NavLocation byKeyword = new NavLocation( null, "simulations.by-keyword", CategoryPage.getDefaultLinker() );
        addLocation( byKeyword );

        NavLocation searchResults = new NavLocation( null, "search.results", SearchResultsPage.getLinker( null ) );
        addLocation( searchResults );

        NavLocation uganda = new NavLocation( null, "workshops.uganda", UgandaWorkshopsPanel.getLinker() );
        addLocation( uganda );

        NavLocation ugandaPhotos = new NavLocation( null, "workshops.uganda-photos", UgandaWorkshopPhotosPanel.getLinker() );
        addLocation( ugandaPhotos );

        NavLocation changePasswordSuccess = new NavLocation( null, "changePasswordSuccess", ChangePasswordSuccessPanel.getLinker() );
        addLocation( changePasswordSuccess );

        NavLocation testFAQ = new NavLocation( null, "sim-faq-test", FaqTestPage.getLinker() );
        addLocation( testFAQ );

        NavLocation troubleshootingJavaSecurity = new NavLocation( null, "troubleshooting.javaSecurity", JavaSecurity.getLinker() );
        addLocation( troubleshootingJavaSecurity );

        Session session = HibernateUtils.getInstance().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Category root = (Category) session.createQuery( "select c from Category as c where c.root = true" ).uniqueResult();
            buildCategoryMenu( simulations, root );

            tx.commit();
        }
        catch ( RuntimeException e ) {
            logger.warn( e );
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
        session.close();

        CategoryChangeHandler.addListener( new AbstractCategoryListener() {
            @Override
            public void categoryAdded( Category cat ) {
                // we are assuming that only simulation categories are changed at runtime
                logger.info( "Category " + cat.getName() + " added, adding nav location" );
                NavLocation parentLocation = cat.getParent().getNavLocation( NavMenu.this );
                if ( parentLocation == null ) {
                    parentLocation = simulations;
                }
                NavLocation location = createSimLocation( parentLocation, cat.getName(), cat );
                parentLocation.addChild( location );
                if ( parentLocation == simulations ) {
                    simulations.organizeSimulationLocations();
                }
                addLocation( location );
            }

            @Override
            public void categoryRemoved( Category cat ) {
                logger.info( "Category " + cat.getName() + " removed, removing nav location" );
                NavLocation location = getLocationByKey( cat.getName() );
                if ( location != null ) {
                    removeLocation( location );
                }
                else {
                    logger.warn( "trying to remove nonexistant category?" );
                }
            }

            @Override
            public void categoryChildrenReordered( final Category cat ) {
                NavLocation location = getLocationByKey( cat.getName() );
                if ( location == null ) {
                    location = simulations;
                }
                final List<NavLocation> clocs = new LinkedList<NavLocation>();
                // TODO: warning, can this not be lazy?
                final NavLocation location1 = location;
                HibernateUtils.wrapTransaction( PhetRequestCycle.get().getHibernateSession(), new HibernateTask() {
                    public boolean run( Session session ) {
                        Category catty = (Category) session.load( Category.class, cat.getId() );
                        for ( Object o : catty.getSubcategories() ) {
                            Category subcat = (Category) o;
                            NavLocation chloc = getLocationByKey( subcat.getName() );
                            clocs.add( chloc );
                        }
                        Collections.sort( location1.getChildren(), new Comparator<NavLocation>() {
                            public int compare( NavLocation a, NavLocation b ) {
                                // slowsort
                                Integer ai = clocs.indexOf( a );
                                Integer bi = clocs.indexOf( b );
                                if ( ai == -1 ) { ai = 1000000; }
                                if ( bi == -1 ) { bi = 1000000; }
                                return ai.compareTo( bi );
                            }
                        } );
                        return true;
                    }
                } );

            }
        } );

    }

    private NavLocation createSimLocation( NavLocation parent, String name, final Category category ) {
        return new NavLocation( parent, name, CategoryPage.getLinker( category ) );
    }

    public void buildCategoryMenu( NavLocation location, Category category ) {
        for ( Object o : category.getSubcategories() ) {
            final Category subCategory = (Category) o;
            NavLocation subLocation = createSimLocation( location, subCategory.getName(), subCategory );
            addLocation( subLocation );
            location.addChild( subLocation );
            buildCategoryMenu( subLocation, subCategory );
        }
        if ( category.isRoot() ) {
            NavLocation allLocation = new NavLocation( location, "all", CategoryPage.getAllSimsLinker() );
            addLocation( allLocation );
            location.addChild( allLocation );
            locationsBelowCategories.add( allLocation );

            NavLocation translatedSimsLocation = new NavLocation( location, "simulations.translated", TranslatedSimsPage.getLinker() );
            addLocation( translatedSimsLocation );
            location.addChild( translatedSimsLocation );
            locationsBelowCategories.add( translatedSimsLocation );

            NavLocation html5SimsLocation = new NavLocation( location, "html", HTML5Page.getLinker() );
            addLocation( html5SimsLocation );

            PhetLocales phetLocales = PhetWicketApplication.get().getSupportedLocales();
            for ( String localeName : phetLocales.getSortedNames() ) {
                Locale locale = phetLocales.getLocale( localeName );
                NavLocation loc = new NavLocation( translatedSimsLocation, "language.names." + LocaleUtils.localeToString( locale ), TranslatedSimsPage.getLinker( locale ) );
                addLocation( loc );
                translatedSimsLocation.addChild( loc );

                // don't show all of the tons of language subcategories unless the one we want is selected
                loc.setHidden( true );
            }

        }
    }

    public void addMajorLocation( NavLocation location ) {
        locations.add( location );
        addLocation( location );
    }

    public void addLocation( NavLocation location ) {
        logger.debug( "adding location " + location.getKey() );
        cache.put( location.getKey(), location );
    }

    /**
     * Removes the location and all of its children
     *
     * @param location
     */
    public void removeLocation( NavLocation location ) {
        logger.debug( "removing location " + location.getKey() );
        locations.remove( location );
        cache.remove( location.getKey() );
        NavLocation parent = location.getParent();
        if ( parent != null ) {
            parent.removeChild( location );
        }
        for ( NavLocation child : location.getChildren() ) {
            removeLocation( child );
        }
    }

    public List<NavLocation> getLocations() {
        return locations;
    }

    public NavLocation getLocationByKey( String key ) {
        return cache.get( key );
    }

    public NavLocation getSimulationsLocation() {
        return simulations;
    }

    public List<NavLocation> getLocationsBelowCategories() {
        return locationsBelowCategories;
    }
}
