/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.about;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.content.IndexPage;
import edu.colorado.phet.website.content.ResearchPanel;
import edu.colorado.phet.website.content.media.TechAwardPage;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class AboutMainPanel extends PhetPanel {
    public AboutMainPanel( String id, PageContext context ) {
        super( id, context );

        // add linkers
        add( TechAwardPage.getLinker().getLink( "tech-award-link", context, getPhetCycle() ) );

        add( new LocalizedText( "about-p1", "about.p1", new Object[] {
                ResearchPanel.getLinker().getHref( context, getPhetCycle() )
        } ) );

        add( new LocalizedText( "about-p2", "about.p2" ) );

        add( new LocalizedText( "about-p3", "about.p3", new Object[] {
                AboutLegendPanel.getLinker().getHref( context, getPhetCycle() )
        } ) );

        add( new LocalizedText( "about-p4", "about.p4", new Object[] {
                IndexPage.getLinker().getHref( context, getPhetCycle() )
        } ) );
    }
    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        ((PhetMenuPage) this.getPage()).hideSocialBookmarkButtons();
        ((PhetMenuPage) this.getPage()).setContentWidth(1120);
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
