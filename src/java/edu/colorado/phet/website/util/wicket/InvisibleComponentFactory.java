/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.util.wicket;

import edu.colorado.phet.website.components.InvisibleComponent;

/**
 * Creates an invisible component
 */
public class InvisibleComponentFactory implements IComponentFactory<InvisibleComponent> {
    public InvisibleComponent create( String id ) {
        return new InvisibleComponent( id );
    }
}
