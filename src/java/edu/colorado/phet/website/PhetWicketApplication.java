/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.LogManager;

import org.apache.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.request.target.coding.HybridUrlCodingStrategy;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.colorado.phet.buildtools.BuildLocalProperties;
import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.common.phetcommon.util.PhetLocales;
import edu.colorado.phet.website.admin.AdminMainPage;
import edu.colorado.phet.website.admin.AdminNewInstallerPage;
import edu.colorado.phet.website.admin.deploy.DeployProjectPage;
import edu.colorado.phet.website.admin.deploy.DeployResourcePage;
import edu.colorado.phet.website.admin.deploy.DeployTranslationPage;
import edu.colorado.phet.website.admin.doc.TechnicalDocPage;
import edu.colorado.phet.website.admin.doc.TranslationDocPage;
import edu.colorado.phet.website.authentication.ChangePasswordPage;
import edu.colorado.phet.website.authentication.EditProfilePage;
import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.authentication.RegisterPage;
import edu.colorado.phet.website.authentication.ResetPasswordCallbackPage;
import edu.colorado.phet.website.authentication.ResetPasswordRequestPage;
import edu.colorado.phet.website.authentication.ResetPasswordRequestSuccessPage;
import edu.colorado.phet.website.authentication.SignInPage;
import edu.colorado.phet.website.authentication.SignOutPage;
import edu.colorado.phet.website.authentication.panels.ChangePasswordSuccessPanel;
import edu.colorado.phet.website.cache.InstallerCache;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.content.BlankPage;
import edu.colorado.phet.website.content.ClassroomUsePanel;
import edu.colorado.phet.website.content.DonatePanel;
import edu.colorado.phet.website.content.ErrorPage;
import edu.colorado.phet.website.content.ForTranslatorsPanel;
import edu.colorado.phet.website.content.IndexPage;
import edu.colorado.phet.website.content.NotFoundPage;
import edu.colorado.phet.website.content.ResearchPanel;
import edu.colorado.phet.website.content.RobotsTxtPage;
import edu.colorado.phet.website.content.SessionExpiredPage;
import edu.colorado.phet.website.content.SponsorListPage;
import edu.colorado.phet.website.content.TeachWithPhetDemoPage;
import edu.colorado.phet.website.content.TeacherIdeasPanel;
import edu.colorado.phet.website.content.TheFutureOfPhet;
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
import edu.colorado.phet.website.content.contribution.AddContributionCommentPage;
import edu.colorado.phet.website.content.contribution.ContributionBrowsePage;
import edu.colorado.phet.website.content.contribution.ContributionCommentSuccessPage;
import edu.colorado.phet.website.content.contribution.ContributionCreatePage;
import edu.colorado.phet.website.content.contribution.ContributionEditPage;
import edu.colorado.phet.website.content.contribution.ContributionGuidelinesPanel;
import edu.colorado.phet.website.content.contribution.ContributionManagePage;
import edu.colorado.phet.website.content.contribution.ContributionNominationSuccessPage;
import edu.colorado.phet.website.content.contribution.ContributionPage;
import edu.colorado.phet.website.content.contribution.ContributionSuccessPage;
import edu.colorado.phet.website.content.contribution.NominateContributionPage;
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
import edu.colorado.phet.website.content.media.MediaImagesPage;
import edu.colorado.phet.website.content.media.TechAwardPage;
import edu.colorado.phet.website.content.media.WorldPhotosPage;
import edu.colorado.phet.website.content.search.SearchResultsPage;
import edu.colorado.phet.website.content.simulations.ByDevicePage;
import edu.colorado.phet.website.content.simulations.ByGradeLevelPage;
import edu.colorado.phet.website.content.simulations.CategoryPage;
import edu.colorado.phet.website.content.simulations.HTML5Page;
import edu.colorado.phet.website.content.simulations.LegacySimulationPage;
import edu.colorado.phet.website.content.simulations.SimsByKeywordPage;
import edu.colorado.phet.website.content.simulations.SimulationChangelogPage;
import edu.colorado.phet.website.content.simulations.SimulationFAQPage;
import edu.colorado.phet.website.content.simulations.SimulationPage;
import edu.colorado.phet.website.content.simulations.TranslatedSimsPage;
import edu.colorado.phet.website.content.troubleshooting.GeneralFAQPanel;
import edu.colorado.phet.website.content.troubleshooting.JavaSecurity;
import edu.colorado.phet.website.content.troubleshooting.TroubleshootingMacPanel;
import edu.colorado.phet.website.content.troubleshooting.TroubleshootingMainPanel;
import edu.colorado.phet.website.content.troubleshooting.TroubleshootingMobilePanel;
import edu.colorado.phet.website.content.troubleshooting.TroubleshootingWindowsPanel;
import edu.colorado.phet.website.content.workshops.ExampleWorkshopsPanel;
import edu.colorado.phet.website.content.workshops.UgandaWorkshopPhotosPanel;
import edu.colorado.phet.website.content.workshops.UgandaWorkshopsPanel;
import edu.colorado.phet.website.content.workshops.WorkshopFacilitatorsGuidePanel;
import edu.colorado.phet.website.content.workshops.WorkshopsPanel;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.menu.NavMenu;
import edu.colorado.phet.website.metadata.LRETerm;
import edu.colorado.phet.website.metadata.MetadataDump;
import edu.colorado.phet.website.newsletter.ConfirmEmailLandingPage;
import edu.colorado.phet.website.newsletter.InitialSubscribePage;
import edu.colorado.phet.website.newsletter.UnsubscribeLandingPage;
import edu.colorado.phet.website.notification.NotificationHandler;
import edu.colorado.phet.website.services.Autocomplete;
import edu.colorado.phet.website.services.CheckLogin;
import edu.colorado.phet.website.services.EnglishSummaryExportCSV;
import edu.colorado.phet.website.services.HealthCheck;
import edu.colorado.phet.website.services.IQityExportCSV;
import edu.colorado.phet.website.services.LearningComExport;
import edu.colorado.phet.website.services.NCInformationPage;
import edu.colorado.phet.website.services.PhetInfoServicePage;
import edu.colorado.phet.website.services.ProjectSortedSimulations;
import edu.colorado.phet.website.services.SchedulerService;
import edu.colorado.phet.website.services.SimJarRedirectPage;
import edu.colorado.phet.website.services.SimulationMetadataFormatService;
import edu.colorado.phet.website.services.SimulationMetadataFormatServiceWithoutDeclaration;
import edu.colorado.phet.website.services.StringGetter;
import edu.colorado.phet.website.services.TranslationList;
import edu.colorado.phet.website.templates.StaticPage;
import edu.colorado.phet.website.test.FaqTestPage;
import edu.colorado.phet.website.test.NestedFormTest;
import edu.colorado.phet.website.test.PublicChangelogTest;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.translation.TranslationMainPage;
import edu.colorado.phet.website.translation.TranslationUrlStrategy;
import edu.colorado.phet.website.util.PhetCycleProcessor;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.PhetUrlStrategy;
import edu.colorado.phet.website.util.SearchUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.webgl.WebglDisabledPage;
import edu.colorado.phet.website.webgl.WebglRedirectPage;

