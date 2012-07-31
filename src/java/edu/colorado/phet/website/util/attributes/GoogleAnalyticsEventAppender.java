// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.util.attributes;

/**
 * Adds an onclick event handler that fires off a Google Analytics event.
 * See https://developers.google.com/analytics/devguides/collection/gajs/eventTrackerGuide
 */
public class GoogleAnalyticsEventAppender extends JavascriptAppender {

    public GoogleAnalyticsEventAppender( String category, String action ) {
        super( "onclick", "_gaq.push(['_trackEvent', '" + category + "', '" + action + "'])" );
    }

    public GoogleAnalyticsEventAppender( String category, String action, String label ) {
        super( "onclick", "_gaq.push(['_trackEvent', '" + category + "', '" + action + "', '" + label + " from ' + document.URL])" );
    }
}
