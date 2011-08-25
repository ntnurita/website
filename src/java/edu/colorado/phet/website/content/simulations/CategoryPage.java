/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.simulations;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;

import edu.colorado.phet.website.cache.SimplePanelCacheEntry;
import edu.colorado.phet.website.content.NotFoundPage;
import edu.colorado.phet.website.data.Category;
import edu.colorado.phet.website.menu.NavLocation;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.panels.simulation.SimulationListViewPanel;
import edu.colorado.phet.website.templates.PhetRegularPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

/**
 * Displays thumbnails of simulations within a single category, or an alphabetical list of simulations, depending on
 * whether in index mode.
 */
public class CategoryPage extends PhetRegularPage {

    private static final Logger logger = Logger.getLogger( CategoryPage.class.getName() );
    private String canonicalUrl;

    public CategoryPage( final PageParameters parameters ) {
        super( parameters );

        boolean showIndex = false;

        // we need to check the "query string" part to see if we will show in "index" mode
        if ( parameters.containsKey( "query-string" ) ) {
            logger.debug( "Query string: " + parameters.getString( "query-string" ) );
            if ( parameters.getString( "query-string" ).equals( "/index" ) ) {
                showIndex = true;
            }
            else {
                setResponsePage( NotFoundPage.class );
            }
        }
        else {
            logger.debug( "No query string" );
        }

        final boolean index = showIndex;
        final String categories = parameters.containsKey( "categories" ) ? parameters.getString( "categories" ) : null;

        // set canonical URL
        if ( categories == null ) {
            // no category, so we are listing all sims.
            canonicalUrl = StringUtils.makeUrlAbsolute( getAllSimsLinker().getRawUrl( getPageContext(), getPhetCycle() ) );
        }
        else {
            canonicalUrl = StringUtils.makeUrlAbsolute( getLinker( categories ).getRawUrl( getPageContext(), getPhetCycle() ) );
        }

        // create a cacheable panel for the simulation list
        PhetPanel viewPanel = new SimplePanelCacheEntry( SimulationListViewPanel.class, this.getClass(), getPageContext().getLocale(), getMyPath(), getPhetCycle() ) {
            public PhetPanel constructPanel( String id, PageContext context ) {
                return new SimulationListViewPanel(
                        id,
                        CategoryPage.this.getMyPath(),
                        categories,
                        index,
                        context
                );
            }
        }.instantiate( "view-panel", getPageContext(), getPhetCycle() );
        add( viewPanel );

        NavLocation location = (NavLocation) viewPanel.getCacheParameter( "location" );
        initializeLocation( location );

        setTitle( StringUtils.messageFormat( getPhetLocalizer().getString( "simulationDisplay.title", this ), new Object[] {
                getPhetLocalizer().getString( location.getLocalizationKey(), this )
        } ) );

    }

    @Override
    public String getStyle( String key ) {
        if ( key.equals( "style.canonical" ) ) {
            return canonicalUrl;
        }
        return super.getStyle( key );
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        // WARNING: don't change without also changing the old URL redirection
        mapper.addMap( "^simulations(/index)?$", CategoryPage.class, new String[] { "query-string" } );
        mapper.addMap( "^simulations/category/(.+?)(/index)?$", CategoryPage.class, new String[] { "categories", "query-string" } );
    }

    public static RawLinkable getDefaultLinker() {
        // WARNING: don't change without also changing the old URL redirection
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return "simulations/category/" + Category.getDefaultCategoryKey();
            }
        };
    }

    public static RawLinkable getLinker( final Category category ) {
        return new AbstractLinker() {
            @Override
            public String getSubUrl( PageContext context ) {
                return "simulations/category/" + category.getCategoryPath();
            }
        };
    }

    public static RawLinkable getLinker( final String categories ) {
        return new AbstractLinker() {
            @Override
            public String getSubUrl( PageContext context ) {
                return "simulations/category/" + categories;
            }
        };
    }

    public static RawLinkable getAllSimsLinker() {
        return new AbstractLinker() {
            @Override
            public String getSubUrl( PageContext context ) {
                return "simulations/index";
            }
        };
    }

}