/**
 * Main entry and configuration point for the Wicket-based PhET website.
 * Initializes pages (and the mappings), along with many other things.
 */
public class PhetWicketApplication extends WebApplication {

    private PhetUrlMapper mapper;
    private NavMenu menu;
    private WebsiteProperties websiteProperties;
    private static PhetWicketApplication instance = null;

    // TODO: flesh out and improve thread-safeness of translations part
    private List<Translation> translations = new LinkedList<Translation>(); // NOTE:
    // seems
    // to
    // be
    // NON-ENGLISH
    // translations
    // TODO
    // make
    // this
    // include
    // English,
    // and
    // handle
    // correctly

    private static final Logger logger = Logger
            .getLogger( PhetWicketApplication.class.getName() );

    /**
     * We have a list of data server DNS names that are essentially aliases to
     * phetsims. These should be used when a large number of separate files need
     * to be downloaded (like sim thumbnails) for speed increases.
     */
    public static final String[] DATA_SERVERS = new String[]{
            "phet-data1.colorado.edu", "phet-data2.colorado.edu"
            // "phet-data3.colorado.edu", // use these later if we determine that it
            // helps performance. DNS was hitting us too hard
            // "phet-data4.colorado.edu"
    };

    /**
     * Wicket likes to hardwire the home page in
     *
     * @return The home page class
     */
    public Class getHomePage() {
        return IndexPage.class;
    }

