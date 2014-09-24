/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.about;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.content.forteachers.ForTeachersPanel;
import edu.colorado.phet.website.content.media.TechAwardPage;
import edu.colorado.phet.website.panels.sponsor.SponsorsPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

// Note that this class extends ForTeachersPanel because it sets the correct width and hides the social media
// icons by default, even though it is not a for-teachers page. ForTeachersPanel should probably be renamed at some point.
public class AboutMainPanel extends ForTeachersPanel {
    public AboutMainPanel( String id, PageContext context ) {
        super( id, context );

        // add linkers
        add( TechAwardPage.getLinker().getLink( "tech-award-link", context, getPhetCycle() ) );
        add( AboutSourceCodePanel.getLinker().getLink( "source-code-link", context, getPhetCycle() ) );
        add( AboutSponsorsPanel.getLinker().getLink( "sponsors-link", context, getPhetCycle() ) );
    }

    public static String getKey() {
        return "about";
    }

    public static String getUrl() {
        return "about";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            @Override
            public String getRawUrl( PageContext context, PhetRequestCycle cycle ) {
                if ( DistributionHandler.redirectPageClassToProduction( cycle, AboutMainPanel.class ) ) {
                    return "http://phet.colorado.edu/about/index.php";
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
