/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.test;

import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class FaqTestPage extends PhetPanel {

    public FaqTestPage( String id, PageContext context ) {
        super( id, context );
    }

    public static String getKey() {
        return "sim-faq-test";
    }

    public static String getUrl() {
        return "sim-faq-test";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }
}