/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation.entities;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;

import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.panels.simulation.SimulationMainPanel;
import edu.colorado.phet.website.translation.PhetPanelFactory;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

public class SimulationsEntity extends TranslationEntity {

    public SimulationsEntity() {
        setDescription( "There is one 'description' string and one 'learning goals' string for each of our simulations.<br/>The simulations are ordered as they appear on the <a href=\"/en/simulations/index\" target=\"_blank\">\"All Sims\" page</a>.<br/>For the learning goals, please put each goal on a separate line, as they will be separated into a list." );
        final List<TranslatedString> strings = new LinkedList<TranslatedString>();
        HibernateUtils.wrapSession( new HibernateTask() {
            public boolean run( Session session ) {
                List strs = session.createQuery( "select ts from Translation as t, TranslatedString as ts where t.visible = true and t.locale = :locale and ts.translation = t and ts.key like 'simulation.%'" )
                        .setLocale( "locale", WebsiteConstants.ENGLISH ).list();
                for ( Object o : strs ) {
                    TranslatedString string = (TranslatedString) o;
                    strings.add( string );
                }
                return true;
            }
        } );

        Collections.sort( strings, new Comparator<TranslatedString>() {
            public int compare( TranslatedString a, TranslatedString b ) {
                return a.getKey().compareTo( b.getKey() );
            }
        } );

        for ( TranslatedString translatedString : strings ) {
            addString( translatedString.getKey() );
        }

        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                            final List<LocalizedSimulation> simulations = new LinkedList<LocalizedSimulation>();
                            HibernateUtils.addPreferredFullSimulationList( simulations, requestCycle.getHibernateSession(), context.getLocale() );
                            HibernateUtils.orderSimulations( simulations, context.getLocale() );
                            return new SimulationMainPanel( id, simulations.get( 0 ), context );
                        }
                    }, "1st simulation page with description and learning goals" );
    }

    public String getDisplayName() {
        return "Simulation Strings";
    }
}