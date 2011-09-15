// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.content.media;

import org.apache.wicket.PageParameters;

import edu.colorado.phet.website.panels.SocialBookmarkPanel;
import edu.colorado.phet.website.templates.PhetPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

/**
 * Shows linked thumbnails of images that are high-resolution and should be used by the press.
 */
public class MediaImagesPage extends PhetPage {
    public MediaImagesPage( PageParameters parameters ) {
        super( parameters );

        String title = getPhetLocalizer().getString( "media.images.title", this );
        setTitle( title );

        add( new MediaImagesPanel( "panel", getPageContext() ) );

        add( new SocialBookmarkPanel( "social-bookmark-panel", getPageContext(), getFullPath(), title ) );
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        mapper.addMap( "^media/images$", MediaImagesPage.class );
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            @Override
            public String getSubUrl( PageContext context ) {
                return "media/images";
            }
        };
    }
}