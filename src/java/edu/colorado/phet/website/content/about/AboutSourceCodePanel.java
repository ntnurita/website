/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.about;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.content.ResearchPanel;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class AboutSourceCodePanel extends PhetPanel {
    public AboutSourceCodePanel( String id, PageContext context ) {
        super( id, context );

        add( new LocalizedText( "about-source-code-p1", "about.source-code.p1" ) );

        add( new LocalizedText( "about-source-code-p2", "about.source-code.p2", new Object[] {
                "href=\"https://github.com/phetsims\""
        } ) );

        add( new LocalizedText( "about-source-code-p3", "about.source-code.p3" ) );
        add( new LocalizedText( "about-source-code-p4", "about.source-code.p4" ) );
        add( new LocalizedText( "about-source-code-p5", "about.source-code.p5" ) );
    }

    public static String getKey() {
        return "about.source-code";
    }

    public static String getUrl() {
        return "about/source-code";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            @Override
            public String getRawUrl( PageContext context, PhetRequestCycle cycle ) {
                if ( DistributionHandler.redirectPageClassToProduction( cycle, AboutSourceCodePanel.class ) ) {
                    return "http://phet.colorado.edu/about/source-code.php";
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