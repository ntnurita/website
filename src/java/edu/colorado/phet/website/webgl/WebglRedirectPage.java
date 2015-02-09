// Copyright 2002-2015, University of Colorado

package edu.colorado.phet.website.webgl;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RedirectToUrlException;

import edu.colorado.phet.website.templates.PhetPage;

/**
 * This page just redirects to WebglDisabledPage with the correct locale take from the query string
 */
public class WebglRedirectPage extends PhetPage {

    public WebglRedirectPage( PageParameters parameters ) {
        super( parameters );

        String myLocale = parameters.getString( "simLocale" );
        if ( myLocale == null ) {
            myLocale = "en";
        }

        throw new RedirectToUrlException( "/" + myLocale + "/webgl-disabled-page" );
    }
}
