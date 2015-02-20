/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.util;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;

public class ImageUtils {

    private static final Logger logger = Logger.getLogger( ImageUtils.class.getName() );

    public static Dimension getImageFileDimension( File imageFile ) {
        try {
            ImageInputStream in = ImageIO.createImageInputStream( imageFile );
            try {
                Iterator<ImageReader> iter = ImageIO.getImageReaders( in );
                if ( iter.hasNext() ) {
                    ImageReader reader = iter.next();
                    reader.setInput( in );
                    int width = reader.getWidth( 0 );
                    int height = reader.getHeight( 0 );
                    reader.dispose();
                    return new Dimension( width, height );
                }
            }
            finally {
                in.close();
            }
        }
        catch ( IOException e ) {
            logger.warn( "image file dimension failure for " + imageFile.getAbsolutePath(), e );
        }
        catch ( IllegalArgumentException e ) {
            logger.warn( "image file dimension failure (argument) for " + imageFile.getAbsolutePath(), e );
        }
        catch ( NullPointerException e ) {
            logger.warn( "Null pointer exception in getImageFileDimension" + imageFile.getAbsolutePath(), e );
        }
        return null; // unknown
    }

    public static Dimension getImageStreamDimension( InputStream inputStream, String imageName ) {
        // TODO: remove duplication once we know what works
        try {
            ImageInputStream in = ImageIO.createImageInputStream( inputStream );
            try {
                Iterator<ImageReader> iter = ImageIO.getImageReaders( in );
                while ( iter.hasNext() ) {
                    ImageReader reader = iter.next();
                    reader.setInput( in );
                    int width = reader.getWidth( 0 );
                    int height = reader.getHeight( 0 );
                    reader.dispose();
                    return new Dimension( width, height );
                }
            }
            finally {
                in.close();
            }
        }
        catch ( IOException e ) {
            logger.warn( "image file dimension failure for stream for " + imageName, e );
        }
        return null; // unknown
    }
}
