// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.content.simulations;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;

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

        setContentWidth( 1120 );

        add( new HTML5Panel( "html-panel", getPageContext() ) );

        NavLocation html5location = getNavMenu().getLocationByKey( "html5" );

        initializeLocation( html5location );

        setTitle( StringUtils.messageFormat( getLocalizer().getString( "simulationDisplay.title", this ), new Object[] {
                getPhetLocalizer().getString( html5location.getLocalizationKey(), this )
        } ) );

        this.getPhetCycle().setMinutesToCache( 15 );
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        // WARNING: don't change without also changing the old URL redirection
        mapper.addMap( "^simulations/category/html$", HTML5Page.class );
    }

    public static RawLinkable getLinker() {
        // WARNING: don't change without also changing the old URL redirection
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return "simulations/category/html";
            }
        };
    }

}