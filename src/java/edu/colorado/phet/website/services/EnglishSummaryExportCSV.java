// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.hibernate.Session;

import edu.colorado.phet.common.phetcommon.util.FunctionalUtils;
import edu.colorado.phet.common.phetcommon.util.function.Function1;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.content.simulations.SimulationPage;
import edu.colorado.phet.website.data.Alignment;
import edu.colorado.phet.website.data.Category;
import edu.colorado.phet.website.data.Keyword;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.RawCSV;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * CSV of simulation data requested for internal use by the PhET team
 */
public class EnglishSummaryExportCSV extends WebPage {

    private RawCSV csv;

    public EnglishSummaryExportCSV( PageParameters parameters ) {
        super( parameters );

        csv = new RawCSV();

        csv.addColumnValue( "Simulation Project" );
        csv.addColumnValue( "Simulation Flavor" );
        csv.addColumnValue( "Simulation Title" );
        csv.addColumnValue( "Description" );
        csv.addColumnValue( "Main Topics" );
        csv.addColumnValue( "Keywords" );
        csv.addColumnValue( "Categories" );
        csv.addColumnValue( "Learning Goals" );
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
                for ( final Simulation simulation : simulations ) {
                    if ( !simulation.isVisible() ) {
                        continue;
                    }

                    csv.addColumnValue( simulation.getProject().getName() ); // project
                    csv.addColumnValue( simulation.getName() ); // flavor

                    LocalizedSimulation englishSimulation = simulation.getEnglishSimulation();

                    csv.addColumnValue( englishSimulation.getTitle() ); // Title
                    csv.addColumnValue( joinDelimited( Arrays.asList( StringUtils.lookup( session, simulation.getDescriptionKey() ).split( "<br/>" ) ) ) ); // Description

                    // Keywords
                    final Set<String> topicsKeys = new HashSet<String>();
                    for ( Keyword keyword : new HashSet<Keyword>() {{
                        addAll( simulation.getTopics() );
                    }} ) {
                        topicsKeys.add( keyword.getLocalizationKey() );
                    }
                    List<String> topics = new LinkedList<String>() {{
                        for ( String keywordKey : topicsKeys ) {
                            add( StringUtils.lookup( session, keywordKey ) );
                        }
                        Collections.sort( this );
                    }};
                    csv.addColumnValue( joinDelimited( topics ) ); // Keywords

                    // Keywords
                    final Set<String> keywordKeys = new HashSet<String>();
                    for ( Keyword keyword : new HashSet<Keyword>() {{
                        addAll( simulation.getKeywords() );
                    }} ) {
                        keywordKeys.add( keyword.getLocalizationKey() );
                    }
                    List<String> keywords = new LinkedList<String>() {{
                        for ( String keywordKey : keywordKeys ) {
                            add( StringUtils.lookup( session, keywordKey ) );
                        }
                        Collections.sort( this );
                    }};
                    csv.addColumnValue( joinDelimited( keywords ) ); // Keywords

                    Set<String> mainCategories = new HashSet<String>();
                    Set<String> parentCategories = new HashSet<String>();

                    for ( Object o : simulation.getCategories() ) {
                        Category category = (Category) o;
                        if ( !category.isGradeLevelCategory() ) {
                            mainCategories.add( StringUtils.lookup( session, category.getLocalizationKey() ) );
                            if ( !category.getParent().isRoot() ) {
                                parentCategories.add( StringUtils.lookup( session, category.getParent().getLocalizationKey() ) );
                            }
                        }
                    }

                    csv.addColumnValue( joinDelimited( FunctionalUtils.unique( FunctionalUtils.concat( mainCategories, parentCategories ) ) ) ); // Categories
                    csv.addColumnValue( joinDelimited( Arrays.asList( StringUtils.lookup( session, simulation.getLearningGoalsKey() ).split( "<br/>" ) ) ) ); // Learning Goals

                    csv.betweenLines();
                }
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
                result += ", ";
            }
            result += string;
        }
        return result;
    }

}