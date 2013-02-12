// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.data;

import java.io.Serializable;

import edu.colorado.phet.website.data.util.IntId;

/**
 * An educational alignment to a standard.
 */
public class Alignment implements Serializable, IntId {

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

    public Framework getFramework() {
        return framework;
    }

    public void setFramework( Framework framework ) {
        this.framework = framework;
    }

    @Override public int hashCode() {
        return url.hashCode() * 31 + framework.hashCode() + id;
    }

    @Override public boolean equals( Object obj ) {
        if( obj instanceof Alignment ) {
            Alignment other = (Alignment) obj;
            return id == other.id && url.equals( other.url ) && framework == other.framework;
        } else {
            return false;
        }
    }
}