    @Override
    protected void init() {
        super.init();

        synchronized( PhetWicketApplication.class ) {
            instance = this;
        }

        // move JUL logging statements to slf4j
        setupJulSfl4j();

        if ( getConfigurationType().equals( Application.DEPLOYMENT ) ) {
            // Logger.getLogger( "edu.colorado.phet.website" ).setLevel(
            // Level.WARN );
        }
        // do not override development. instead use log4j.properties debugging
        // level. -JO
        // else {
        // Logger.getLogger( "edu.colorado.phet.website" ).setLevel( Level.INFO
        // );
        // }

        websiteProperties = new WebsiteProperties( getServletContext() );

        // set up error pages
        getApplicationSettings().setPageExpiredErrorPage(
                SessionExpiredPage.class );
        getApplicationSettings().setAccessDeniedPage( ErrorPage.class );
        getApplicationSettings().setInternalErrorPage( ErrorPage.class );

        // add static pages, that are accessed through reflection. this is used
        // so that separate page AND panel classes
        // are not needed for each visual page.
        // NOTE: do this before adding StaticPage into the mapper. Checked, and
        // violation will result in a fatal error.
        StaticPage.addPanel( TroubleshootingMainPanel.class );
        StaticPage.addPanel( GeneralFAQPanel.class );
        StaticPage.addPanel( AboutMainPanel.class );
        StaticPage.addPanel( WorkshopsPanel.class );
        StaticPage.addPanel( WorkshopFacilitatorsGuidePanel.class );

        StaticPage.addPanel( ActivitiesdesignPanel.class );
        StaticPage.addPanel( FacilitatingActivitiesPanel.class );
        StaticPage.addPanel( ExampleWorkshopsPanel.class );
        StaticPage.addPanel( ClickersPanel.class );
        StaticPage.addPanel( LectureDemoPanel.class );
        StaticPage.addPanel( LectureOverviewPanel.class );
        StaticPage.addPanel( PlanningPanel.class );
        StaticPage.addPanel( TipsPanel.class );
        StaticPage.addPanel( VirtualWorkshopPanel.class );

        StaticPage.addPanel( RunOurSimulationsPanel.class );
        StaticPage.addPanel( FullInstallPanel.class );
        StaticPage.addPanel( OneAtATimePanel.class );
        StaticPage.addPanel( ResearchPanel.class );
        StaticPage.addPanel( TroubleshootingMacPanel.class );
        StaticPage.addPanel( TroubleshootingWindowsPanel.class );
        StaticPage.addPanel( TroubleshootingMobilePanel.class );
        StaticPage.addPanel( JavaSecurity.class );
        StaticPage.addPanel( AboutSourceCodePanel.class );
        StaticPage.addPanel( AboutLegendPanel.class );
        StaticPage.addPanel( AboutContactPanel.class );
        StaticPage.addPanel( AboutLicensingPanel.class );
        StaticPage.addPanel( AboutSponsorsPanel.class );
        StaticPage.addPanel( TeacherIdeasPanel.class );
        StaticPage.addPanel( ContributionGuidelinesPanel.class );
        StaticPage.addPanel( DonatePanel.class ); // english donation page
        // redirecting to
        // https://donatenow.networkforgood.org/1437859
        StaticPage.addPanel( ForTranslatorsPanel.class );
        StaticPage.addPanel( TranslationUtilityPanel.class );
        StaticPage.addPanel( AboutNewsPanel.class );
        StaticPage.addPanel( ClassroomUsePanel.class );
        StaticPage.addPanel( UgandaWorkshopsPanel.class );
        StaticPage.addPanel( UgandaWorkshopPhotosPanel.class );
        StaticPage.addPanel( ChangePasswordSuccessPanel.class );
        StaticPage.addPanel( FaqTestPage.class );

        StaticPage.addPanel( AboutTeamPanel.class );

        // NOTE: Adding another static panel? Make sure it's cached properly by
        // Varnish

        StaticPage.addPanel( HTMLLicensingPanel.class );
        StaticPage.addPanel( AccessibilityPanel.class );
        // NOTE: Adding another static panel? Make sure it's cached properly by Varnish

        // create a url mapper, and add the page classes to it
        mapper = new PhetUrlMapper();
        HTML5Page.addToMapper( mapper );
        ByGradeLevelPage.addToMapper( mapper ); // always add this before
        ByDevicePage.addToMapper( mapper ); // always add this before
        // CategoryPage so it can
        // display the icons
        CategoryPage.addToMapper( mapper );
        SimulationPage.addToMapper( mapper );
        LegacySimulationPage.addToMapper( mapper );
        SimulationChangelogPage.addToMapper( mapper );
        StaticPage.addToMapper( mapper );
        IndexPage.addToMapper( mapper );
        SimsByKeywordPage.addToMapper( mapper );
        SearchResultsPage.addToMapper( mapper );
        EditProfilePage.addToMapper( mapper );
        SignInPage.addToMapper( mapper );
        SignOutPage.addToMapper( mapper );
        RegisterPage.addToMapper( mapper );
        ContributionPage.addToMapper( mapper );
        ContributionBrowsePage.addToMapper( mapper );
        ContributionCreatePage.addToMapper( mapper );
        ContributionEditPage.addToMapper( mapper );
        ContributionManagePage.addToMapper( mapper );
        ContributionSuccessPage.addToMapper( mapper );
        ContributionCommentSuccessPage.addToMapper( mapper );
        ContributionNominationSuccessPage.addToMapper( mapper );
        TranslatedSimsPage.addToMapper( mapper );
        TranslationMainPage.addToMapper( mapper );
        AddContributionCommentPage.addToMapper( mapper );
        NominateContributionPage.addToMapper( mapper );
        ChangePasswordPage.addToMapper( mapper );
        ResetPasswordRequestPage.addToMapper( mapper );
        ResetPasswordRequestSuccessPage.addToMapper( mapper );
        ResetPasswordCallbackPage.addToMapper( mapper );
        InitialSubscribePage.addToMapper( mapper );
        ConfirmEmailLandingPage.addToMapper( mapper );
        UnsubscribeLandingPage.addToMapper( mapper );
        SponsorListPage.addToMapper( mapper );
        SimulationFAQPage.addToMapper( mapper );
        TechAwardPage.addToMapper( mapper );
        WorldPhotosPage.addToMapper( mapper );
        MediaImagesPage.addToMapper( mapper );
        TeachWithPhetDemoPage.addToMapper( mapper );
        WebglDisabledPage.addToMapper( mapper );

        // don't error if a string isn't found
        getResourceSettings().setThrowExceptionOnMissingResource( false );

        // set up the custom localizer
        getResourceSettings().setLocalizer( PhetLocalizer.get() );

        // set up LRE terms
        LRETerm.initialize();

        // set up the locales that will be accessible
        initializeTranslations();
        mount( new PhetUrlStrategy( "en", mapper ) );
        for ( Translation translation : translations ) {
            mount( new PhetUrlStrategy( LocaleUtils.localeToString( translation
                                                                            .getLocale() ), mapper ) );
        }
        mount( new TranslationUrlStrategy( "translation", mapper ) );

        mountBookmarkablePage( "admin/main", AdminMainPage.class );
        mountBookmarkablePage( "admin/deploy", DeployProjectPage.class );
        mountBookmarkablePage( "admin/deploy-translation",
                               DeployTranslationPage.class );
        mountBookmarkablePage( "admin/deploy-resource", DeployResourcePage.class );
        mountBookmarkablePage( "admin/new-installer",
                               AdminNewInstallerPage.class );
        mountBookmarkablePage( "admin/tech-docs", TechnicalDocPage.class );
        mountBookmarkablePage( "admin/translation-docs",
                               TranslationDocPage.class );

        mountBookmarkablePage( "2013/the-future-of-phet", TheFutureOfPhet.class );

        mountBookmarkablePage( "webgl-disabled-page", WebglRedirectPage.class );

        // services
        mountBookmarkablePage( "services/check-login", CheckLogin.class );
        mountBookmarkablePage( "services/phet-info", PhetInfoServicePage.class );
        mountBookmarkablePage( "services/phet-info.php", PhetInfoServicePage.class );
        mountBookmarkablePage( "services/sim-jar-redirect", SimJarRedirectPage.class );
        mountBookmarkablePage( "services/sim-jar-redirect.php", SimJarRedirectPage.class );
        mountBookmarkablePage( "services/get-string", StringGetter.class );
        mountBookmarkablePage( "services/nc-info.csv", NCInformationPage.class );
        mountBookmarkablePage( "services/english-summary.csv", EnglishSummaryExportCSV.class );
        mountBookmarkablePage( "services/learning-com-export.csv", LearningComExport.class );
        mountBookmarkablePage( "services/iqity-export.csv", IQityExportCSV.class );
        mountBookmarkablePage( "services/project-sorted-simulations.csv", ProjectSortedSimulations.class );
        mountBookmarkablePage( "services/metadata/simulation-nodecl", SimulationMetadataFormatServiceWithoutDeclaration.class );
        mountBookmarkablePage( "services/metadata/simulation", SimulationMetadataFormatService.class );
        mountBookmarkablePage( "services/translation-list", TranslationList.class );
        mountBookmarkablePage( "services/varnish-health-check", HealthCheck.class );
        mountBookmarkablePage( "services/nagios-health-check", HealthCheck.class );
        mountBookmarkablePage( "services/general-health-check", HealthCheck.class );
        // mountBookmarkablePage( "services/github-push", GithubEmailHook.class );
        mountBookmarkablePage( "autocomplete", Autocomplete.class );
        mountBookmarkablePage( "robots.txt", RobotsTxtPage.class );
        mountBookmarkablePage( "metadata-dump.xml", MetadataDump.class );

        // FOR XSS TESTING
        // mountBookmarkablePage( "xsstest", PreventXSSTest.class );

        // For nested for mtesting
        mountBookmarkablePage( "nested-form-test", NestedFormTest.class );
        mountBookmarkablePage( "changelog-test", PublicChangelogTest.class );

        // this will remove the default string resource loader. essentially this
        // new one has better locale-handling,
        // so that if a string is not found for a more specific locale (es_MX),
        // it would try "es", then the default
        // properties file
        // NOTE: This may break in Wicket 1.4 (hopefully fixed?)
        assert ( getResourceSettings().getStringResourceLoaders().size() == 1 );
        getResourceSettings().addStringResourceLoader( 0,
                                                       new ClassStringResourceLoader( PhetWicketApplication.class ) );

        // initialize the navigation menu
        menu = new NavMenu();

        // get rid of wicket:id's and other related tags in the produced HTML.
        getMarkupSettings().setStripWicketTags( true );

        mount( new HybridUrlCodingStrategy( "/error", ErrorPage.class ) );
        mount( new HybridUrlCodingStrategy( "/error/404", NotFoundPage.class ) );
        mount( new HybridUrlCodingStrategy( "/activities", BlankPage.class ) );
        mount( new HybridUrlCodingStrategy( "/uptime-check", BlankPage.class ) ); // landing
        // page
        // for
        // checking
        // that
        // the
        // PhET
        // site
        // is
        // up

        logger.info( "Running as: " + getConfigurationType() );
        try {
            logger.debug( "Detected phet-document-root: "
                          + getWebsiteProperties().getPhetDocumentRoot()
                    .getAbsolutePath() );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        NotificationHandler.initialize( websiteProperties );

        SearchUtils.initialize();

        SchedulerService.initialize( this, PhetLocalizer.get() ); // start up
        // cron4j jobs

        BuildLocalProperties.initFromPropertiesFile( getWebsiteProperties().getBuildLocalPropertiesFile() );

        setInstallerTimestampFromFile();

        // if there are new strings that should be added, add them
        StringChanges.checkNewStrings();
        NewSimPagesMigrator.migrateSims();
    }

    private void setupJulSfl4j() {
        java.util.logging.Logger rootLogger = LogManager.getLogManager()
                .getLogger( "" );
        rootLogger.setLevel( java.util.logging.Level.ALL );
        Handler[] handlers = rootLogger.getHandlers();
        for ( Handler handler : handlers ) {
            rootLogger.removeHandler( handler );
        }
        // SLF4JBridgeHandler.install();
        SLF4JBridgeHandler handler = new SLF4JBridgeHandler();
        LogManager.getLogManager().getLogger( "" ).addHandler( handler );
        handler.setLevel( java.util.logging.Level.ALL );
    }

    private void setInstallerTimestampFromFile() {
        try {
            File propsFile = new File( getWebsiteProperties()
                                               .getPhetDocumentRoot(), "installer/version.properties" );
            Properties props = new Properties();
            FileInputStream stream = new FileInputStream( propsFile );
            try {
                props.load( stream );
                long ver = Long.valueOf( props.getProperty( "timestamp" ) );
                InstallerCache.updateTimestamp( ver );
            }
            finally {
                stream.close();
            }
        }
        catch( RuntimeException e ) {
            logger.error( "setInstallerTimestamp runtime exception" );
            InstallerCache.setDefault();
        }
        catch( IOException e ) {
            logger.error( "setInstallerTimestamp IO exception" );
            InstallerCache.setDefault();
        }
    }

    public boolean isDeployment() {
        return getConfigurationType().equals( Application.DEVELOPMENT );
    }

    public boolean isDevelopment() {
        return getConfigurationType().equals( Application.DEVELOPMENT );
    }

	/*---------------------------------------------------------------------------*
     * server-specific configuration locations
	 *----------------------------------------------------------------------------*/

    public File getActivitiesRoot() {
        return new File( getWebsiteProperties().getPhetDownloadRoot(),
                         "activities" );
    }

    public File getTeachersGuideRoot() {
        return new File( getWebsiteProperties().getPhetDownloadRoot(),
                         "teachers-guide" );
    }

    public File getSimulationsRoot() {
        return new File( getWebsiteProperties().getPhetDocumentRoot(), "sims" );
    }

    public File getStagingRoot() {
        return new File( getWebsiteProperties().getPhetDocumentRoot(), "staging" );
    }

    public String getActivitiesLocation() {
        return getWebsiteProperties().getPhetDownloadLocation() + "/activities";
    }

    public String getTeachersGuideLocation() {
        return getWebsiteProperties().getPhetDownloadLocation()
               + "/teachers-guide";
    }

    public WebsiteProperties getWebsiteProperties() {
        return websiteProperties;
    }

	/*---------------------------------------------------------------------------*
	 * supported locales and translations
	 *----------------------------------------------------------------------------*/

    private PhetLocales supportedLocales = PhetLocales.getInstance();

    public PhetLocales getSupportedLocales() {
        // if ( supportedLocales == null ) {
        // supportedLocales = PhetLocales.getInstance();
        // }
        return supportedLocales;
    }

    private synchronized void initializeTranslations() {
        org.hibernate.Session session = HibernateUtils.getInstance()
                .openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            translations = HibernateUtils.getVisibleTranslations( session );

            tx.commit();
        }
        catch( RuntimeException e ) {
            logger.warn( "WARNING: exception:\n" + e );
            if ( tx != null && tx.isActive() ) {
                try {
                    tx.rollback();
                }
                catch( HibernateException e1 ) {
                    logger.error( "ERROR: Error rolling back transaction", e1 );
                }
                throw e;
            }
        }
        session.close();

        sortTranslations();
    }

