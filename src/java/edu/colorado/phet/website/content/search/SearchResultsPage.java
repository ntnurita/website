/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;

import edu.colorado.phet.website.templates.PhetRegularPage;
import edu.colorado.phet.website.util.HtmlUtils;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class SearchResultsPage extends PhetRegularPage {

    private static final Logger logger = Logger.getLogger( SearchResultsPage.class.getName() );

    public SearchResultsPage( PageParameters parameters ) {
        super( parameters );

        String query = parameters.getString( "q" );

        initializeLocation( getNavMenu().getLocationByKey( "search.results" ) );

        if ( query != null ) {
            setTitle( StringUtils.messageFormat( getPhetLocalizer().getString( "search.title", this ), HtmlUtils.encode( query ) ) );
        }
        else {
            setTitle( StringUtils.messageFormat( getPhetLocalizer().getString( "search.title", this ), "-" ) );
        }

        add( new SearchResultsPanel( "search-results-panel", getPageContext(), query ) );

        this.getPhetCycle().setMinutesToCache( 15 );
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        mapper.addMap( "^search$", SearchResultsPage.class, new String[] { null, "q" } );
    }

    public static RawLinkable getLinker( final String query ) {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                if ( query != null ) {
                    try {
                        return "search?q=" + URLEncoder.encode( query, "UTF-8" );
                    }
                    catch ( UnsupportedEncodingException e ) {
                        e.printStackTrace();
                        logger.error( e );
                        return "";
                    }
                }
                else {
                    return "search";
                }
            }
        };
    }

    @Override
    public String getStyle( String key ) {
        if ( key.equals( "style.metaRobots" ) ) {
            return "noindex"; // possible to change for the future
        }
        return super.getStyle( key );
    }
}