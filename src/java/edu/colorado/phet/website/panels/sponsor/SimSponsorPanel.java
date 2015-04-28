/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.panels.sponsor;

import org.apache.log4j.Logger;
import org.apache.wicket.behavior.AbstractHeaderContributor;

import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.wicket.WicketUtils;

/**
 * Shows a sponsor highlight on the simulation pages
 * <p/>
 * WARNING: dragons be here!
 * TODO: double-check all of our assumptions, and run many tests. we are now nesting fake responses in creating
 * component strings during a cachable-panel construction
 */
public class SimSponsorPanel extends PhetPanel {
    public static final String SIM_SPONSOR_STYLE = "padding-top: 5px; border: none; max-height: 95px;";

    private static final Logger logger = Logger.getLogger( FeaturedSponsorPanel.class.getName() );
    private PageContext context;
    private boolean visible = true;

    private AbstractHeaderContributor header;

    public SimSponsorPanel( String id, final PageContext context, Sponsor sponsor ) {
        super( id, context );

        this.context = context;

        add( Sponsor.createSponsorLogoPanel( "sim-sponsor-panel", sponsor, context, SIM_SPONSOR_STYLE, Sponsor.SponsorContext.SIM ) );

        if ( getMyLocale().equals( WebsiteConstants.ENGLISH ) && sponsor.getNeedsArticle() ) {
            add( new LocalizedText( "before-text", "sponsors.sim.supportedByThe" ) );
        }
        else {
            add( new LocalizedText( "before-text", "sponsors.sim.supportedBy" ) );
        }
        if ( sponsor instanceof Sponsor.YouSponsor ) {
            add( new InvisibleComponent( "after-text" ) ); // hide that part when we have the You sponsor!
        }
        else {
            add( new LocalizedText( "after-text", "sponsors.sim.andEducators" ) );
        }
    }

    public SimSponsorPanel( String id, final PageContext context, Sponsor sponsor, boolean visible ) {
        this( id, context, sponsor );
        this.visible = visible;
        setRenderBodyOnly( true );
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        if ( visible && header == null ) {
            StringBuilder builder = new StringBuilder();
            double totalWeight = 0;
            for ( Sponsor sponsor : Sponsor.getSimSponsors() ) {
                if ( totalWeight > 0 ) {
                    builder.append( "," );
                }
                String componentString = WicketUtils.renderToString( this, new SimSponsorPanel( this.getId(), context, sponsor, false ) );
                builder.append( "{weight:" + sponsor.getSimWeight() + ",html:\"" + componentString.replace( '\n', ' ' ).replace( '\r', ' ' ).replace( '\t', ' ' ).replace( "\"", "\\\"" ) + "\"}" );
                totalWeight += sponsor.getSimWeight();
            }
            // if someone gets an X S S attack through here, they've done their research.
            String listing = "<script type=\"text/javascript\">\n/* <![CDATA[ */\nvar phetSimSponsors = {totalWeight:" + totalWeight + ",sponsors:[" + builder.toString() + "]};\n/* ]]> */\n</script>";
            header = WicketUtils.createStringHeaderContributor( listing );
            add( header );
        }
    }

}