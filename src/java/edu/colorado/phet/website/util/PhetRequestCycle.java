/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.hibernate.Session;

import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * A request cycle is created for each HTTP request, and is available at any point from pages or components by calling
 * (PhetRequestCycle) getRequestCycle(). Only PhetRequestCycles should be returned.
 * <p/>
 * The PhetRequestCycle is responsible for initializing a Hibernate session that will be used for all database
 * interactions for the pages, components, and everything else.
 * <p/>
 * Additionally, support is provided to determine whether this request was made from a specialized ripper (for
 * external installers)
 */
public class PhetRequestCycle extends WebRequestCycle {

    private Session session;
    private Long start;

    private String cachedUserAgent;
    private boolean installer;
    private boolean offlineInstaller;
    private boolean ksu;
    private boolean youngAndFreeman;
    private long millisecondsToCache;

    public static final String KSU_RIPPER_USER_AGENT = "httrack-web-mirror-ar_SA";
    public static final String YOUNG_AND_FREEDMAN_USER_AGENT = "httrack-web-mirror-yf";
    public static final String RIPPER_USER_AGENT_SUBSTRING = "httrack-web";
    public static final String OFFLINE_USER_AGENT = "httrack-web-offline"; // example of full would be httrack-web-offline-en
    public static final String OFFLINE_USER_AGENT_PREFIX = OFFLINE_USER_AGENT + "-";
    public static final String ENGLISH_ONLY_OFFLINE_USER_AGENT = OFFLINE_USER_AGENT_PREFIX + "en";
    public static final String HIDE_TRANSLATIONS_USER_AGENT = "hide-translations";

    private static final Logger logger = Logger.getLogger( PhetRequestCycle.class.getName() );

    public PhetRequestCycle( WebApplication webApplication, WebRequest webRequest, Response response ) {
        this( webApplication, webRequest, response, webRequest.getHttpServletRequest().getRequestURI() );
    }

    public PhetRequestCycle( WebApplication webApplication, WebRequest webRequest, Response response, String originalRequest ) {
        super( webApplication, webRequest, response );

        logger.debug( "created request cycle for " + originalRequest );

        millisecondsToCache = 0; // by default, don't cache
    }

    @Override
    protected void onBeginRequest() {
        logger.debug( "onBeginRequest" );
        logger.debug( "request for: " + getWebRequest().getHttpServletRequest().getRequestURI() );
        start = System.currentTimeMillis();
        logger.debug( "----------" );
        session = HibernateUtils.getInstance().openSession();
        super.onBeginRequest();
    }

    @Override
    protected void onEndRequest() {
        logger.debug( "onEndRequest" );
        logger.debug( "Request cycle: " + ( System.currentTimeMillis() - start ) + " ms" );
        session.close();
        super.onEndRequest();

        // don't allow installer user agents to return cached content
        if ( millisecondsToCache > 0 && !this.isInstaller() && !this.isOriginalSecure() ) {
            WebResponse webResponse = (WebResponse) response;

            // TODO: are we overriding something important here?
            webResponse.setHeader( "Vary", "Accept-Encoding" );
            webResponse.setHeader( "Cache-control", "public, max-age=" + ( millisecondsToCache / 1000 ) );
            webResponse.setHeader( "Pragma", "public" ); // stop Wicket/Tomcat from sending no-cache
            webResponse.setDateHeader( "Expires", System.currentTimeMillis() + millisecondsToCache );
        }
    }

    public void setMillisecondsToCache( long millis ) {
        millisecondsToCache = millis;
    }

    public void setMinutesToCache( long minutes ) {
        this.setMillisecondsToCache( minutes * 60 * 1000 );
    }

    public Session getHibernateSession() {
        return session;
    }

    public String getRequestURI() {
        return getHttpServletRequest().getRequestURI();
    }

    public String getQueryString() {
        return getHttpServletRequest().getQueryString();
    }

    public String getRemoteHost() {
        return getHttpServletRequest().getRemoteHost();
    }

    /**
     * @return The Servlet HTTP request. Includes more of the 'raw' things like query string, remote / local hosts, etc.
     */
    public HttpServletRequest getHttpServletRequest() {
        return getWebRequest().getHttpServletRequest();
    }

    public String getUserAgent() {
        if ( cachedUserAgent == null ) {
            readUserAgent();
        }
        return cachedUserAgent;
    }

    public boolean isInstaller() {
        if ( cachedUserAgent == null ) {
            readUserAgent();
        }
        return installer;
    }

    public boolean isOfflineInstaller() {
        if ( cachedUserAgent == null ) {
            readUserAgent();
        }
        return offlineInstaller;
    }

    public boolean isKsuRipperRequest() {
        if ( cachedUserAgent == null ) {
            readUserAgent();
        }
        return ksu;
    }

    public boolean isYoungAndFreedmanRipperRequest() {
        if ( cachedUserAgent == null ) {
            readUserAgent();
        }
        return youngAndFreeman;
    }

    private void readUserAgent() {
        cachedUserAgent = ( (WebClientInfo) getClientInfo() ).getUserAgent();
        if ( cachedUserAgent == null ) {
            cachedUserAgent = "";
        }
        installer = getUserAgent().startsWith( RIPPER_USER_AGENT_SUBSTRING );
        offlineInstaller = getUserAgent().startsWith( OFFLINE_USER_AGENT );
        ksu = getUserAgent().equals( KSU_RIPPER_USER_AGENT );
        youngAndFreeman = getUserAgent().equals( YOUNG_AND_FREEDMAN_USER_AGENT );
    }

    /**
     * @return Whether we are being accessed from the production server URL (or a separate URL). Note that this can
     *         return various values for the same website instance depending on the name used to access it.
     */
    public boolean isForProductionServer() {
        return getHttpServletRequest().getServerName().equals( PhetWicketApplication.getProductionServerName() );
    }

    public boolean isForTestingServer() {
        return getHttpServletRequest().getServerName().equals( PhetWicketApplication.getTestingServerName() );
    }

    public boolean isLocalRequest() {
        String addr = getHttpServletRequest().getRemoteAddr();
        String host = getHttpServletRequest().getRemoteHost();
        String localhost = getHttpServletRequest().getServerName();

        if ( localhost.equals( host ) || localhost.equals( "phetsims.colorado.edu" ) || localhost.equals( "phet.colorado.edu" ) || localhost.equals( "phet-server.colorado.edu" ) ) {
            return true;
        }
        else {
            return false;
        }
    }

    public String getServerName() {
        return getHttpServletRequest().getServerName();
    }

    // NOTE: If X-Forwarded-Proto is set, we respect it
    public String getOriginalScheme() {
        String forwardedScheme = getHttpServletRequest().getHeader( "X-Forwarded-Proto" );
        if ( forwardedScheme != null ) {
            return forwardedScheme;
        }
        return getHttpServletRequest().getScheme();
    }

    public static PhetRequestCycle get() {
        return (PhetRequestCycle) WebRequestCycle.get();
    }

    // NOTE: If X-Forwarded-Proto is set, we respect it
    public boolean isOriginalSecure() {
        return this.getOriginalScheme().equals( "https" ) || this.getHttpServletRequest().isSecure();
    }

    public void redirectWithSameProtocol( String destination ) {
        setRequestTarget( new RedirectRequestTarget( StringUtils.makeUrlAbsolute( destination ) ) );
    }

}
