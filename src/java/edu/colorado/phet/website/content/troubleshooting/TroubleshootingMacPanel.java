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

public class TroubleshootingMacPanel extends PhetPanel {
    public TroubleshootingMacPanel( String id, PageContext context ) {
        super( id, context );

        // add( new LocalizedText( "intro", "troubleshooting.mac.intro", new Object[] {
        //         new Linkers.HelpMailer().getHref( context, getPhetCycle() )
        // } ) );

//        add( new LocalizedText( "troubleshooting-mac-q1-answer", "troubleshooting.mac.q1.answer" ) );
//        add( new LocalizedText( "troubleshooting-mac-q2-answer", "troubleshooting.mac.q2.answer" ) );
//        add( new LocalizedText( "troubleshooting-mac-q3-answer", "troubleshooting.mac.q3.answer" ) );
//        add( new LocalizedText( "troubleshooting-mac-q4-answer", "troubleshooting.mac.q4.answer" ) );
//        add( new LocalizedText( "troubleshooting-mac-q5-answer", "troubleshooting.mac.q5.answer" ) );
//        add( new LocalizedText( "troubleshooting-mac-q6-answer", "troubleshooting.mac.q6.answer" ) );

    }

    public static String getKey() {
        return "troubleshooting.mac";
    }

    public static String getUrl() {
        return "troubleshooting/mac";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            @Override
            public String getRawUrl( PageContext context, PhetRequestCycle cycle ) {
                if ( DistributionHandler.redirectPageClassToProduction( cycle, TroubleshootingMacPanel.class ) ) {
                    return "http://phet.colorado.edu/tech_support/support-mac.php";
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