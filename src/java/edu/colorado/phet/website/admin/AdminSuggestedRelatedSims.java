/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.admin;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.hibernate.Session;

import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.Task;
import edu.colorado.phet.website.util.hibernate.VoidTask;

public class AdminSuggestedRelatedSims extends AdminPage {

    private static final Logger logger = Logger.getLogger( AdminSuggestedRelatedSims.class.getName() );

    public AdminSuggestedRelatedSims( PageParameters parameters, final Simulation sim ) {
        super( parameters );

        LocalizedSimulation lsim = HibernateUtils.resultTransaction( getHibernateSession(), new Task<LocalizedSimulation>() {
            public LocalizedSimulation run( Session session ) {
                Simulation simulation = (Simulation) session.load( Simulation.class, sim.getId() );
                add( new Label( "suggest", "Suggested 'Related Simulations' for " + simulation.getEnglishSimulation().getTitle() ) );
                return simulation.getEnglishSimulation();
            }
        } ).value;

        add( new ListView<Score>( "simulation", getSuggestedRelatedSimulations( lsim ) ) {
            @Override
            protected void populateItem( ListItem<Score> item ) {
                Score score = item.getModelObject();
                item.add( new Label( "sim", score.lsim.getTitle() ) );
                item.add( new Label( "score", String.valueOf( score.score ) ) );
            }
        } );

    }

    public static class Score implements Serializable {
        public LocalizedSimulation lsim;
        public int score;

        public Score( LocalizedSimulation lsim, int score ) {
            this.lsim = lsim;
            this.score = score;
        }
    }

    public List<Score> getSuggestedRelatedSimulations( final LocalizedSimulation simulation ) {
        final List<Score> ret = new LinkedList<Score>();
        HibernateUtils.wrapTransaction( getHibernateSession(), new VoidTask() {
            private LocalizedSimulation sim;

            private int score( LocalizedSimulation lsim ) {
                int count = 0;

                // TODO: rank with popularity also?

                if ( lsim.getSimulation().getProject().getId() == sim.getSimulation().getProject().getId() ) {
                    count += 5;
                }

                // add +1 for each keyword that they share
                for ( Object o : lsim.getSimulation().getKeywords() ) {
                    if ( sim.getSimulation().getKeywords().contains( o ) ) {
                        count += 1;
                    }
                }

                // add +1 for each category that they share
                for ( Object o : lsim.getSimulation().getCategories() ) {
                    if ( sim.getSimulation().getCategories().contains( o ) ) {
                        count += 1;
                    }
                }
                return count;
            }

            public Void run( Session session ) {
                sim = (LocalizedSimulation) session.load( LocalizedSimulation.class, simulation.getId() );
                List<LocalizedSimulation> simPool = new LinkedList<LocalizedSimulation>();
                HibernateUtils.addPreferredFullSimulationList( simPool, getHibernateSession(), getMyLocale() );
                simPool.remove( sim ); // don't allow this simulation in

                for ( LocalizedSimulation lsim : simPool ) {
                    ret.add( new Score( lsim, score( lsim ) ) );
                }

                Collections.sort( ret, new Comparator<Score>() {
                    public int compare( Score a, Score b ) {
                        return new Integer( b.score ).compareTo( a.score );
                    }
                } );
                return null;
            }
        } );
        return ret;
    }

}
