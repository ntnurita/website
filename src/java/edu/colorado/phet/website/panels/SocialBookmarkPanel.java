// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import edu.colorado.phet.website.constants.SocialBookmarkService;
import edu.colorado.phet.website.util.PageContext;

/**
 * Displays a vertical list of social bookmarking service icons
 */
public class SocialBookmarkPanel extends PhetPanel {
    public SocialBookmarkPanel( String id, final PageContext context, final String bookmarkableUrl, final String bookmarkableTitle ) {
        super( id, context );

        add( new ListView<SocialBookmarkService>( "social-list", SocialBookmarkService.SERVICES ) {
            @Override
            protected void populateItem( ListItem<SocialBookmarkService> item ) {
                final SocialBookmarkService mark = item.getModelObject();
                Link link = mark.getLinker( bookmarkableUrl, bookmarkableTitle ).getLink( "link", context, getPhetCycle() );
                link.add( new AttributeModifier( "title", true, new ResourceModel( mark.getTooltipLocalizationKey() ) ) ); // tooltip
                item.add( link );
                link.add( new WebMarkupContainer( "icon" ) {{
                    add( new AttributeModifier( "style", true, new Model<String>( "display: block; width: 28px; height: 28px;" ) ) );
                    add( new AttributeModifier( "src", true, new Model<String>( mark.getIconPath() ) ) );
                }} );
                //link.add( new StaticImage( "icon", mark.getIconHandle(), null ) ); // for now, don't replace the alt attribute
            }
        } );
    }
}