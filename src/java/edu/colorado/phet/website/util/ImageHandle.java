/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.util;

import java.io.Serializable;

/**
 * Used to describe the location of an image that our server has direct access to.
 */
public class ImageHandle implements Serializable {
    public String src; // "relative" URL, absolute to our document root.
    public boolean inWar; // Whether this image is included in the WAR file (handled by Tomcat), or is processed by Apache

    public ImageHandle( String src, boolean inWar ) {
        this.src = src;
        this.inWar = inWar;
    }
}
