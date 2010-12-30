/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.util.wicket;

import org.apache.wicket.Component;

import edu.colorado.phet.website.components.InvisibleComponent;

public abstract class OptionComponent {
    private String id;

    protected OptionComponent( String id ) {
        this.id = id;
    }

    public abstract Component getComponent( String id );

    public Component ifTrue( boolean visible ) {
        return ( visible ? getComponent( id ) : new InvisibleComponent( id ) );
    }
}
