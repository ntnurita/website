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
import edu.colorado.phet.website.content.simulations.SimulationPage;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.util.DataTrie;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;

/**
 * Handles autocompletion of search requests
 */
public class Autocomplete extends WebPage {

    private static final Logger logger = Logger.getLogger( Autocomplete.class.getName() );

    private static final HashMap<Locale, DataTrie<LocalizedSimulation>> simulationNameTries = new HashMap<Locale, DataTrie<LocalizedSimulation>>();

    public Autocomplete( PageParameters parameters ) {
        super( parameters );

        String query = parameters.getString( "q" );
        String localeString = parameters.getString( "l" );
        Locale locale = localeString == null ? PhetWicketApplication.getDefaultLocale() : LocaleUtils.stringToLocale( localeString );
        if ( !PhetWicketApplication.get().isVisibleLocale( locale ) ) {
            locale = PhetWicketApplication.getDefaultLocale();
        }
        logger.debug( query );

        DataTrie<LocalizedSimulation> trie = getStringTrie( PhetRequestCycle.get().getHibernateSession(), locale );
        List<LocalizedSimulation> matches = trie.getStartingWith( query );
        StringBuilder buf = new StringBuilder();
        PageContext pageContext = new PageContext( PageContext.getStandardPrefix( locale ), "autocomplete", locale );
        for ( LocalizedSimulation match : matches ) {
            String url = SimulationPage.getLinker( match ).getRawUrl( pageContext, PhetRequestCycle.get() );
            buf.append( match.getTitle() ).append( "|sim|" ).append( url ).append( "\n" );
        }

        add( new RawBodyLabel( "response", buf.toString() ) );
    }

    public static synchronized void invalidate() {
        simulationNameTries.clear();
    }

    private static synchronized DataTrie<LocalizedSimulation> getStringTrie( Session session, Locale locale ) {
        DataTrie<LocalizedSimulation> trie = simulationNameTries.get( locale );
        if ( trie == null ) {
            trie = new DataTrie<LocalizedSimulation>( locale ) {
                @Override
                protected String toString( LocalizedSimulation simulation ) {
                    return simulation.getTitle();
                }
            };
            List<LocalizedSimulation> sims = new LinkedList<LocalizedSimulation>();
            SimulationCache.addSortedLocalizedSimulations( sims, session, locale );
            // TODO: add sorting?
            for ( LocalizedSimulation lsim : sims ) {
                trie.add( lsim );
            }
            simulationNameTries.put( locale, trie );
        }
        return trie;
    }
}
