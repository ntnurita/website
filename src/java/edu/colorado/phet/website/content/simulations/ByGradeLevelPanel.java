/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.simulations;

import org.apache.wicket.markup.html.link.Link;

import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

public class ByGradeLevelPanel extends PhetPanel {

    public ByGradeLevelPanel( String id, final PageContext context ) {
        super( id, context );

        // TODO: localize (alt attributes)

        Link elementaryLink = getNavMenu().getLocationByKey( "elementary-school" ).getLink( "elementary-school-link", context, getPhetCycle() );
        elementaryLink.add( new StaticImage( "elementary-image", Images.BY_LEVEL_ELEMENTARY_SCHOOL, null ) );
        add( elementaryLink );

        Link middleLink = getNavMenu().getLocationByKey( "middle-school" ).getLink( "middle-school-link", context, getPhetCycle() );
        middleLink.add( new StaticImage( "middle-image", Images.BY_LEVEL_MIDDLE_SCHOOL, null ) );
        add( middleLink );

        Link highLink = getNavMenu().getLocationByKey( "high-school" ).getLink( "high-school-link", context, getPhetCycle() );
        highLink.add( new StaticImage( "high-image", Images.BY_LEVEL_HIGH_SCHOOL, null ) );
        add( highLink );

        Link universityLink = getNavMenu().getLocationByKey( "university" ).getLink( "university-link", context, getPhetCycle() );
        universityLink.add( new StaticImage( "university-image", Images.BY_LEVEL_UNIVERSITY, null ) );
        add( universityLink );
    }

}