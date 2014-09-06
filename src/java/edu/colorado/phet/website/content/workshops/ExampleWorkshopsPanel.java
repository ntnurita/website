/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.workshops;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.content.forteachers.ForTeachersPanel;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class ExampleWorkshopsPanel extends PhetPanel {

    public static final int CONTENT_WIDTH = ForTeachersPanel.CONTENT_WIDTH - PhetMenuPage.SOCIAL_ICON_PADDING;

    public ExampleWorkshopsPanel( String id, PageContext context ) {
        super( id, context );

        // add linkers
        add( WorkshopsPanel.getLinker().getLink( "workshops-link", context, PhetRequestCycle.get() ) );
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