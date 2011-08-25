/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.hibernate.Session;

import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.RawCSV;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * CSV of simulations sorted by project name, and then by simulation name
 * <p/>
 */
public class ProjectSortedSimulations extends WebPage {

    private RawCSV csv;

    public ProjectSortedSimulations( PageParameters parameters ) {
        super( parameters );

        csv = new RawCSV();

        // column headers
        csv.addColumnValue( "Projects" );
        csv.addColumnValue( "Simulations" );
        csv.betweenLines();

        // simulations
        HibernateUtils.wrapTransaction( PhetRequestCycle.get().getHibernateSession(), new VoidTask() {
            public void run( final Session session ) {
                List list = session.createQuery( "select s from Simulation as s" ).list();
                List<Simulation> simulations = new LinkedList<Simulation>( list );

                // sort by project name, then by simulation name
                Collections.sort( simulations, new Comparator<Simulation>() {
                    // TODO: replace with StringComparator once we merge
                    public String toString( Simulation simulation ) {
                        return simulation.getProject().getName() + " " + simulation.getName();
                    }

                    public int compare( Simulation a, Simulation b ) {
                        return toString( a ).compareTo( toString( b ) );
                    }
                } );
                for ( Simulation simulation : simulations ) {
                    if ( !simulation.isVisible() ) {
                        continue;
                    }
                    csv.addColumnValue( simulation.getProject().getName() );
                    csv.addColumnValue( simulation.getName() );
                    csv.betweenLines();
                }
            }
        } );

        add( new RawLabel( "text", csv.toString() ) {{
            setRenderBodyOnly( true );
        }} );

        getResponse().setContentType( "text/csv" );
    }

}