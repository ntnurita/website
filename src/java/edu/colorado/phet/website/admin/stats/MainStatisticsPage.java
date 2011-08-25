/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.admin.stats;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.hibernate.Session;

import edu.colorado.phet.website.admin.AdminPage;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.data.contribution.Contribution;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.SimpleTask;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * A page that shows some elementary statistics about the website, simulations, activities and translations.
 * <p/>
 * In general, if I'm asked about a statistic and I can compute it on this page, I'll probably add it here so I won't
 * need to compute it more.
 */
public class MainStatisticsPage extends AdminPage {

    private static final Logger logger = Logger.getLogger( MainStatisticsPage.class.getName() );

    public MainStatisticsPage( PageParameters parameters ) {
        super( parameters );

        HibernateUtils.wrapTransaction( getHibernateSession(), new SimpleTask() {
            public void run( Session session ) {
                final List simList = session.createQuery( "select s from Simulation as s" ).list();
                final List activityList = session.createQuery( "select c from Contribution as c" ).list();
                final Set<Locale> usedSimulationLocales = new HashSet<Locale>();
                final List<LocalizedSimulation> localizedSimulations = new LinkedList<LocalizedSimulation>();
                List<Simulation> simulations = new LinkedList<Simulation>() {{
                    for ( Object o : simList ) {
                        Simulation simulation = (Simulation) o;
                        if ( simulation.isVisible() ) { // note: checks project visibility too, don't reduce
                            add( simulation );
                            for ( Object o1 : simulation.getLocalizedSimulations() ) {
                                LocalizedSimulation lsim = (LocalizedSimulation) o1;
                                localizedSimulations.add( lsim );
                                usedSimulationLocales.add( lsim.getLocale() );
                            }
                        }
                    }
                }};
                List<Contribution> contributions = new LinkedList<Contribution>( activityList );
                int phetContributionsCount = 0;
                for ( Contribution contribution : contributions ) {
                    if ( contribution.isFromPhet() ) {
                        phetContributionsCount++;
                    }
                }

                add( new Label( "sim-count", "Total visible simulations: " + simulations.size() ) );
                add( new Label( "activity-count", "Total activities: " + contributions.size() ) );
                add( new Label( "phet-activity-count", "PhET activities: " + phetContributionsCount ) );
                add( new Label( "total-sim-languages", "Number of languages (including English) that sims are translated into: " + usedSimulationLocales.size() ) );
                add( new Label( "total-sim-translations", "Number of sim translations (including English): " + localizedSimulations.size() ) );

                add( new Label( "total-users", "Number of users: " + session.createQuery( "select count(*) from PhetUser" ).uniqueResult() ) );
                add( new Label( "subscribed-users", "Users subscribed to receive our newsletter: " + session.createQuery( "select count(*) from PhetUser as u where u.receiveEmail = true" ).uniqueResult() ) );
                add( new Label( "newsletter-only-users", "Users with newsletter-only accounts: " + session.createQuery( "select count(*) from PhetUser as u where u.newsletterOnlyAccount = true" ).uniqueResult() ) );
            }
        } );
    }

}