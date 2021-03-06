/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.simulations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.hibernate.Session;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.cache.CacheableUrlStaticPanel;
import edu.colorado.phet.website.cache.EventDependency;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.content.TranslationUtilityPanel;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Project;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.data.util.HibernateEventListener;
import edu.colorado.phet.website.data.util.IChangeListener;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.wicket.WicketUtils;

/**
 * Shows a list of all translated and untranslated simulations for a particular locale
 */
public class TranslationListPanel extends PhetPanel implements CacheableUrlStaticPanel {

    private static final Logger logger = Logger.getLogger( TranslationListPanel.class.getName() );

    public TranslationListPanel( String id, final PageContext context, final Locale locale ) {
        super( id, context );

        final List<LocalizedSimulation> localizedSimulations = new LinkedList<LocalizedSimulation>();
        final List<LocalizedSimulation> untranslatedSimulations = new LinkedList<LocalizedSimulation>();
        final Map<Simulation, String> simNameDefault = new HashMap<Simulation, String>();

        add( new LocalizedText( "untranslated-header", "simulations.translated.untranslated", new Object[] {
                StringUtils.getLocaleTitle( locale, context.getLocale(), PhetLocalizer.get() )
        } ) );

        HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
            public boolean run( Session session ) {

                List lsims = session.createQuery( "select ls from LocalizedSimulation as ls where ls.locale = :locale or ls.locale = :default" )
                        .setLocale( "locale", locale ).setLocale( "default", context.getLocale() ).list();

                List ensims = session.createQuery( "select ls from LocalizedSimulation as ls where ls.locale = :locale" )
                        .setLocale( "locale", WebsiteConstants.ENGLISH ).list();

                for ( Object lsim : lsims ) {
                    LocalizedSimulation localizedSimulation = (LocalizedSimulation) lsim;

                    if ( localizedSimulation.getLocale().equals( context.getLocale() ) ) {
                        simNameDefault.put( localizedSimulation.getSimulation(), localizedSimulation.getTitle() );
                    }

                    if ( !localizedSimulation.getSimulation().isVisible() || !localizedSimulation.getLocale().equals( locale ) ) {
                        continue;
                    }

                    localizedSimulations.add( localizedSimulation );
                }

                for ( Object ensimo : ensims ) {
                    LocalizedSimulation ensim = (LocalizedSimulation) ensimo;
                    if ( !ensim.getSimulation().isVisible() ) {
                        continue;
                    }

                    Simulation sim = ensim.getSimulation();

                    boolean found = false;

                    for ( LocalizedSimulation lsim : localizedSimulations ) {
                        if ( lsim.getSimulation().getId() == sim.getId() ) {
                            found = true;
                            break;
                        }
                    }

                    if ( !found ) {
                        untranslatedSimulations.add( ensim );
                    }
                }

                return true;
            }
        } );

        HibernateUtils.orderSimulations( localizedSimulations, context.getLocale() );

        add( new Label( "locale-header", locale.getDisplayName( locale ) + " " + StringUtils.getLocaleTitle( locale, context.getLocale(), (PhetLocalizer) getLocalizer() ) ) );
        add( new ListView<LocalizedSimulation>( "translations", localizedSimulations ) {
            protected void populateItem( ListItem<LocalizedSimulation> item ) {
                LocalizedSimulation lsim = item.getModelObject();

                //Link simLink = SimulationPage.getLinker( lsim ).getLink( "sim-link", context, getPhetCycle() );
                item.add( new Label( "title-translated", lsim.getTitle() ) );
                //item.add( simLink );

                String otherTitle = simNameDefault.get( lsim.getSimulation() );
                if ( otherTitle == null ) {
                    otherTitle = "-";
                }
                item.add( new Label( "title", otherTitle ) );
                item.add( lsim.getRunLink( "run-now-link" ) );

                if ( DistributionHandler.displayJARLink( (PhetRequestCycle) getRequestCycle(), lsim ) ) {
                    item.add( lsim.getDownloadLink( "download-link" ) );
                }
                else {
                    item.add( new InvisibleComponent( "download-link" ) );
                }

                WicketUtils.highlightListItem( item );
            }
        } );

        if ( untranslatedSimulations.isEmpty() ) {
            add( new InvisibleComponent( "untranslated-list" ) );
        }
        else {
            HibernateUtils.orderSimulations( untranslatedSimulations, getMyLocale() );
            add( new ListView<LocalizedSimulation>( "untranslated-list", untranslatedSimulations ) {
                protected void populateItem( ListItem<LocalizedSimulation> item ) {
                    LocalizedSimulation lsim = item.getModelObject();
                    Link link = SimulationPage.getLinker( lsim ).getLink( "untranslated-link", context, getPhetCycle() );
                    link.add( new Label( "untranslated-title", lsim.getTitle() ) );
                    item.add( link );
                }
            } );
        }

        add( new LocalizedText( "remaining", "simulations.translated.toTranslate", new Object[] {
                TranslationUtilityPanel.getLinker().getHref( context, getPhetCycle() )
        } ) );

        // TODO: update dependencies to narrow
        addDependency( new EventDependency() {

            private IChangeListener stringListener;

            @Override
            protected void addListeners() {
                stringListener = createTranslationChangeInvalidator( context.getLocale() );
                HibernateEventListener.addListener( Project.class, getAnyChangeInvalidator() );
                HibernateEventListener.addListener( TranslatedString.class, stringListener );
                HibernateEventListener.addListener( Simulation.class, getAnyChangeInvalidator() );
                HibernateEventListener.addListener( LocalizedSimulation.class, getAnyChangeInvalidator() );
            }

            @Override
            protected void removeListeners() {
                HibernateEventListener.removeListener( Project.class, getAnyChangeInvalidator() );
                HibernateEventListener.removeListener( TranslatedString.class, stringListener );
                HibernateEventListener.removeListener( Simulation.class, getAnyChangeInvalidator() );
                HibernateEventListener.removeListener( LocalizedSimulation.class, getAnyChangeInvalidator() );
            }
        } );

    }

}