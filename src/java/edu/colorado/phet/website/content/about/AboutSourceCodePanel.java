/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.about;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class AboutSourceCodePanel extends PhetPanel {
    public AboutSourceCodePanel( String id, PageContext context ) {
        super( id, context );

        add( new LocalizedText( "browseOnline-visitUrlStep", "about.source-code.browseOnline.visitUrlStep", new Object[]{
                "<a href=\"https://phet.unfuddle.com/a#/repositories/23262/browse?path=/trunk\">https://phet.unfuddle.com/a#/repositories/23262/browse?path=/trunk</a>"
        } ) );

        add( new LocalizedText( "browseOnline-loginStep", "about.source-code.browseOnline.loginStep", new Object[]{
                "guest", // username
                "guest"
        } ) );

        add( new LocalizedText( "checkout-header", "about.source-code.checkout.header", new Object[]{
                "href=\"http://subversion.apache.org/\""
        } ) );

        add( new LocalizedText( "build-and-run", "about.source-code.buildAndRun.header" ) );

        add( new LocalizedText( "using-pbg", "about.source-code.usingPBG.header" ) );

        add( new LocalizedText( "pbg-building-flash-flex", "about.source-code.usingPBG.flashFlex", new Object[]{
                "<a href=\"/templates/build-local.properties\">build-local.properties</a>"
        } ) );

//        add( new LocalizedText( "source-code-location", "about.source-code.location", new Object[]{
//                "href=\"https://phet.unfuddle.com/projects/9404/repositories/23262/browse/head/trunk\"",
//                "href=\"http://tortoisesvn.tigris.org/\"",
//                "href=\"http://www.syntevo.com/smartsvn/download.jsp\""
//        } ) );

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