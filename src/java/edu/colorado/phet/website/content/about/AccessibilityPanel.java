/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.about;

import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class AccessibilityPanel extends PhetPanel {
    public AccessibilityPanel( String id, PageContext context ) {
        super( id, context );

    }

    public static String getKey() {
        return "about.accessibility";
    }

    public static String getUrl() {
        return "about/accessibility";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }
}