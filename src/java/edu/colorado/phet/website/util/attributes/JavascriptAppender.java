// Copyright 2002-2012, University of Colorado

package edu.colorado.phet.website.util.attributes;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.Model;

/**
 * Shortcut class for appending a Javascript script to an element
 */
public class JavascriptAppender extends AttributeAppender {
    private final String event;
    private final String script;

    public JavascriptAppender( String event, String script ) {
        super( event, true, new Model<String>( script ), ";" );
        this.event = event;
        this.script = script;
    }

    public String getSingleAttributeBlock() {
        return event + "=\"" + script + "\"";
    }
}
