/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.panels;

import java.util.Random;

import org.apache.wicket.markup.html.basic.Label;

import edu.colorado.phet.website.components.RawLink;
import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.util.PageContext;

public class SimSponsorPanel extends PhetPanel {
    public SimSponsorPanel( String id, final PageContext context, Sponsor sponsor ) {
        super( id, context );

        RawLink link = new RawLink( "link", sponsor.getUrl() );
        add( link );
        link.add( new StaticImage( "image", sponsor.getImageUrl(), sponsor.getImageAlt() ) );

        // TODO: i18n
        add( new Label( "before-text", "PhET is supported by" + ( sponsor.needsArticle() ? " the" : "" ) ) );
        add( new Label( "after-text", "and educators like you." ) );
        add( new Label( "thanks", "Thanks!" ) );
    }

}