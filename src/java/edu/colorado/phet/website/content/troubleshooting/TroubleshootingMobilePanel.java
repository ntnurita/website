/*
 * Copyright 2014, University of Colorado
 */

package edu.colorado.phet.website.content.troubleshooting;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.constants.Linkers;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class TroubleshootingMobilePanel extends PhetPanel {
    public TroubleshootingMobilePanel( String id, PageContext context ) {
        super( id, context );

        add( new LocalizedText( "troubleshooting-mobile-p2", "troubleshooting.mobile.p2" , new Object[] {
                "<a href=\"http://phet.colorado.edu/en/simulations/category/html\">http://phet.colorado.edu/en/simulations/category/html</a>"
        } ) );
        add( new LocalizedText( "troubleshooting-mobile-p3", "troubleshooting.mobile.p3", new Object[] {
                "<a href=\"http://phet.colorado.edu/en/contributions/view/3870\">http://phet.colorado.edu/en/contributions/view/3870</a>"
        } ) );
        add( new LocalizedText( "troubleshooting-mobile-p4", "troubleshooting.mobile.p4", new Object[] {
                "<a href=\"http://www.youtube.com/watch?v=eBGYTPJOiNU\">http://www.youtube.com/watch?v=eBGYTPJOiNU</a>"
        } ) );
    }

    public static String getKey() {
        return "troubleshooting.mobile";
    }

    public static String getUrl() {
        return "troubleshooting/mobile";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            @Override
            public String getRawUrl( PageContext context, PhetRequestCycle cycle ) {
                if ( DistributionHandler.redirectPageClassToProduction( cycle, TroubleshootingMobilePanel.class ) ) {
                    return "http://phet.colorado.edu/tech_support/support-mobile.php";
                }
                else {
                    return super.getRawUrl( context, cycle );
                }
            }

            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }
}