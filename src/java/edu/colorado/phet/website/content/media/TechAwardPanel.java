// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.content.media;

import edu.colorado.phet.website.components.RawLink;
import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.content.ResearchPanel;
import edu.colorado.phet.website.content.about.AboutMainPanel;
import edu.colorado.phet.website.content.simulations.CategoryPage;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

/**
 * Information about PhET's 2011 Tech Award
 */
public class TechAwardPanel extends PhetPanel {
    public TechAwardPanel( String id, PageContext context ) {
        super( id, context );

        add( ResearchPanel.getLinker().getLink( "research-link", context, getPhetCycle() ) );

        add( new StaticImage( "tech-awards-logo", Images.LOGO_TECH_AWARDS, null ) );
        add( new StaticImage( "cambodia-image", Images.PHET_USE_IN_CAMBODIA, null ) );

        add( CategoryPage.getDefaultLinker().getLink( "interactive-simulations", context, getPhetCycle() ) );
        add( ResearchPanel.getLinker().getLink( "research-link-1", context, getPhetCycle() ) );
        add( ResearchPanel.getLinker().getLink( "research-link-2", context, getPhetCycle() ) );
        add( WorldPhotosPage.getLinker().getLink( "world-photos-link-1", context, getPhetCycle() ) );
        add( WorldPhotosPage.getLinker().getLink( "world-photos-link-2", context, getPhetCycle() ) );

        // TODO: correct these links!
        add( AboutMainPanel.getLinker().getLink( "team-link", context, getPhetCycle() ) );
        add( new RawLink( "hi-res-screenshots-link", "#" ) );
    }
}