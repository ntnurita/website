// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.panels;

import org.apache.log4j.Logger;

import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.WebImage;

public class HTMLBanner extends PhetPanel {

    private static final Logger logger = Logger.getLogger( DonationBannerRegularPanel.class.getName() );

    public HTMLBanner( String id, final PageContext context ) {
        super( id, context );

        add( new StaticImage( "html-banner-image", WebImage.get( Images.HTML5_LOGO_80 ), null ) );
    }
}