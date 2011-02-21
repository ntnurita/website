/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.util;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.log4j.Logger;

import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.cache.ImageCache;

/**
 * Immutable image data class
 */
public class WebImage implements Serializable {
    // TODO: consider alt keys? or alt text?

    private static final Logger logger = Logger.getLogger( ImageUtils.class.getName() );

    private final String src; //
    private final Dimension dimension; // image dimensions
    private final boolean inWar; // whether the image is contained within the WAR file

    /**
     * @param src   "relative" URL, absolute to our document root.
     * @param inWar Whether this image is included in the WAR file (handled by Tomcat), or is processed by Apache
     * @return A WebImage instance. Possibly cached.
     */
    public static WebImage get( String src, boolean inWar ) {
        WebImage image = ImageCache.get( src );
        if ( image != null ) {
            return image;
        }
        return new WebImage( src, inWar );
    }

    public static WebImage get( ImageHandle handle ) {
        WebImage image = ImageCache.get( handle.src );
        if ( image != null ) {
            return image;
        }
        return new WebImage( handle.src, handle.inWar );
    }

    protected WebImage( String src, boolean inWar ) {
        this.src = src;
        this.inWar = inWar;

        if ( inWar ) {
            InputStream stream = PhetWicketApplication.get().getServletContext().getResourceAsStream( src );
            dimension = ImageUtils.getImageStreamDimension( stream );
        }
        else {
            String strippedSrc = src.startsWith( "/" ) ? src.substring( 1 ) : src; // strip leading slash if necessary
            File imageFile = new File( PhetWicketApplication.get().getWebsiteProperties().getPhetDocumentRoot(), strippedSrc );
            Dimension imageDimension = null;
            try {
                imageDimension = ImageUtils.getImageFileDimension( imageFile );
            }
            catch( RuntimeException e ) {
                logger.warn( "Image dimension problem", e );
            }
            dimension = imageDimension;
        }
    }

    public String getSrc() {
        return src;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public boolean isInWar() {
        return inWar;
    }

    public boolean hasDimension() {
        return dimension != null;
    }

    public int getWidth() {
        return dimension.width;
    }

    public int getHeight() {
        return dimension.height;
    }
}
