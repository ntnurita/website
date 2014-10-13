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

public class TroubleshootingWindowsPanel extends PhetPanel {
    public TroubleshootingWindowsPanel( String id, PageContext context ) {
        super( id, context );

        add( new LocalizedText( "troubleshooting-windows-q1-answer", "troubleshooting.windows.q1.answer" ) );
        add( new LocalizedText( "troubleshooting-windows-q2-answer", "troubleshooting.windows.q2.answer" ) );
        add( new LocalizedText( "troubleshooting-windows-q3-answer", "troubleshooting.windows.q3.answer", new Object[]{
                Linkers.getHelpLink( "Windows 2000 Issues", context, getPhetCycle() )
        } ) );

        add( new LocalizedText( "troubleshooting-windows-q4-answer", "troubleshooting.windows.q4.answer", new Object[]{
                "<a href=\"http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6946221\">http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6946221</a>"
        } ) );

        add( new LocalizedText( "troubleshooting-windows-q5-answer", "troubleshooting.windows.q5.answer", new Object[]{
                "<a href=\"http://www.nvidia.com/Download/Find.aspx?lang=en-us\">Nvidia</a>",
                "<a href=\"http://support.amd.com/us/Pages/AMDSupportHub.aspx\">AMD</a>",
                "<a href=\"http://downloadcenter.intel.com/default.aspx\">Intel</a>"
        } ) );
    }

    public static String getKey() {
        return "troubleshooting.windows";
    }

    public static String getUrl() {
        return "troubleshooting/windows";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            @Override
            public String getRawUrl( PageContext context, PhetRequestCycle cycle ) {
                if ( DistributionHandler.redirectPageClassToProduction( cycle, TroubleshootingWindowsPanel.class ) ) {
                    return "http://phet.colorado.edu/tech_support/support-windows.php";
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