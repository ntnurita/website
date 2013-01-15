/*
 * Copyright 2010, University of Colorado
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

public class JavaSecurity extends PhetPanel {
    public JavaSecurity( String id, PageContext context ) {
        super( id, context );

        add( new LocalizedText( "troubleshooting-javaSecurity-intro", "troubleshooting.javaSecurity.intro", new Object[] {
            "href=\"http://www.us-cert.gov/cas/techalerts/TA13-010A.html\""
        } ) );

        add( new LocalizedText( "troubleshooting-javaSecurity-upgradeStep", "troubleshooting.javaSecurity.upgradeStep", new Object[] {
            "href=\"http://www.java.com/en/download/index.jsp\""
        } ) );

        add( new LocalizedText( "troubleshooting-javaSecurity-disableStep", "troubleshooting.javaSecurity.disableStep", new Object[] {
            "href=\"http://www.zdnet.com/how-to-disable-java-in-your-browser-on-windows-mac_p5-7000009732/\""
        } ) );
        
        add( new LocalizedText( "troubleshooting-javaSecurity-questions", "troubleshooting.javaSecurity.questions", new Object[] {
            "<a href=\"mailto:phethelp@colorado.edu\">phethelp@colorado.edu</a>"
        } ) );
        
        

    }

    public static String getKey() {
        return "troubleshooting.javaSecurity";
    }

    public static String getUrl() {
        return "troubleshooting/javaSecurity";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }
}