/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.panels;

import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.util.PageContext;

public class SimSponsorPanel extends PhetPanel {
    public static final String SIM_SPONSOR_STYLE = "padding-top: 5px; border: none;";

    public SimSponsorPanel( String id, final PageContext context, Sponsor sponsor ) {
        super( id, context );

        add( Sponsor.createSponsorLogoPanel( "sim-sponsor-panel", sponsor, context, SIM_SPONSOR_STYLE ) );

        if ( getMyLocale().equals( PhetWicketApplication.getDefaultLocale() ) && sponsor.getNeedsArticle() ) {
            add( new LocalizedText( "before-text", "sponsors.sim.supportedByThe" ) );
        }
        else {
            add( new LocalizedText( "before-text", "sponsors.sim.supportedBy" ) );
        }
        add( new LocalizedText( "after-text", "sponsors.sim.andEducators" ) );
        add( new LocalizedText( "thanks", "sponsors.sim.thanks" ) );
    }

}