    private synchronized void sortTranslations() {
        Collections.sort( translations, new Comparator<Translation>() {
            public int compare( Translation a, Translation b ) {
                return a.getLocale().getDisplayName()
                        .compareTo( b.getLocale().getDisplayName() );
            }
        } );
    }

    // always return a defensive copy, as this is modified at some calling sites
    public synchronized List<String> getTranslationLocaleStrings() {
        // TODO: maybe cache this list if it's called a lot?
        List<String> ret = new LinkedList<String>();
        for ( Translation translation : translations ) {
            ret.add( LocaleUtils.localeToString( translation.getLocale() ) );
        }
        return ret;
    }

    public synchronized int getTranslationCount() {
        return translations.size();
    }

    public synchronized List<Locale> getAllVisibleTranslationLocales() {
        List<Locale> ret = new LinkedList<Locale>();
        ret.add( WebsiteConstants.ENGLISH ); // TODO: refactor translations to
        // include English!
        for ( Translation translation : translations ) {
            ret.add( translation.getLocale() );
        }
        return ret;
    }

    public synchronized boolean isVisibleLocale( Locale locale ) {
        if ( locale.equals( WebsiteConstants.ENGLISH ) ) {
            return true;
        }
        for ( Translation translation : translations ) {
            if ( translation.getLocale().equals( locale ) ) {
                return true;
            }
        }
        return false;
    }

