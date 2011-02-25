/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.panels.sponsor;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

/**
 * Shows a sponsor panel that indicates that the user (You) could be a sponsor!
 */
public class YouSponsorPanel extends PhetPanel {
    public YouSponsorPanel( String id, PageContext context, Sponsor.SponsorContext sponsorContext ) {
        super( id, context );

        final int youFontSize = ( sponsorContext == Sponsor.SponsorContext.HOME ) ? 26 : 20;

        add( new WebMarkupContainer( "you" ) {{
            add( new AttributeModifier( "style", true, new Model<String>( "font-size: " + youFontSize + "px;" ) ) ); // standard 26 px.
        }} );
    }
}
