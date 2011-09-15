// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.content.about;

import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class AboutTeamPanel extends PhetPanel {
    public AboutTeamPanel( String id, PageContext context ) {
        super( id, context );
    }

    public static String getKey() {
        return "about.team";
    }

    public static String getUrl() {
        return "about/team";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }
}
