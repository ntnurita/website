// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.data;

import java.io.Serializable;

import edu.colorado.phet.website.data.util.IntId;

public class Alignment implements Serializable, IntId {

    public static enum Framework {
        COMMON_CORE_MATH // see http://asn.jesandco.org/resources/D10003FB
    }

    private int id;

    /**
     * Primary URL for the alignment
     */
    private String url;

    private Framework framework;

    public Alignment() {
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }
}
