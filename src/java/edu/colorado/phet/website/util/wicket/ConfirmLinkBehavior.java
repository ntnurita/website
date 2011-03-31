/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.util.wicket;

import org.apache.wicket.behavior.SimpleAttributeModifier;

import edu.colorado.phet.website.util.HtmlUtils;

public class ConfirmLinkBehavior extends SimpleAttributeModifier {
    public ConfirmLinkBehavior( String confirmMessage ) {
        super( "onclick", "if(!confirm('" + HtmlUtils.encodeForAttribute( confirmMessage ) + "')) return false;" );
    }
}
