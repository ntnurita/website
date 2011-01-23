/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.services;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;

import edu.colorado.phet.website.components.RawBodyLabel;

/**
 * Handles autocompletion of search requests
 */
public class Autocomplete extends WebPage {

    private static final Logger logger = Logger.getLogger( Autocomplete.class.getName() );

    public Autocomplete( PageParameters parameters ) {
        super( parameters );

        String query = parameters.getString( "q" );
        logger.info( query );

        add( new RawBodyLabel( "response", query + "aaa" + "\n" + query + "bbb" ) );
    }
}
