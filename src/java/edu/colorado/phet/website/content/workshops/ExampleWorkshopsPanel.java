/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.workshops;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class ExampleWorkshopsPanel extends PhetPanel {
    public ExampleWorkshopsPanel( String id, PageContext context ) {
        super( id, context );


    }

    public static String getKey() {
        return "exampleworkshops";
    }

    public static String getUrl() {
        return "for-teachers/exampleworkshops";
    }
    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }
}