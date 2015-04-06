/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation;

import org.apache.wicket.PageParameters;

import edu.colorado.phet.website.authentication.AuthenticatedPage;
import edu.colorado.phet.website.util.links.AbstractLinker;

public class TranslationPage extends AuthenticatedPage {
    public TranslationPage( PageParameters parameters ) {
        super( parameters );

        setTitle( getLocalizer().getString( "forTranslators.website.title", this ) );
    }

    public TranslationPage( PageParameters parameters, AbstractLinker linker ) {
        super( parameters, linker );

        setTitle( getLocalizer().getString( "forTranslators.website.title", this ) );
    }
}
