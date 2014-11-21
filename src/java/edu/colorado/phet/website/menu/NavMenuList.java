/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.menu;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.ResourceModel;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.components.VisListView;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.attributes.ClassAppender;
import edu.colorado.phet.website.util.HtmlUtils;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;

public class NavMenuList extends PhetPanel {

    private static final Logger logger = Logger.getLogger( NavMenuList.class.getName() );

    public NavMenuList( String id, final PageContext context, List<NavLocation> locations, final Collection<NavLocation> currentLocations, final int level ) {
        super( id, context );

        add( new VisListView<NavLocation>( "items", locations ) {
            protected void populateItem( ListItem item ) {
                NavLocation location = (NavLocation) item.getModel().getObject();
                Link link = location.getLink( "link", context, (PhetRequestCycle) getRequestCycle() );
                link.setMarkupId( "nav-location-" + HtmlUtils.sanitizeId( location.getLocalizationKey() ) );
                link.setOutputMarkupId( true );

                RawLabel label = new RawLabel( "link-label", new ResourceModel( location.getLocalizationKey() ) );

                boolean open = false;

                if ( currentLocations != null ) {
                    for ( NavLocation currentLocation : currentLocations ) {
                        if ( currentLocation == null ) {
                            logger.warn( "currentLocation is null in NavMenuList: " + item.getModelObject().toString() );
                            continue;
                        }
                        if ( !open ) {
                            open = currentLocation.isUnderLocation( location );
                        }
                        if ( currentLocation.getBaseKey().equals( location.getKey() ) || currentLocation.getKey().equals( location.getKey() ) ) {
                            label.add( new ClassAppender( "selected" ) );
                        }
                    }
                }

                // adds class so we can remove these for things like the installer and whatnot in CSS
                if ( location.isUnderLocationKey( "get-phet" ) ) {
                    label.add( new ClassAppender( "get-phet-item" ) );

                    // if this is an offline installer, just get rid of it
                    if ( PhetRequestCycle.get().isOfflineInstaller() ) {
                        item.setVisible( false );
                    }
                }
                if ( location.isUnderLocationKey( "teacherIdeas" ) ) {
                    label.add( new ClassAppender( "teacher-ideas-item" ) );
                    if ( !DistributionHandler.displayContributions( getPhetCycle() ) ) {
                        item.setVisible( false );
                    }
                }
                if ( location.isUnderLocationKey( "forTranslators.website" ) ) {
                    if ( !DistributionHandler.showAnyWebsiteTranslations( getPhetCycle() ) ) {
                        item.setVisible( false );
                    }
                }

                link.add( label );

                item.add( link );

                if ( location.getChildren().isEmpty() || !open ) {
                    Label placeholder = new Label( "children", "BOO" );
                    placeholder.setVisible( false );
                    item.add( placeholder );
                }
                else {
                    NavMenuList children = new NavMenuList( "children", context, location.getVisibleChildren( currentLocations ), currentLocations, level + 1 );
                    children.setRenderBodyOnly( true );
                    item.add( children );
                }

                link.add( new ClassAppender( "nav" + level ) );

            }
        } );

        //add( HeaderContributor.forCss( CSS.NAV_MENU ) );
    }
}