    public NavMenu getMenu() {
        return menu;
    }

    @Override
    public Session newSession( Request request, Response response ) {
        return new PhetSession( request );
    }

    @Override
    public RequestCycle newRequestCycle( Request request, Response response ) {
        return new PhetRequestCycle( this, (WebRequest) request, response );
    }

    // TODO: separate out translation parts into another area?
    private List<TranslationChangeListener> translationChangeListeners = new LinkedList<TranslationChangeListener>();

    public synchronized void addTranslationChangeListener(
            TranslationChangeListener listener ) {
        translationChangeListeners.add( listener );
    }

    public synchronized void removeTranslationChangeListener(
            TranslationChangeListener listener ) {
        translationChangeListeners.remove( listener );
    }

    private synchronized void notifyTranslationChangeListeners() {
        // make a copy before for modification during traversal avoidance
        for ( TranslationChangeListener listener : new LinkedList<TranslationChangeListener>(
                translationChangeListeners ) ) {
            listener.onChange();
        }
    }

    public synchronized void addTranslation( Translation translation ) {
        String localeString = LocaleUtils.localeToString( translation
                                                                  .getLocale() );
        logger.info( "Adding translation for " + localeString );
        getResourceSettings().getLocalizer().clearCache();
        translations.add( translation );
        mount( new PhetUrlStrategy( localeString, mapper ) );
        sortTranslations();

        notifyTranslationChangeListeners();
    }

