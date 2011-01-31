/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.hibernate.Session;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.cache.SimulationCache;
import edu.colorado.phet.website.components.RawBodyLabel;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.StringTrie;

/**
 * Handles autocompletion of search requests
 */
public class Autocomplete extends WebPage {

    private static final Logger logger = Logger.getLogger( Autocomplete.class.getName() );

    private static final HashMap<Locale, StringTrie> simulationNameTries = new HashMap<Locale, StringTrie>();

    public Autocomplete( PageParameters parameters ) {
        super( parameters );

        String query = parameters.getString( "q" );
        String localeString = parameters.getString( "l" );
        Locale locale = localeString == null ? PhetWicketApplication.getDefaultLocale() : LocaleUtils.stringToLocale( localeString );
        if ( !PhetWicketApplication.get().isVisibleLocale( locale ) ) {
            locale = PhetWicketApplication.getDefaultLocale();
        }
        logger.debug( query );

        StringTrie trie = getStringTrie( PhetRequestCycle.get().getHibernateSession(), locale );
        List<String> matches = trie.getStartingWith( query );
        StringBuilder buf = new StringBuilder();
        for ( String match : matches ) {
            buf.append( match ).append( "\n" );
        }

        add( new RawBodyLabel( "response", buf.toString() ) );
    }

    public static synchronized void invalidate() {
        simulationNameTries.clear();
    }

    private static synchronized StringTrie getStringTrie( Session session, Locale locale ) {
        StringTrie stringTrie = simulationNameTries.get( locale );
        if ( stringTrie == null ) {
            stringTrie = new StringTrie( locale );
            List<LocalizedSimulation> sims = new LinkedList<LocalizedSimulation>();
            SimulationCache.addSortedLocalizedSimulations( sims, session, locale );
            // TODO: add sorting?
            for ( LocalizedSimulation lsim : sims ) {
                stringTrie.add( lsim.getTitle() );
            }
            simulationNameTries.put( locale, stringTrie );
        }
        return stringTrie;
    }
}
