/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;

import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.authentication.SignInPage;

public class TranslationSubmittedPage extends TranslationPage {

    private static final Logger logger = Logger.getLogger( TranslationSubmittedPage.class.getName() );

    public TranslationSubmittedPage( PageParameters parameters ) {
        super( parameters );

        add( TranslationMainPage.getLinker().getLink( "return", getPageContext(), getPhetCycle() ) );
    }
}
