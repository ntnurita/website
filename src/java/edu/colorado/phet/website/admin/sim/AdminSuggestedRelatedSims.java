/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.admin.sim;

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

import edu.colorado.phet.website.admin.AdminPage;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.data.Category;
import edu.colorado.phet.website.data.Keyword;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.translation.PhetLocalizer;
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
                item.add( new RawLabel( "notes", score.notes ) );
                item.add( new RawLabel( "keywords", score.keywords ) );
                item.add( new RawLabel( "categories", score.categories ) );
            }
        } );

    }

    public static class Score implements Serializable {
        public LocalizedSimulation lsim;
        public int score;
        public String notes;
        public String keywords;
        public String categories;

        public Score( LocalizedSimulation lsim, int score, String notes, String keywords, String categories ) {
            this.lsim = lsim;
            this.score = score;
            this.notes = notes;
            this.keywords = keywords;
            this.categories = categories;
        }
    }

    public List<Score> getSuggestedRelatedSimulations( final LocalizedSimulation simulation ) {
        final List<Score> ret = new LinkedList<Score>();
        HibernateUtils.wrapTransaction( getHibernateSession(), new VoidTask() {
            private LocalizedSimulation sim;

            private Score score( LocalizedSimulation lsim ) {
                int count = 0;

                String notes = "";
                String keywords = "";
                String categories = "";

                // TODO: rank with popularity also?

                if ( lsim.getSimulation().getProject().getId() == sim.getSimulation().getProject().getId() ) {
                    count += 5;
                    notes += "same project";
                }

                // add +1 for each keyword that they share
                for ( Object o : lsim.getSimulation().getKeywords() ) {
                    if ( sim.getSimulation().getKeywords().contains( o ) ) {
                        count += 1;
                        if ( keywords.length() > 0 ) {
                            keywords += ", ";
                        }
                        keywords += PhetLocalizer.get().getBestStringWithinTransaction( getHibernateSession(), ( (Keyword) o ).getLocalizationKey(), WebsiteConstants.ENGLISH );
                    }
                }

                // add +1 for each category that they share
                for ( Object o : lsim.getSimulation().getCategories() ) {
                    if ( sim.getSimulation().getCategories().contains( o ) ) {
                        count += 1;
                        if ( categories.length() > 0 ) {
                            categories += ", ";
                        }
                        categories += PhetLocalizer.get().getBestStringWithinTransaction( getHibernateSession(), ( (Category) o ).getLocalizationKey(), WebsiteConstants.ENGLISH );
                    }
                }
                return new Score( lsim, count, notes, keywords, categories );
            }

            public Void run( Session session ) {
                sim = (LocalizedSimulation) session.load( LocalizedSimulation.class, simulation.getId() );
                List<LocalizedSimulation> simPool = new LinkedList<LocalizedSimulation>();
                HibernateUtils.addPreferredFullSimulationList( simPool, getHibernateSession(), getMyLocale() );
                simPool.remove( sim ); // don't allow this simulation in

                for ( LocalizedSimulation lsim : simPool ) {
                    ret.add( score( lsim ) );
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
