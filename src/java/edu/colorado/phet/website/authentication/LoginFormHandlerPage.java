// Copyright 2002-2015, University of Colorado

package edu.colorado.phet.website.authentication;


import org.apache.wicket.RedirectToUrlException;
import org.apache.wicket.Request;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;

import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.StringUtils;

public class LoginFormHandlerPage extends WebPage {
    public LoginFormHandlerPage( PageParameters parameters ) {
        Request req = getRequest();


        String username = req.getParameter( "username" );
        String password = req.getParameter( "password" );
        String destination = req.getParameter( "destination" );

        System.out.println( username + " " + password + " " + destination );

        if ( !PhetSession.get().signIn( (PhetRequestCycle) getRequestCycle(), username, password ) ) {
            String url = StringUtils.makeUrlAbsolute( destination );
            url += "?login-failed";
            throw new RedirectToUrlException( url );
        }

        if ( destination == null || destination.isEmpty() ) {
            destination = "/";
        }

        throw new RedirectToUrlException( StringUtils.makeUrlHTTPS( destination ) );

//        if ( loginSuccessful( username, password ) ) {
//            if ( !continueToOriginalDestination() ) { ; }
//            {
//                setResponsePage( AccountPage.class );
//            }
//        }
//        else {
//            getSession().error( "login failed" );
//            // on failure send user to our regular login page
//            setResponsePage( SignInPage.class );
//        }
    }
}