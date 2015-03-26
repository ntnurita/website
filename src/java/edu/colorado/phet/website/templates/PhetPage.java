/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.templates;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RedirectToUrlException;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.authentication.AuthenticatedPage;
import edu.colorado.phet.website.authentication.EditProfilePage;
import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.authentication.RegisterPage;
import edu.colorado.phet.website.authentication.SignInPage;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.components.RawBodyLabel;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.components.RawLink;
import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.content.IndexPage;
import edu.colorado.phet.website.content.about.AboutLicensingPanel;
import edu.colorado.phet.website.content.getphet.FullInstallPanel;
import edu.colorado.phet.website.content.simulations.HTML5Page;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.menu.NavMenu;
import edu.colorado.phet.website.panels.DonationBannerRegularPanel;
import edu.colorado.phet.website.panels.HTMLBanner;
import edu.colorado.phet.website.panels.JavaSecurityBanner;
import edu.colorado.phet.website.panels.LogInOutPanel;
import edu.colorado.phet.website.panels.SearchPanel;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.translation.TranslationUrlStrategy;
import edu.colorado.phet.website.util.HtmlUtils;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.WebImage;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.wicket.WicketUtils;

/**
 * This is a page that generally has the PhET header (logo, search, sign off, etc), but can be instantiated without
 * any extras with PhetPage( params, false ). Base class for all PhET web pages.
 * <p/>
 * For now, all direct subclasses should call addTitle exactly once
 */
public abstract class PhetPage extends WebPage implements Stylable {

    /*
    Google Analytics changes: based on http://code.google.com/apis/analytics/docs/tracking/asyncUsageGuide.html
    We are now using the asynchronous method for loading GA. Under "One Push, Multiple Commands", it shows the example
    for hitting multiple trackers, which we also need to do. Also using setDomainName as before
     */

    private Locale myLocale;
    private String prefix;
    private String path;
    private String variation;

    private String title = null; // initialize as null
    private RawLabel titleLabel;

    private StringBuilder debugText = new StringBuilder();

    private String metaDescription;
    private Component metaDescriptionLabel;

    private Long initStart;

    private static final int NUM_SIMS_DELIVERED = 200; // in millions

    private static final Logger logger = Logger.getLogger( PhetPage.class.getName() );

    public PhetPage( PageParameters parameters ) {
        this( parameters, true );
    }

