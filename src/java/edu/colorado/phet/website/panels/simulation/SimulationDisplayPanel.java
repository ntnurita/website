/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.panels.simulation;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.GridView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.CSS;
import edu.colorado.phet.website.content.simulations.SimulationPage;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.ClassAppender;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.StringUtils;

import static edu.colorado.phet.website.util.HtmlUtils.encode;

public class SimulationDisplayPanel extends PhetPanel {

    public SimulationDisplayPanel( String id, final PageContext context, List<LocalizedSimulation> simulations ) {
        super( id, context );

        SimulationDataProvider simData = new SimulationDataProvider( simulations );
        GridView gridView = new GridView<LocalizedSimulation>( "rows", simData ) {
            @Override
            protected void populateEmptyItem( Item item ) {
                item.setVisible( false );
            }

            @Override
            protected void populateItem( Item item ) {
                final LocalizedSimulation simulation = (LocalizedSimulation) item.getModelObject();
                Link link = SimulationPage.getLinker( simulation ).getLink( "simulation-link", context, getPhetCycle() );
                link.add( new Label( "title", simulation.getTitle() ) );
                if ( !simulation.getLocale().getLanguage().equals( context.getLocale().getLanguage() ) ) {
                    // sim isn't translated
                    link.add( new ClassAppender( "untranslated-sim" ) );
                }
                String alt;
                try {
                    alt = StringUtils.messageFormat( "Screenshot of the simulation {0}", encode( simulation.getTitle() ) );
                }
                catch( RuntimeException e ) {
                    e.printStackTrace();
                    alt = "Screenshot of the simulation";
                }
                link.add( new StaticImage( "thumbnail", simulation.getSimulation().getThumbnailUrl(), alt ) {{
                    setOutputMarkupId( true );
                    setMarkupId( "simulation-display-thumbnail-" + simulation.getSimulation().getName() );
                }} );
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