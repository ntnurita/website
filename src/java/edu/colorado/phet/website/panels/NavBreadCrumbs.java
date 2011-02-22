/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.panels;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.menu.NavLocation;
import edu.colorado.phet.website.util.PageContext;

/**
 * Displays breadcrumb navigation on the top of the content area
 */
public class NavBreadCrumbs extends PhetPanel {
    public NavBreadCrumbs( String id, final PageContext context, NavLocation location ) {
        super( id, context );

        NavLocation base = null;
        List<NavLocation> locations = new LinkedList<NavLocation>();

        for ( NavLocation loc = location; loc != null; loc = loc.getParent() ) {
            if ( loc.getParent() != null ) {
                locations.add( 0, loc );
            }
            else {
                base = loc;
            }
        }

        if ( base == null ) {
            throw new RuntimeException( "BreadCrumbs failure!" );
        }
        else {
            Link baseLink = base.getLink( "base-link", context, getPhetCycle() );
            baseLink.add( new LocalizedText( "base-label", base.getBreadcrumbLocalizationKey() ) );
            add( baseLink );

            ListView listView = new ListView<NavLocation>( "more-crumbs", locations ) {
                protected void populateItem( ListItem<NavLocation> item ) {
                    NavLocation location = item.getModelObject();
                    Link link = location.getLink( "crumb-link", context, getPhetCycle() );
                    link.add( new LocalizedText( "crumb-label", location.getBreadcrumbLocalizationKey() ) );
                    item.add( link );
                }
            };
            add( listView );
        }
    }
}
