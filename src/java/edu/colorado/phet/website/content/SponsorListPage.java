/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.content;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import edu.colorado.phet.website.panels.sponsor.FeaturedSponsorPanel;
import edu.colorado.phet.website.panels.sponsor.SimSponsorPanel;
import edu.colorado.phet.website.panels.sponsor.Sponsor;
import edu.colorado.phet.website.templates.PhetPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class SponsorListPage extends PhetPage {
    public SponsorListPage( PageParameters parameters ) {
        super( parameters );

        setTitle( "PhET sponsor list" ); // TODO: i18n?

        add( new ListView<Sponsor>( "home-sponsor", Sponsor.getHomeSponsors() ) {
            @Override
            protected void populateItem( ListItem<Sponsor> item ) {
                Sponsor sponsor = item.getModelObject();
                item.add( new FeaturedSponsorPanel( "panel", sponsor, getPageContext() ) );
            }
        } );

        add( new ListView<Sponsor>( "sim-sponsor", Sponsor.getSimSponsors() ) {
            @Override
            protected void populateItem( ListItem<Sponsor> item ) {
                Sponsor sponsor = item.getModelObject();
                item.add( new SimSponsorPanel( "panel", getPageContext(), sponsor ) );
            }
        } );
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        mapper.addMap( "^sponsor-list$", SponsorListPage.class );
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            @Override
            public String getSubUrl( PageContext context ) {
                return "sponsor-list";
            }
        };
    }
}