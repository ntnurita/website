/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.authentication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RedirectToUrlException;

import edu.colorado.phet.website.authentication.panels.SignInPanel;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.AuthenticatedLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

/**
 * The page to send people to if they need to sign in. Specify a destination that they will be taken to after signing
 * in (even after registering)
 */
public class SignInPage extends PhetMenuPage {

    public static final String SIGN_IN_PATH = "sign-in";
    private static final Logger logger = Logger.getLogger( SignInPage.class.getName() );

    public SignInPage( PageParameters parameters ) {
        super( parameters );

        // redirect to the page with HTTPS if they have come using HTTP
        if ( !getPhetCycle().isOriginalSecure() ) {
            String url = SignInPage.getLinker( this.getFullPath() ).getRawUrl( getPageContext(), getPhetCycle() );
            String[] urlAndQuery = url.split( "\\?" );
            throw new RedirectToUrlException( urlAndQuery[0] + "?dest=/" );
        }

        String destination = null;

        if ( parameters != null && parameters.containsKey( "dest" ) ) {
            destination = parameters.getString( "dest" );

            // pass the url hash as a query parameter since it is usually not sent to the server
            if ( parameters.containsKey( "urlHash" ) ) {
                destination += '#' + parameters.getString( "urlHash" );
            }
        }

        setTitle( getLocalizer().getString( "signIn.title", this ) );

        add( new SignInPanel( "sign-in-panel", getPageContext(), destination ) );

        hideSocialBookmarkButtons();
    }

    /**
     * NOTE: Don't call if you're not in a request cycle
     *
     * @param destination Where to redirect the user to after they complete the sign-in
     * @return
     */
    public static RawLinkable getLinker( final String destination ) {
        return new AuthenticatedLinker() {
            @Override
            public String getSubUrl( PageContext context ) {
                try {
                    // always encode the destination because special characters might be hidden
                    String finalDestination = destination;
                    if ( finalDestination.equals( "/en/" ) ) {
                        finalDestination = "/";
                    }
                    return SIGN_IN_PATH + "?dest=" + URLEncoder.encode( finalDestination, "UTF-8" );
                }
                catch ( UnsupportedEncodingException e ) {
                    e.printStackTrace();
                    throw new RuntimeException( e );
                }
            }
        };
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        mapper.addMap( "^" + SIGN_IN_PATH + "$", SignInPage.class );
    }

}
