// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.content.media;

import org.apache.wicket.PageParameters;

import edu.colorado.phet.website.panels.SocialBookmarkPanel;
import edu.colorado.phet.website.templates.PhetPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class WorldPhotosPage extends PhetPage {
    public WorldPhotosPage( PageParameters parameters ) {
        super( parameters );

        String title = getPhetLocalizer().getString( "world-photos.title", this );
        setTitle( title );

        add( new WorldPhotosPanel( "panel", getPageContext() ) );

        add( new SocialBookmarkPanel( "social-bookmark-panel", getPageContext(), getFullPath(), title ) );
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        mapper.addMap( "^media/photos$", WorldPhotosPage.class );
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            @Override
            public String getSubUrl( PageContext context ) {
                return "media/photos";
            }
        };
    }
}