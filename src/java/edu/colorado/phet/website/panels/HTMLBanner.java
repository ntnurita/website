// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.panels;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.ResourceModel;

import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.content.DonatePanel;
import edu.colorado.phet.website.content.simulations.HTML5Page;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.WebImage;

public class HTMLBanner extends PhetPanel {

    private static final Logger logger = Logger.getLogger( HTMLBanner.class.getName() );

    public HTMLBanner( String id, final PageContext context ) {
        super( id, context );

        add( DonatePanel.getLinker().getLink( "donate-link", context, getPhetCycle() ) );

        Link link = HTML5Page.getLinker().getLink( "html-banner-link", context, getPhetCycle() );
        add( link );
        link.add( new AttributeModifier( "title", true, new ResourceModel( "donation-banner.html5.tooltip" ) ) ); // tooltip
        link.add( new StaticImage( "html-banner-image", WebImage.get( Images.HTML5_LOGO_40 ), null ) );

        add( HTML5Page.getLinker().getLink( "html-banner-link-button", context, getPhetCycle() ) );
    }
}