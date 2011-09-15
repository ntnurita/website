// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.content.media;

import org.apache.wicket.PageParameters;

import edu.colorado.phet.website.templates.PhetPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class TechAwardPage extends PhetPage {
    public TechAwardPage( PageParameters parameters ) {
        super( parameters );

        setTitle( getPhetLocalizer().getString( "award.techAward2011.title", this ) );

        add( new TechAwardPanel( "panel", getPageContext() ) );
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        mapper.addMap( "^media/tech-award-2011$", TechAwardPage.class );
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            @Override
            public String getSubUrl( PageContext context ) {
                return "media/tech-award-2011";
            }
        };
    }
}