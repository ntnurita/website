/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.simulations;

import org.apache.wicket.markup.html.link.Link;

import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.data.Category;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

public class ByDevicePanel extends PhetPanel {

    public ByDevicePanel( String id, final PageContext context ) {
        super( id, context );

        // TODO: localize (alt attributes)

        Link tabletsLink = getNavMenu().getLocationByKey( Category.IPAD_TABLET ).getLink( "ipad-tablet-link", context, getPhetCycle() );
        tabletsLink.add( new StaticImage( "ipad-tablet-image", Images.BY_DEVICE_IPAD, null ) );
        add( tabletsLink );

        Link middleLink = getNavMenu().getLocationByKey( Category.CHROMEBOOK ).getLink( "chromebook-link", context, getPhetCycle() );
        middleLink.add( new StaticImage( "chromebook-image", Images.BY_DEVICE_CHROMEBOOK, null ) );
        add( middleLink );
    }

}