    public PhetPage( PageParameters parameters, boolean addTemplateBindings ) {

        initStart = System.currentTimeMillis();

        if ( parameters.get( TranslationUrlStrategy.LOCALE ) != null ) {
            myLocale = (Locale) parameters.get( TranslationUrlStrategy.LOCALE );
        }
        else {
            // try again with localeString, but use english as default
            myLocale = LocaleUtils.stringToLocale( parameters.getString( TranslationUrlStrategy.LOCALE_STRING, "en" ) );
        }

        if ( parameters.getString( TranslationUrlStrategy.PREFIX_STRING ) != null ) {
            prefix = parameters.getString( TranslationUrlStrategy.PREFIX_STRING );
        }
        else {
            prefix = "/" + LocaleUtils.localeToString( myLocale ) + "/";
        }

        if ( prefix.equals( "/error/" ) ) {
            prefix = "/";
        }

        path = parameters.getString( TranslationUrlStrategy.PATH );

        // should usually default to null
        variation = parameters.getString( TranslationUrlStrategy.VARIATION );

        Session wicketSession = getSession();
        wicketSession.setLocale( myLocale );

        if ( showDebugText() ) {
            addDebugLine( "class: " + this.getClass().getSimpleName() );
            addDebugLine( "locale: " + LocaleUtils.localeToString( myLocale ) );
            addDebugLine( "path: " + path );
            addDebugLine( "prefix: " + prefix );
            addDebugLine( "session id: " + wicketSession.getId() );

            /*
            public static final String FULL_PATH = "fullPath";
            public static final String PATH = "path";
            public static final String LOCALE_STRING = "localeString";
            public static final String PREFIX_STRING = "prefixString";
            public static final String LOCALE = "locale";
            public static final String VARIATION = "variation";
             */
            addDebugLine( "FULL_PATH: " + parameters.getString( TranslationUrlStrategy.FULL_PATH ) );
            addDebugLine( "PATH: " + parameters.getString( TranslationUrlStrategy.PATH ) );
            addDebugLine( "LOCALE_STRING: " + parameters.getString( TranslationUrlStrategy.LOCALE_STRING ) );
            addDebugLine( "PREFIX_STRING: " + parameters.getString( TranslationUrlStrategy.PREFIX_STRING ) );
            addDebugLine( "LOCALE: " + parameters.getString( TranslationUrlStrategy.LOCALE ) );
            addDebugLine( "VARIATION: " + parameters.getString( TranslationUrlStrategy.VARIATION ) );

            if ( !parameters.keySet().isEmpty() ) {
                StringBuilder builder = new StringBuilder( "<table>" );
                for ( Object o : parameters.keySet() ) {
                    builder.append( "<tr><td>" + o.toString() + "</td><td>" + parameters.get( o ).toString() + "</td></tr>" );
                }
                builder.append( "</table>" );
                addDebugLine( builder.toString() );
            }
        }

        // if we are using the offline installer, add a reference for JS (asynchronous loading won't work here)
        if ( PhetRequestCycle.get().isOfflineInstaller() ) {
            add( new Label( "installer-javascript-reference", "" ) );
        }
        else {
            add( new InvisibleComponent( "installer-javascript-reference" ) );
        }

        // visual display
        if ( addTemplateBindings ) {

            add( new LocalizedText( "sim-count", "home.simulationsDelivered", new Object[] {
                    NUM_SIMS_DELIVERED
            } ) );

            // TODO: refactor static images to a single location, so paths / names can be quickly changed
            Link link = IndexPage.getLinker().getLink( "page-header-home-link", getPageContext(), getPhetCycle() );
            if ( DistributionHandler.redirectHeaderToProduction( (PhetRequestCycle) getRequestCycle() ) ) {
                link = new RawLink( "page-header-home-link", "http://phet.colorado.edu" );
            }
            add( link );
            // TODO: localize alt attributes
//            link.add( new StaticImage( "page-header-logo-image", WebImage.get( Images.PHET_LOGO ), null ) );
            link.add( new StaticImage( "page-header-logo-image", WebImage.get( Images.PHET_LOGO ), null ) );
//            add( new StaticImage( "page-header-title-image", WebImage.get( Images.LOGO_TITLE ), null ) );
            add( new StaticImage( "page-header-cu-logo", WebImage.get( Images.CU_LOGO_HORIZONTAL ), null ) );

            //add( HeaderContributor.forCss( CSS.PHET_PAGE ) );

            switch( DistributionHandler.getSearchBoxVisibility( getPhetCycle() ) ) {
                case NONE:
                    add( new InvisibleComponent( "search-panel" ) );
                    break;
                case OFFLINE_INSTALLER:
                    add( new LocalizedText( "search-panel", "installer.mostUpToDate", new Object[]{
                            new Date(),
                            FullInstallPanel.getLinker().getHref( getPageContext(), getPhetCycle() )
                    } ) );
                    break;
                case NORMAL:
                    add( new SearchPanel( "search-panel", getPageContext() ) );
                    break;
            }

            if ( prefix.startsWith( "/translation" ) && getVariation() != null ) {
                final Translation translation = new Translation();

                HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                    public boolean run( org.hibernate.Session session ) {
                        session.load( translation, Integer.valueOf( getVariation() ) );
                        return true;
                    }
                } );

                add( new Label( "translation-preview-notification", "This is a preview for translation #" + getVariation() +
                                                                    " of " + translation.getLocale().getDisplayName() +
                                                                    " (" + LocaleUtils.localeToString( translation.getLocale() ) +
                                                                    ")" ) );
            }
            else {
                add( new InvisibleComponent( "translation-preview-notification" ) );
            }

            String emergencyString = getPhetLocalizer().getBestString( getHibernateSession(), "emergencyMessage", WebsiteConstants.ENGLISH );
            if ( emergencyString == null || emergencyString.length() == 0 ) {
                add( new InvisibleComponent( "emergency-message" ) );
            }
            else {
                add( new Label( "emergency-message", emergencyString ) );
            }

            boolean isAdmin = PhetSession.get().isSignedIn() && PhetSession.get().getUser().isTeamMember();

