// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.content.about;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.constants.Linkers;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class HTMLLicensingPanel extends PhetPanel {
    public HTMLLicensingPanel( String id, PageContext context ) {
        super( id, context );

        add( new LocalizedText( "html-licensing-comingSoon", "html.licensing.comingSoon", new Object[] {
                Linkers.PHET_HELP_LINK
        } ) );
    }

    public static String getKey() {
        return "html.licensing";
    }

    public static String getUrl() {
        return "html-licensing";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }
}