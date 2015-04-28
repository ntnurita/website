// Copyright 2002-2015, University of Colorado

package edu.colorado.phet.website.util.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * This class is used to append a validation-error class to components that fail to validate.
 * If the component is not a FormComponent (such as a div surrounding several FromComponents),
 * then isValid must be manually set.
 *
 * This code taken from the Apache Wicket Cookbook and modified.
 */
public class ErrorAppender extends AbstractBehavior {
    private boolean formComponent; // true if the component is a FormComponent
    public boolean isValid = true;

    public ErrorAppender() {
        this( true );
    }

    public ErrorAppender( boolean formComponent ) {
        super();
        this.formComponent = formComponent;
    }

    @Override
    public void onComponentTag( Component component, ComponentTag componentTag ) {
        if ( formComponent ) {
            if ( !( (FormComponent<?>) component ).isValid() ) {
                String cl = componentTag.getAttribute( "class" );
                if ( cl == null ) {
                    componentTag.put( "class", "validation-error" );
                }
                else {
                    componentTag.put( "class", "validation-error " + cl );
                }
            }
        }
        else {
            if ( !isValid ) {
                String cl = componentTag.getAttribute( "class" );
                if ( cl == null ) {
                    componentTag.put( "class", "validation-error" );
                }
                else {
                    componentTag.put( "class", "validation-error " + cl );
                }
            }
        }
    }
}
