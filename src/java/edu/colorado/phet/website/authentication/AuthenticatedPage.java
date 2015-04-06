/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.authentication;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;

import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.menu.NavLocation;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.templates.PhetPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlStrategy;
import edu.colorado.phet.website.util.links.AbstractLinker;

/**
 * Class and methods for requiring authentication (user log in) before viewing a page
 */
public class AuthenticatedPage extends PhetPage {

    private static final Logger logger = Logger.getLogger( AuthenticatedPage.class.getName() );

    public AuthenticatedPage( PageParameters parameters ) {
        super( parameters );

        if ( !PhetSession.get().isSignedIn() ) {
            throwRedirectException();
        }
    }

    public AuthenticatedPage( PageParameters parameters, AbstractLinker linker ) {
        super( parameters );

        if ( !PhetSession.get().isSignedIn() ) {
            throwRedirectException( linker.getHttpsUrl( getPageContext(), getPhetCycle() ) );
        }
    }

    /**
     * Shortcut for getting the user
     *
     * @return The logged-in phet user
     */
    public PhetUser getUser() {
        return PhetSession.get().getUser();
    }

    /**
     * Check whether the user is signed in. If not, redirect to the sign-in page.
     *
     * @param context The page context of the current page
     */
    public static void checkSignedIn( PageContext context ) {
        if ( !PhetSession.get().isSignedIn() ) {
            PageParameters params = new PageParameters();
            PhetUrlStrategy.setPageParamToStandard( params, context.getLocale(), SignInPage.SIGN_IN_PATH );

            throw new RestartResponseAtInterceptPageException( new SignInPage( params ) );
        }
    }

    /**
     * Check whether the user is signed in. If not, redirect to the sign-in page with the following nav locations for
     * the navigation menu
     *
     * @param navLocations Navigation locations for the navigation menu
     * @param context      The page context of the current page
     */
    public static void checkSignedIn( Collection<NavLocation> navLocations, PageContext context ) {
        if ( !PhetSession.get().isSignedIn() ) {
            PageParameters params = new PageParameters();
            params.put( PhetMenuPage.NAV_LOCATIONS, navLocations );
            PhetUrlStrategy.setPageParamToStandard( params, context.getLocale(), SignInPage.SIGN_IN_PATH );

            throw new RestartResponseAtInterceptPageException( new SignInPage( params ) );
        }
    }

}
