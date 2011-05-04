/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.services;

import java.util.*;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.hibernate.Session;

import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.data.Keyword;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.RawCSV;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * CSV of simulation data that is targetted towards exporting data to learning.com
 */
public class LearningComExport extends WebPage {

    private RawCSV csv;

    public LearningComExport( PageParameters parameters ) {
        super( parameters );

        csv = new RawCSV();

        // column headers
        csv.addColumnValue( "Simulation Name" ); // simulation name
        csv.addColumnValue( "Product High Grade" );
        csv.addColumnValue( "Product Low Grade" );
        csv.addColumnValue( "Sequence Title" ); // like "Physics"?
        csv.addColumnValue( "Unit Title" ); // like sub-subject ("Motion")?
        csv.addColumnValue( "Curriculum Item Type Icon" ); // thumbnail
        csv.addColumnValue( "Curriculum Item Description" ); // sim description
        csv.addColumnValue( "Direct Item Link" ); // link to "Run Now"
        csv.addColumnValue( "Keywords" ); // main topics + keywords, delimited by " | "
        csv.addColumnValue( "Primary Learning Objective" ); // learning goals
        csv.betweenLines();

        // simulations
        HibernateUtils.wrapTransaction( PhetRequestCycle.get().getHibernateSession(), new VoidTask() {
            public Void run( final Session session ) {
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
                for ( final Simulation simulation : simulations ) {
                    if ( !simulation.isVisible() ) {
                        continue;
                    }
                    csv.addColumnValue( simulation.getName() );

                    csv.addColumnValue( "" );// TODO: product high grade
                    csv.addColumnValue( "" );// TODO: product low grade

                    csv.addColumnValue( "" );// TODO: sequence title
                    csv.addColumnValue( "" );// TODO: unit title

                    csv.addColumnValue( StringUtils.makeUrlAbsolute( simulation.getThumbnailUrl() ) ); // thumbnail url
                    csv.addColumnValue( StringUtils.lookup( session, simulation.getDescriptionKey() ) ); // English desc
                    csv.addColumnValue( StringUtils.makeUrlAbsolute( simulation.getEnglishSimulation().getRunUrl() ) ); // direct item link

                    /*---------------------------------------------------------------------------*
                    * " | "-delimited keywords
                    *----------------------------------------------------------------------------*/

                    final Set<String> keywordKeys = new HashSet<String>();
                    for ( Keyword keyword : new HashSet<Keyword>() {{
                        addAll( simulation.getKeywords() );
                        addAll( simulation.getTopics() );
                    }} ) {
                        keywordKeys.add( keyword.getLocalizationKey() );
                    }
                    List<String> keywords = new LinkedList<String>() {{
                        for ( String keywordKey : keywordKeys ) {
                            add( StringUtils.lookup( session, keywordKey ) );
                        }
                        Collections.sort( this );
                    }};
                    csv.addColumnValue( joinDelimited( keywords ) );

                    // learning goals
                    csv.addColumnValue( joinDelimited( Arrays.asList( StringUtils.lookup( session, simulation.getLearningGoalsKey() ).split( "<br/>" ) ) ) );

                    csv.betweenLines();
                }
                return null;
            }
        } );

        add( new RawLabel( "text", csv.toString() ) {{
            setRenderBodyOnly( true );
        }} );

        getResponse().setContentType( "text/csv" );
    }

    private static String joinDelimited( Collection<String> strings ) {
        String result = "";
        for ( String string : strings ) {
            if ( result.length() > 0 ) {
                result += " | ";
            }
            result += string;
        }
        return result;
    }

}