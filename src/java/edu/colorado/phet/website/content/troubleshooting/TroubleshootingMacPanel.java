/*
 * Copyright 2014, University of Colorado
 */

package edu.colorado.phet.website.content.troubleshooting;

import edu.colorado.phet.common.phetcommon.view.util.ImageLoader;
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

//        add( new LocalizedText( "intro", "troubleshooting.mac.intro", new Object[] {
//                 new Linkers.HelpMailer().getHref( context, getPhetCycle() )
//        } ) );

        add( new LocalizedText( "troubleshooting-mac-q1-answer", "troubleshooting.mac.q1.answer" ) );
        add( new LocalizedText( "troubleshooting-mac-q2-answer", "troubleshooting.mac.q2.answer" ) );
        add( new LocalizedText( "troubleshooting-mac-q3-answer", "troubleshooting.mac.q3.answer" ) );
        add( new LocalizedText( "troubleshooting-mac-q4-answer", "troubleshooting.mac.q4.answer" ) );
        add( new LocalizedText( "troubleshooting-mac-q5-answer", "troubleshooting.mac.q5.answer" ) );

        add( new LocalizedText( "troubleshooting-mac-q6-answer-firefox", "troubleshooting.mac.q6.answer.firefox" ) );
        add( new LocalizedText( "troubleshooting-mac-q6-answer-chrome", "troubleshooting.mac.q6.answer.chrome", new Object[] {
                "<img src=\"//phet.colorado.edu/images/troubleshooting/file-can-harm-computer.png\" alt=\"File can harm computer image\" width=\"300\"/>",
                "<img src=\"//phet.colorado.edu/images/troubleshooting/newly-downloaded-icon.png\" alt=\"Newly downloaded image\" width=\"200\"/>"
        } ) );
        add( new LocalizedText( "troubleshooting-mac-q6-answer-safari", "troubleshooting.mac.q6.answer.safari", new Object[] {
                "<img src=\"//phet.colorado.edu/images/troubleshooting/downloads-button.png\" alt=\"Downloads icon\" height=\"15\"/>",
                "<img src=\"//phet.colorado.edu/images/troubleshooting/downloads-menu.png\" alt=\"Downloads menu image\" width=\"300\"/>"
        } ) );

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