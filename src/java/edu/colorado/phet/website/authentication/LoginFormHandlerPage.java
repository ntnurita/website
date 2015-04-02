// Copyright 2002-2015, University of Colorado

package edu.colorado.phet.website.authentication;


import org.apache.wicket.PageParameters;
import org.apache.wicket.RedirectToUrlException;
import org.apache.wicket.Request;
import org.apache.wicket.markup.html.WebPage;

import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.StringUtils;

public class LoginFormHandlerPage extends WebPage {
    public LoginFormHandlerPage( PageParameters parameters ) {

        Request req = getRequest();

        String username = req.getParameter( "username" );
        String password = req.getParameter( "password" );

        // destination url, the url of the link that the user clicked that requires login
        String destination = req.getParameter( "destination" );

        // source url, the url of the page the user is currently on. This is present only if the user clicks the login button,
        // so we know which page to send them back to after they are logged in.
        String source = req.getParameter( "source" );

        // if source is present, send the back to the source url instead of the destination
        if ( source == null || source.isEmpty() ) {
            source = "/";
        }
        else {
            destination = source;
        }

        if ( !PhetSession.get().signIn( (PhetRequestCycle) getRequestCycle(), username, password ) ) {
            String url = StringUtils.makeUrlAbsolute( source );
            url += "?login-failed";
            throw new RedirectToUrlException( url );
        }

        if ( destination == null || destination.isEmpty() ) {
            destination = "/";
        }

        throw new RedirectToUrlException( StringUtils.makeUrlHTTPS( destination ) );
    }
}