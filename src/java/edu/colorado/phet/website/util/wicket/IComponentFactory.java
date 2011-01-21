/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.util.wicket;

import java.io.Serializable;

import org.apache.wicket.Component;

/**
 * Ability to create Wicket components with a particular string ID.
 *
 * @param <T> Component class
 */
public interface IComponentFactory<T extends Component> extends Serializable {
    public T create( String id );
}
