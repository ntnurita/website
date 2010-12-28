/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.services;

import java.util.Locale;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.StringUtils;

/**
 * Allow the ability to pull strings out as a web service. Will allow requests like:
 * http://phet.colorado.edu/services/get-string?key=language.dir&locale=en
 * to serve back the XML data:
 * <string>ltr</string>
 */
public class StringGetter extends WebPage {
    public StringGetter( PageParameters parameters ) {
        super( parameters );

        if ( parameters.getString( "key" ) == null || parameters.getString( "locale" ) == null ) {
            showError();
            return;
        }

        try {

            String key = parameters.getString( "key" );
            Locale locale = LocaleUtils.stringToLocale( parameters.getString( "locale" ) );

            String value = StringUtils.getStringDirect( ( (PhetRequestCycle) getRequestCycle() ).getHibernateSession(), key, locale );
            if ( value == null ) {
                showError();
            }
            else {
                add( new InvisibleComponent( "error" ) );
                add( new Label( "string", value ) ); // this should escape it
            }
        }
        catch( Exception e ) {
            showError();
        }
    }

    public void showError() {
        add( new InvisibleComponent( "string" ) );
        add( new Label( "error", "" ) );
    }
}
