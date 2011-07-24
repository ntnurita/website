// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.website.util;

import java.io.File;

import edu.colorado.phet.website.PhetWicketApplication;

/**
 * URL and file related utilities
 */
public class UrlUtils {

    /**
     * Return a File instance from a URL that is relative to the Apache document root
     *
     * @param relativeUrl Url (without host or protocol)
     * @return File instance
     */
    public static File getDocrootFile( String relativeUrl ) {
        String strippedUrl = relativeUrl.startsWith( "/" ) ? relativeUrl.substring( 1 ) : relativeUrl; // strip leading slash if necessary
        return new File( PhetWicketApplication.get().getWebsiteProperties().getPhetDocumentRoot(), strippedUrl );
    }
}
