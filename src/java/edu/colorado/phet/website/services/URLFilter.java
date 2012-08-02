// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.services;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Allows us to discard some URL patterns that are known to fail and cause spam in the error logs.
 */
public class URLFilter implements Filter {
    private static final Logger logger = Logger.getLogger( URLFilter.class.getName() );

    public void init( FilterConfig filterConfig ) throws ServletException {

    }

    public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain ) throws IOException, ServletException {
        logger.debug( "Filtering the URL for patterns to discard" );

        // prevents CharConversionExceptions on invalid URLs like http://phet.colorado.edu/fi/simulation/rotation?iframe=true&width=80%&height=80%
        // this was SIGNIFICANTLY cluttering our error logs
        if ( servletRequest instanceof HttpServletRequest
             && ( (HttpServletRequest) servletRequest ).getQueryString() != null
             && ( (HttpServletRequest) servletRequest ).getQueryString().contains( "iframe=true&width=80%&height=80%" ) ) {
            logger.info( "gagged url for invalid query string: " + ( (HttpServletRequest) servletRequest ).getRequestURL() + " with query string " + ( (HttpServletRequest) servletRequest ).getQueryString() );
            if ( servletResponse instanceof HttpServletRequest ) {
                ( (HttpServletResponse) servletResponse ).sendError( HttpURLConnection.HTTP_BAD_REQUEST );
            }
        }
        else {
            // otherwise, behave like normal
            chain.doFilter( servletRequest, servletResponse );
        }
    }

    public void destroy() {

    }
}