    public synchronized void removeTranslation( Translation translation ) {
        String localeString = LocaleUtils.localeToString( translation
                                                                  .getLocale() );
        logger.info( "Removing translation for " + localeString );
        getResourceSettings().getLocalizer().clearCache();
        int oldNumTranslations = translations.size();
        for ( Translation tr : translations ) {
            if ( tr.getId() == translation.getId() ) {
                translations.remove( tr );
                break;
            }
        }
        if ( translations.size() != oldNumTranslations - 1 ) {
            throw new RuntimeException(
                    "Did not correctly remove old translation" );
        }
        unmount( localeString );
        sortTranslations();

        notifyTranslationChangeListeners();
    }

    @Override
    /**
     * Override request cycle processor, so we can handle things like redirection
     */
    protected IRequestCycleProcessor newRequestCycleProcessor() {
        return new PhetCycleProcessor();
    }

    public static PhetWicketApplication get() {
        try {
            return (PhetWicketApplication) WebApplication.get();
        }
        catch( WicketRuntimeException e ) {
            // attempting from an outside thread
            synchronized( PhetWicketApplication.class ) {
                return instance;
            }
        }
    }

    @Override
    protected void onDestroy() {
        try {
            logger.info( "Shutting down PhetWicketApplication" );
            SchedulerService.destroy(); // stop cron4j jobs first
            SearchUtils.destroy();

            logger.info( HibernateUtils.getInstance().getCache().getClass()
                                 .getCanonicalName() );
        }
        catch( Exception e ) {
            logger.error( e );
        }
    }

    public static String getProductionServerName() {
        return "phet.colorado.edu";
    }

    public static String getTestingServerName() {
        return "phet-server.colorado.edu";
    }

    public boolean isProductionServer() {
        return websiteProperties.getWebHostname().equals(
                getProductionServerName() );
    }

    public boolean isTestingServer() {
        return websiteProperties.getWebHostname()
                .equals( getTestingServerName() );
    }

    public static interface TranslationChangeListener {
        public void onChange();
    }

}
