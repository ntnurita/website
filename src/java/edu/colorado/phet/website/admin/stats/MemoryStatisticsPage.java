// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.admin.stats;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.Localizer;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.hibernate.Session;

import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.admin.AdminPage;
import edu.colorado.phet.website.authentication.panels.ResetPasswordRequestPanel;
import edu.colorado.phet.website.cache.ImageCache;
import edu.colorado.phet.website.cache.PanelCache;
import edu.colorado.phet.website.cache.SimulationCache;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.data.contribution.Contribution;
import edu.colorado.phet.website.data.util.CategoryChangeHandler;
import edu.colorado.phet.website.data.util.HibernateEventListener;
import edu.colorado.phet.website.newsletter.InitialSubscribePanel;
import edu.colorado.phet.website.panels.ComponentThreadStatusPanel;
import edu.colorado.phet.website.services.Autocomplete;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * Statistics for use in debugging our memory issues
 */
public class MemoryStatisticsPage extends AdminPage {

    private static final Logger logger = Logger.getLogger( MemoryStatisticsPage.class.getName() );

    public MemoryStatisticsPage( PageParameters parameters ) {
        super( parameters );

        try {
            add( new Label( "mem-translation-count", "PhetWicketApplication.translations: " + PhetWicketApplication.get().getTranslationCount() ) );
            add( new Label( "mem-imagemap-count", "ImageCache.imageMap: " + ImageCache.getImageMapCount() ) );
            Field cacheField = Localizer.class.getDeclaredField( "cache" );
            cacheField.setAccessible( true );
            Map<String, String> localizerCache = (Map<String, String>) cacheField.get( PhetLocalizer.get() );
            add( new Label( "mem-phetlocalizer-count", "PhetLocalizer.cache: " + localizerCache.size() ) );
            add( new Label( "mem-panelcache-count", "PanelCache.cache: " + PanelCache.get().getSize() ) );
            add( new Label( "mem-simcache-preferred-count", "SimulationCache.preferredSims: " + SimulationCache.getPreferredSimsCount() ) );
            add( new Label( "mem-simcache-map-count", "SimulationCache.fullSortedLocalizedMap: " + SimulationCache.getMapCount() ) );
            add( new Label( "mem-autocomplete-count", "Autocomplete tries: " + Autocomplete.getTrieCount() ) );
            add( new Label( "mem-hibernatelistener-count", "HibernateEventListener.listenerMap: " + HibernateEventListener.getListenerCount() ) );
            add( new Label( "mem-categorylistener-count", "CategoryChangeHandler.listeners: " + CategoryChangeHandler.getListenerCount() ) );
            add( new Label( "mem-spamip-count", "InitialSubscribeCount.ipAttempts: " + InitialSubscribePanel.getIPCount() ) );
            add( new Label( "mem-spamemail-count", "InitialSubscribeCount.emailAttempts: " + InitialSubscribePanel.getEmailCount() ) );
            add( new Label( "mem-threadmap-count", "ComponentThreadStatusPanel.threadMap: " + ComponentThreadStatusPanel.getThreadMapCount() ) );
            add( new Label( "mem-resetattempt-count", "ResetPasswordRequestPanel.attempts: " + ResetPasswordRequestPanel.getAttemptCounts() ) );
            add( new RawLabel( "mem-hibernate-report", HibernateEventListener.getListenerReport() ) );
            add( new RawLabel( "mem-category-report", CategoryChangeHandler.getListenerReport() ) );
        }
        catch( IllegalAccessException e ) {
            e.printStackTrace();
        }
        catch( NoSuchFieldException e ) {
            e.printStackTrace();
        }

        HibernateUtils.wrapTransaction( getHibernateSession(), new VoidTask() {
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

//                add( new Label( "statistics-sim-count", "Total visible simulations: " + simulations.size() ) );
//                add( new Label( "activity-count", "Total activities: " + contributions.size() ) );
//                add( new Label( "phet-activity-count", "PhET activities: " + phetContributionsCount ) );
//                add( new Label( "total-sim-languages", "Number of languages (including English) that sims are translated into: " + usedSimulationLocales.size() ) );
//                add( new Label( "total-sim-translations", "Number of sim translations (including English): " + localizedSimulations.size() ) );
//
//                add( new Label( "total-users", "Number of users: " + session.createQuery( "select count(*) from PhetUser" ).uniqueResult() ) );
//                add( new Label( "subscribed-users", "Users subscribed to receive our newsletter: " + session.createQuery( "select count(*) from PhetUser as u where u.receiveEmail = true" ).uniqueResult() ) );
//                add( new Label( "subscribed-simulation-users", "Users subscribed to receive simulation notifications: " + session.createQuery( "select count(*) from PhetUser as u where u.receiveSimulationNotifications = true" ).uniqueResult() ) );
//                add( new Label( "newsletter-only-users", "Users with newsletter-only accounts: " + session.createQuery( "select count(*) from PhetUser as u where u.newsletterOnlyAccount = true" ).uniqueResult() ) );
            }
        } );
    }

}