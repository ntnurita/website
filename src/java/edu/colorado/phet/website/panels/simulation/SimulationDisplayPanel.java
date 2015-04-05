/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.panels.simulation;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.GridView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.content.simulations.HTML5Page;
import edu.colorado.phet.website.content.simulations.LegacySimulationPage;
import edu.colorado.phet.website.content.simulations.SimulationPage;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.WebImage;
import edu.colorado.phet.website.util.attributes.ClassAppender;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.StringUtils;

import static edu.colorado.phet.website.util.HtmlUtils.encode;

/**
 * Displays a grid of simulation thumbnails, which can be clicked on to go to the simulation page
 */
public class SimulationDisplayPanel extends PhetPanel {

    private static final Logger logger = Logger.getLogger( SimulationDisplayPanel.class.getName() );

    public SimulationDisplayPanel( String id, final PageContext context, List<LocalizedSimulation> simulations ) {
        super( id, context );

        final boolean isEnglish = PhetSession.get().getLocale().equals( LocaleUtils.stringToLocale( "en" ) );
        final boolean isHTML5Page = context.getPath().contains( "simulations/category/new" );

        SimulationDataProvider simData = new SimulationDataProvider( simulations );
        GridView gridView = new GridView<LocalizedSimulation>( "rows", simData ) {

            @Override
            protected void populateEmptyItem( Item item ) {
                item.setVisible( false );
            }

            @Override
            protected void populateItem( Item item ) {
                final LocalizedSimulation simulation = (LocalizedSimulation) item.getModelObject();
                Link link;
                if ( isEnglish || isHTML5Page ) {
                    link = SimulationPage.getLinker( simulation ).getLink( "simulation-link", context, getPhetCycle() );
                }
                else {
                    link = LegacySimulationPage.getLinker( simulation ).getLink( "simulation-link", context, getPhetCycle() );
                }
                link.add( new Label( "title", simulation.getTitle() ) );
                if ( !simulation.getLocale().getLanguage().equals( context.getLocale().getLanguage() ) ) {
                    // sim isn't translated
                    link.add( new ClassAppender( "untranslated-sim" ) );
                }
                String alt;
                try {
                    alt = StringUtils.messageFormat( "Screenshot of the simulation {0}", encode( simulation.getTitle() ) );
                }
                catch ( RuntimeException e ) {
                    e.printStackTrace();
                    alt = "Screenshot of the simulation";
                }

                // rotate among the data servers
                String[] dataServers = DistributionHandler.getDistributionServers( getPhetCycle() );
                int dataServerIndex = simulation.getId() % dataServers.length;
//                final String dataServer = dataServers[dataServerIndex];

                // TODO: for now don't use dataservers when not on figaro since the new screenshots are not there
                String serverName = getPhetCycle().getHttpServletRequest().getServerName();
                final String dataServer = ( !serverName.equals( "phet.colorado.edu" ) ) ? serverName : dataServers[dataServerIndex];

                WebImage thumbnail = ( simulation.getSimulation().isHTML() ) ? simulation.getSimulation().getHTMLThumbnail() : simulation.getSimulation().getThumbnail();
                link.add( new StaticImage( "thumbnail", thumbnail, alt ) {{
                    setOutputMarkupId( true );
                    setMarkupId( "simulation-display-thumbnail-" + simulation.getSimulation().getName() );
                    setDataServer( dataServer );
                }} );
                WebMarkupContainer badge = new WebMarkupContainer( "badge" );
                link.add( badge );
                if ( simulation.getSimulation().isHTML() ) {
                    badge.add( new SimpleAttributeModifier( "class", "sim-display-badge sim-badge-html" ) );
                }
                else if ( simulation.getSimulation().isJava() ) {
                    badge.add( new SimpleAttributeModifier( "class", "sim-display-badge sim-badge-java" ) );
                }
                else if ( simulation.getSimulation().isFlash() ) {
                    badge.add( new SimpleAttributeModifier( "class", "sim-display-badge sim-badge-flash" ) );
                }
                else {
                    logger.warn( "Simulation " + simulation.getSimulation().getName() + "didn't have a type" );
                }

                item.add( link );
            }
        };
        gridView.setColumns( 3 );
        add( gridView );

        //add( HeaderContributor.forCss( CSS.CATEGORY_PAGE ) );

    }

    private static class SimulationDataProvider implements IDataProvider<LocalizedSimulation> {
        private List<LocalizedSimulation> simulations;

        private SimulationDataProvider( List<LocalizedSimulation> simulations ) {
            this.simulations = simulations;
        }


        public Iterator<LocalizedSimulation> iterator( int first, int count ) {
            int endIndex = first + count;
            if ( endIndex > simulations.size() ) {
                endIndex = simulations.size();
            }
            return simulations.subList( first, endIndex ).iterator();
        }

        public int size() {
            return simulations.size();
        }

        public IModel<LocalizedSimulation> model( final LocalizedSimulation o ) {
            return new IModel<LocalizedSimulation>() {
                public LocalizedSimulation getObject() {
                    return o;
                }

                public void setObject( LocalizedSimulation o ) {

                }

                public void detach() {

                }
            };
        }

        public void detach() {

        }
    }

}