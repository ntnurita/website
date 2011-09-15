// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.content.awards;

import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.content.ResearchPanel;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class TechAwardPanel extends PhetPanel {
    public TechAwardPanel( String id, PageContext context ) {
        super( id, context );

        add( ResearchPanel.getLinker().getLink( "research-link", context, getPhetCycle() ) );

        add( new StaticImage( "tech-awards-logo", Images.LOGO_TECH_AWARDS, null ) );
        add( new StaticImage( "cambodia-image", Images.PHET_USE_IN_CAMBODIA, null ) );
    }

    public static String getUrl() {
        return "award/tech-award-2011";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }
}