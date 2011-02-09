/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.panels;

import org.apache.wicket.markup.html.basic.Label;

import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.util.PageContext;

public class SimSponsorPanel extends PhetPanel {
    public SimSponsorPanel( String id, final PageContext context, Sponsor sponsor ) {
        super( id, context );

//        RawLink link = new RawLink( "link", sponsor.getUrl() );
//        add( link );
//        link.add( sponsor.createLogoComponent( "image", "padding-top: 5px; border: none;" ) );

        add( Sponsor.createSponsorLogoPanel( "sim-sponsor-panel", sponsor, context, "padding-top: 5px; border: none;" ) );

        // TODO: i18n
        if ( getMyLocale().equals( PhetWicketApplication.getDefaultLocale() ) && sponsor.getNeedsArticle() ) {
            add( new LocalizedText( "before-text", "sponsors.sim.supportedByThe" ) );
        } else {
            add( new LocalizedText( "before-text", "sponsors.sim.supportedBy" ) );
        }
        add( new LocalizedText( "after-text", "sponsors.sim.andEducators" ) );
        add( new LocalizedText( "thanks", "sponsors.sim.thanks" ) );
    }

}