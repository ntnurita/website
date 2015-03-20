// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.content.simulations;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;

import edu.colorado.phet.website.content.NotFoundPage;
import edu.colorado.phet.website.menu.NavLocation;
import edu.colorado.phet.website.templates.PhetRegularPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class HTML5Page extends PhetRegularPage {

    private static final Logger logger = Logger.getLogger( HTML5Page.class.getName() );

    public HTML5Page( final PageParameters parameters ) {
        super( parameters );

        hideSocialBookmarkButtons();

        boolean showIndex = false;

        // we need to check the "query string" part to see if we will show in "index" mode
        if ( parameters.containsKey( "query-string" ) ) {
            logger.debug( "Query string: " + parameters.getString( "query-string" ) );
            if ( parameters.getString( "query-string" ).equals( "/index" ) ) {
                showIndex = true;
            }
            else {
                setResponsePage( NotFoundPage.class );
            }
        }
        else {
            logger.debug( "No query string" );
        }

        add( new HTML5Panel( "html-panel", getPageContext(), showIndex ) );

        NavLocation location = getNavMenu().getLocationByKey( "new" );

        initializeLocation( location );

        setTitle( StringUtils.messageFormat( getLocalizer().getString( "simulationDisplay.title", this ), new Object[] {
                getPhetLocalizer().getString( location.getLocalizationKey(), this )
        } ) );

        this.getPhetCycle().setMinutesToCache( 15 );
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        // WARNING: don't change without also changing the old URL redirection
        mapper.addMap( "^simulations/category/new$", HTML5Page.class );
        mapper.addMap( "^simulations/category/new(/index)?$", HTML5Page.class, new String[] { "query-string" } );
    }

    public static RawLinkable getLinker() {
        // WARNING: don't change without also changing the old URL redirection
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return "simulations/category/new";
            }
        };
    }

}