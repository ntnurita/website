package edu.colorado.phet.website.util;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.Model;

/**
 * Shortcut class for appending a class to the element's class attribute
 */
public class ClassAppender extends AttributeAppender {
    public ClassAppender( String clazz ) {
        super( "class", true, new Model<String>( clazz ), " " );
    }
}
