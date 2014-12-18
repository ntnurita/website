// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.panels;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import edu.colorado.phet.website.constants.SocialBookmarkService;
import edu.colorado.phet.website.newsletter.InitialSubscribePage;
import edu.colorado.phet.website.util.PageContext;

/**
 * Displays a vertical list of social bookmarking service icons
 */
public class SocialBookmarkPanel extends PhetPanel {
    public SocialBookmarkPanel( String id, final PageContext context, final String bookmarkableUrl, final String bookmarkableTitle ) {
        super( id, context );

        final boolean onHomePage = ( bookmarkableUrl.equals( "" ) );
        List<SocialBookmarkService> services = ( onHomePage ) ? SocialBookmarkService.HOMEPAGE_SERVICES : SocialBookmarkService.SERVICES;

        add( new ListView<SocialBookmarkService>( "social-list", services ) {
            @Override
            protected void populateItem( ListItem<SocialBookmarkService> item ) {
                final SocialBookmarkService mark = item.getModelObject();
                Link link;
                if ( mark.getName().equals( "newsletter" ) ) {
                    link = InitialSubscribePage.getLinker().getLink( "link", context, getPhetCycle() );
                } else {
                    link = mark.getLinker( bookmarkableUrl, bookmarkableTitle ).getLink( "link", context, getPhetCycle() );
                }
                String toolTipKey = ( onHomePage ) ? mark.getHomePageTooltipLocalizationKey() : mark.getTooltipLocalizationKey();
                link.add( new AttributeModifier( "title", true, new ResourceModel( toolTipKey ) ) ); // tooltip

                // phetLinks (blog and newsletter) should not open in a new tab, but other links should
                if ( !mark.isPhetLink() ) {
                    link.add( new AttributeModifier( "rel", true, new Model<String>( "external nofollow" ) ) );
                }
                item.add( link );
                link.add( new WebMarkupContainer( "icon" ) {{
                    add( new AttributeModifier( "style", true, new Model<String>( "display: block; width: 28px; height: 28px; border: none;" ) ) );
                    add( new AttributeModifier( "src", true, new Model<String>( mark.getIconPath() ) ) );
                    add( new AttributeModifier( "alt", true, new Model<String>( " " ) ) ); // not needed since link title is read by screen readers
                }} );
                //link.add( new StaticImage( "icon", mark.getIconHandle(), null ) ); // for now, don't replace the alt attribute
            }
        } );
    }
}