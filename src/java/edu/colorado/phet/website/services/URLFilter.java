// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.services;

import java.io.IOException;

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
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        if ( servletRequest instanceof HttpServletRequest
             && httpRequest.getQueryString() != null
             && httpRequest.getQueryString().contains( "iframe=true&width=" ) ) {
            String requestURL = httpRequest.getRequestURL().toString();
            logger.info( "gagged url for invalid query string: " + requestURL + " with query string " + httpRequest.getQueryString() );
            logger.info( "referred from: " + httpRequest.getHeader( "referer" ) ); // the misspelling is correct

            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

            // permanent redirect to the URL with the query string stripped off (googlebot seems to be making the most requests to these)
            httpResponse.setStatus( HttpServletResponse.SC_MOVED_PERMANENTLY );
            httpResponse.setHeader( "Location", requestURL ); // request URL doesn't include query string, but should have everything else
        }
        else if ( servletRequest instanceof HttpServletRequest
                  && ( httpRequest.getRequestURL().toString().endsWith( "/en/donate" ) || httpRequest.getRequestURL().toString().endsWith( "phet.colorado.edu/donate" ) ) ) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

            // temporary redirection to the donation URL:
            httpResponse.setStatus( HttpServletResponse.SC_MOVED_TEMPORARILY );
            httpResponse.setHeader( "Location", "https://donatenow.networkforgood.org/1437859" );
        }
        else if ( servletRequest instanceof HttpServletRequest
                  && ( httpRequest.getRequestURL().toString().endsWith( "phet.colorado.edu/sponsor-test" ) ) ) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

            // temporary redirection to the sponsor test URL:
            httpResponse.setStatus( HttpServletResponse.SC_MOVED_TEMPORARILY );
            httpResponse.setHeader( "Location", "http://www.colorado.edu/physics/phet/dev/circuit-construction-kit/3.20.15/sponsor-prototype_en.jnlp" );
        }
        else {
            // otherwise, behave like normal
            chain.doFilter( servletRequest, servletResponse );
        }
    }

    public void destroy() {

    }
}