            if ( !isAdmin && ( this instanceof SignInPage || this instanceof RegisterPage || this instanceof EditProfilePage ) ) {
                add( new InvisibleComponent( "log-in-out-panel" ) );
            }
            else {
                add( new LogInOutPanel( "log-in-out-panel", getPageContext() ) );
            }
        }

        logger.debug( "request cycle is a : " + getRequestCycle().getClass().getSimpleName() );

        add( new RawBodyLabel( "debug-page-class", "<!-- class " + getClass().getCanonicalName() + " -->" ) );
        if ( getPhetCycle().isForProductionServer() ) {
            add( new Label( "autotracking" ) ); // enable autotracking script
        }
        else {
            add( new InvisibleComponent( "autotracking" ) ); // disable autotracking
        }
        add( new RawBodyLabel( "debug-page-host", "<!-- host " + getPhetCycle().getServerName() + " -->" ) );

        if ( DistributionHandler.getSearchBoxVisibility( getPhetCycle() ) != DistributionHandler.SearchBoxVisibility.NORMAL ) {
            add( new InvisibleComponent( "js" ) );
        }
        else {
            add( new WebMarkupContainer( "js" ) );
        }

        // hide the banner for the installer, and switch banner type based on the page
        if ( PhetRequestCycle.get().isOfflineInstaller() ) {
            add( new InvisibleComponent( "html-banner" ) );
        }
        else if ( this instanceof IndexPage ) {
//            add( new JavaSecurityBanner( "donation-banner", getPageContext() ) );
//            add( new DonationBannerRegularPanel( "html-banner", getPageContext() ) );
            add( new HTMLBanner( "html-banner", getPageContext() ) );
        }
        else {
//            add( new JavaSecurityBanner( "donation-banner", getPageContext() ) );
//            add( new DonationBannerRegularPanel( "html-banner", getPageContext() ) );
            add( new HTMLBanner( "html-banner", getPageContext() ) );
        }
    }

    /**
     * Add a copyright footer that auto-updates the year
     */
    public void addCopyright() {
        add( new WebMarkupContainer( "copyright" ) {{
            int year = Calendar.getInstance().get( Calendar.YEAR );
            add ( new Label( "copyright-label", "© " + year + " University of Colorado. ") );
            add( AboutLicensingPanel.getLinker().getLink( "some-rights-link", getPageContext(), getPhetCycle() ) );
        }} );
    }

    public Locale getMyLocale() {
        return myLocale;
    }

    /**
     * @return The prefix of the URL, which generally represents /XX/ where XX is the locale.
     */
    public String getMyPrefix() {
        return prefix;
    }

    /**
     * @return The relative path underneath the prefix. Thus for a URL of http://phet.colorado.edu/en/simulations/new,
     *         this would return 'simulations/new' (the prefix would be /en/)
     */
    public String getMyPath() {
        if ( path == null ) {
            return "";
        }
        return path;
    }

    /**
     * @return Everything past 'http://phet.colorado.edu' in the URI.
     */
    public String getFullPath() {
        String p = prefix + path;
        String queryString = getPhetCycle().getQueryString();
        if ( queryString != null ) {
            p += "?" + queryString;
        }
        return p;
    }

    public PageContext getPageContext() {
        return new PageContext( getMyPrefix(), getMyPath(), getMyLocale() );
    }

    @Override
    public Locale getLocale() {
        return myLocale;
    }

    /**
     * @param title The string passed in should be properly escaped!
     */
    public void setTitle( String title ) {
        if ( hasTitle() ) {
            remove( titleLabel );
        }
        this.title = title;
        titleLabel = new RawLabel( "page-title", title );
        add( titleLabel );
    }

    public Boolean hasTitle() {
        return this.title != null && titleLabel != null;
    }

    public String getTitle() {
        return title;
    }

    public org.hibernate.Session getHibernateSession() {
        return ( (PhetRequestCycle) getRequestCycle() ).getHibernateSession();
    }

    public NavMenu getNavMenu() {
        return ( (PhetWicketApplication) getApplication() ).getMenu();
    }

    public boolean showDebugText() {
        //return PhetWicketApplication.get().isDevelopment();
        return false;
    }

    private Long renderStart;

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        renderStart = System.currentTimeMillis();
        logger.debug( "Pre-render: " + ( renderStart - initStart ) + " ms" );
        //logger.debug( "stack trace: ", new Exception() );
        logger.debug( "Debug: page stateless = " + isPageStateless() );
        addDebugLine( "pre-render: " + ( renderStart - initStart ) + " ms" );
        addDebugLine( "page stateless: <span style='color: #" + ( isPageStateless() ? "00ff00" : "ff0000" ) + "'>" + isPageStateless() + "</span>" );

        // add a debug panel in that shows us some info
        if ( showDebugText() ) {
            add( WicketUtils.createStringHeaderContributor( "<script type=\"text/javascript\">addEventListener( 'load', function() {\n" +
                                                            "            var div = document.createElement( 'div' );\n" +
                                                            "            div.style.position = \"fixed\";\n" +
                                                            "            div.style.right = \"0\";\n" +
                                                            "            div.style.bottom = \"0\";\n" +
                                                            "            div.style.border = \"1px solid #888\";\n" +
                                                            "            div.style.padding = \"0.3em\";\n" +
                                                            "            div.style.background = \"#fff\";\n" +
                                                            "            div.style.maxWidth = \"300px\";\n" +
                                                            "            div.id = \"phet-page-debug\";\n" +
                                                            "            div.innerHTML = \"" + debugText.toString() + "\";\n" +
                                                            "            document.body.appendChild( div );\n" +
                                                            "        }, false );</script>" ) );
        }

        // add meta description if it does not already exist
        if ( metaDescriptionLabel == null ) {
            if ( metaDescription == null || !getLocale().equals( WebsiteConstants.ENGLISH ) ) {
                metaDescriptionLabel = new InvisibleComponent( "metaDescription" );
            }
            else {
                metaDescriptionLabel = new RawLabel( "metaDescription", "<meta name=\"description\" content=\"" + HtmlUtils.encodeForAttribute( metaDescription ) + "\"/>" ) {{
                    setRenderBodyOnly( true );
                }};
            }
            add( metaDescriptionLabel );
        }
    }

    public void setMetaDescription( String desc ) {
        metaDescription = desc;
    }

    public void setMetaDescriptionKey( String key ) {
        metaDescription = getPhetLocalizer().getString( key, this );
    }

    public void addDebugLine( String str ) {
        if ( showDebugText() ) {
            debugText.append( str ).append( "<br/>" );
        }
    }

    public void throwRedirectException() {
        HttpServletRequest request = getPhetCycle().getHttpServletRequest();
        String uri = request.getServletPath();
        if (request.getQueryString() != null) {
            uri += "?" + request.getQueryString();
        }
        String url = SignInPage.getLinker( uri ).getRawUrl( getPageContext(), getPhetCycle() );
        throw new RedirectToUrlException( url );
    }

    @Override
    protected void onAfterRender() {
        super.onAfterRender();
        logger.debug( "Render: " + ( System.currentTimeMillis() - renderStart ) + " ms" );
    }

    @Override
    protected void onDetach() {
        logger.debug( "Detaching page" );
        super.onDetach();
    }

    @Override
    public String getVariation() {
        return variation;
    }

    public PhetRequestCycle getPhetCycle() {
        return (PhetRequestCycle) getRequestCycle();
    }

    public PhetLocalizer getPhetLocalizer() {
        return (PhetLocalizer) getLocalizer();
    }

    public ServletContext getServletContext() {
        return ( (PhetWicketApplication) getApplication() ).getServletContext();
    }

    public String getStyle( String key ) {
        if ( key.equals( "style.body.id" ) ) {
            if ( getPhetCycle().isOfflineInstaller() ) {
                return "offline-installer-body";
            }
            else {
                return "other-body";
            }
        }
        if ( key.equals( "style.lang" ) ) {
            return getMyLocale().getLanguage(); // note: this adds the attribute for xml:lang. see JIRA WICKET-1229 for the reason that the colons work
        }
        return "";
    }

    /**
     * If the user is not signed in, redirect them to the sign-in page.
     */
    protected void verifySignedIn() {
        if ( !PhetSession.get().isSignedIn() ) {
            throwRedirectException();
        }
//        AuthenticatedPage.checkSignedIn( getPageContext() );
    }

    /**
     * @param component The component to add
     * @param id        The HTML id attribute
     * @return The page itself
     */
    public PhetPage addWithId( Component component, String id ) {
        add( component );
        component.setMarkupId( id );
        component.setOutputMarkupId( true );
        return this; // similar to MarkupContainer.add()
    }

}