/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.panels.sponsor;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractHeaderContributor;
import org.apache.wicket.markup.html.basic.Label;

import edu.colorado.phet.website.cache.EventDependency;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.RawBodyLabel;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.data.util.HibernateEventListener;
import edu.colorado.phet.website.data.util.IChangeListener;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.wicket.WicketUtils;

/**
 * Shows a sponsor highlight on the home page
 * <p/>
 * NOTE: I apologize to whoever will have to maintain this part. Wicket has a very weird way of having to render
 * components into strings, and this is combined with piling those into a JS string which will be processed.
 */
public class FeaturedSponsorPanel extends PhetPanel {
    public static final String HOME_SPONSOR_STYLE = "border: 1px solid #aaa; background-color: #fff;";

    private static final Logger logger = Logger.getLogger( FeaturedSponsorPanel.class.getName() );
    private PageContext context;
    private boolean visible = true; // visibility tag. when we need to duplicate this component and re-render to get renderings for each panel, the duplicates will have visible = false

    private AbstractHeaderContributor header;

    public FeaturedSponsorPanel( String id, final Sponsor sponsor, final PageContext context ) {
        super( id, context );

        this.context = context;

        if ( sponsor.getLogoNeedsSubtitle() ) {
            add( new Label( "featured-sponsor-name", sponsor.getFullName() ) );
        }
        else {
            add( new InvisibleComponent( "featured-sponsor-name" ) );
        }

        add( Sponsor.createSponsorLogoPanel( "featured-sponsor-panel", sponsor, context, HOME_SPONSOR_STYLE, Sponsor.SponsorContext.HOME ) );

        addDependency( new EventDependency() {
            private IChangeListener stringListener;

            @Override
            protected void addListeners() {
                stringListener = createTranslationChangeInvalidator( context.getLocale() );
                HibernateEventListener.addListener( TranslatedString.class, stringListener );
            }

            @Override
            protected void removeListeners() {
                HibernateEventListener.removeListener( TranslatedString.class, stringListener );
            }
        } );
    }

    public FeaturedSponsorPanel( String id, final Sponsor sponsor, final PageContext context, boolean visible ) {
        this( id, sponsor, context );
        this.visible = visible;
        setRenderBodyOnly( true ); // don't render the dd
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        if ( visible && header == null ) {
            StringBuilder builder = new StringBuilder();
            double totalWeight = 0;
            for ( Sponsor sponsor : Sponsor.getHomeSponsors() ) {
                if ( totalWeight > 0 ) {
                    builder.append( "," );
                }
                String componentString = WicketUtils.renderToString( this, new FeaturedSponsorPanel( this.getId(), sponsor, context, false ) );
                builder.append( "{weight:" + sponsor.getHomeWeight() + ",html:\"" + componentString.replace( '\n', ' ' ).replace( "\"", "\\\"" ) + "\"}" );
                totalWeight += sponsor.getHomeWeight();
            }
            // if someone gets an X S S attack through here, they've done their research.
            String listing = "<script type=\"text/javascript\">\n/* <![CDATA[ */\nvar phetHomeSponsors = {totalWeight:" + totalWeight + ",sponsors:[" + builder.toString() + "]};\n/* ]]> */\n</script>";
            header = WicketUtils.createStringHeaderContributor( listing );
            add( header );
        }
    }
}
