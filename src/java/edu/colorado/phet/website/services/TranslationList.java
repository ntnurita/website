// Copyright 2002-2012, University of Colorado

package edu.colorado.phet.website.services;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.common.phetcommon.util.function.Function1;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.components.RawBodyLabel;

import static edu.colorado.phet.common.phetcommon.util.FunctionalUtils.mkString;

/**
 * Returns a simple list of locales for all of the visible translations, separated by commas (no spaces)
 */
public class TranslationList extends WebPage {
    public TranslationList( PageParameters parameters ) {
        super( parameters );

        List<Locale> translations = PhetWicketApplication.get().getAllVisibleTranslationLocales();

        add( new RawBodyLabel( "string", mkString( translations, new Function1<Locale, String>() {
            public String apply( Locale locale ) {
                return LocaleUtils.localeToString( locale );
            }
        }, "," ) ) );
    }
}
