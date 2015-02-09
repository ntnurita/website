// Copyright 2002-2015, University of Colorado

package edu.colorado.phet.website.webgl;

import org.apache.wicket.PageParameters;

import edu.colorado.phet.website.templates.PhetPage;
import edu.colorado.phet.website.util.PhetUrlMapper;

/**
 * Page with information about how to enable WebGL in different browsers
 */
public class WebglDisabledPage extends PhetPage {

    public WebglDisabledPage( PageParameters parameters ) {
        super( parameters );

        setTitle( "Enabling WebGL" );
        add( new WebglDisabledPanel( "webgl-disabled-panel", getPageContext() ) );

        this.getPhetCycle().setMinutesToCache( 15 );
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        mapper.addMap( "^webgl-disabled-page$", WebglDisabledPage.class );
    }
}
