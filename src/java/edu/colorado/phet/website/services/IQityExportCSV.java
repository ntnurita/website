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
import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.common.phetcommon.util.function.Function1;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.content.simulations.SimulationPage;
import edu.colorado.phet.website.data.Alignment;
import edu.colorado.phet.website.data.Category;
import edu.colorado.phet.website.data.GradeLevel;
import edu.colorado.phet.website.data.Keyword;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.panels.simulation.SimulationMainPanel;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.RawCSV;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * CSV of simulation data that is targetted towards exporting data for iQity.
 */
public class IQityExportCSV extends WebPage {

    private RawCSV csv;

    public IQityExportCSV( PageParameters parameters ) {
        super( parameters );

        csv = new RawCSV();

        // column headers
        csv.addColumnValue( "Collection Name" );
        csv.addColumnValue( "Course Contents" );
        csv.addColumnValue( "Title" );
        csv.addColumnValue( "Description" );
        csv.addColumnValue( "Keywords" );
        csv.addColumnValue( "Language" );
        csv.addColumnValue( "Subjects" );
        csv.addColumnValue( "Learning Resource Type" );
        csv.addColumnValue( "Interactivity Type" );
        csv.addColumnValue( "Intended End User" );
        csv.addColumnValue( "Degree of Interactivity" );
        csv.addColumnValue( "Min Age" );
        csv.addColumnValue( "Max Age" );
        csv.addColumnValue( "Difficulty" );
        csv.addColumnValue( "Context" );
        csv.addColumnValue( "Extension" );
        csv.addColumnValue( "Standards Code" );
        csv.addColumnValue( "Standards Verbiage" );
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

                    LocalizedSimulation englishSimulation = simulation.getEnglishSimulation();

                    csv.addColumnValue( "PhET Interactive Simulations" ); // Collection Name
                    csv.addColumnValue( englishSimulation.getRunUrl() + ", " + SimulationPage.getLinker( simulation ).getDefaultRawUrl() ); // Course Contents
                    csv.addColumnValue( englishSimulation.getTitle() ); // Title
                    csv.addColumnValue( joinDelimited( Arrays.asList( StringUtils.lookup( session, simulation.getDescriptionKey() ).split( "<br/>" ) ) ) ); // Description

                    // Keywords
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
                    csv.addColumnValue( joinDelimited( keywords ) ); // Keywords

                    final List<Locale> locales = FunctionalUtils.map( (Set<LocalizedSimulation>)(simulation.getLocalizedSimulations()), new Function1<LocalizedSimulation,Locale>() {
                        public Locale apply( LocalizedSimulation lsim ) {
                            return lsim.getLocale();
                        }
                    } );
                    final List<String> localeStrings = FunctionalUtils.map( locales, new Function1<Locale, String>() {
                        public String apply( Locale locale ) {
                            return StringUtils.getLocaleTitle( locale, WebsiteConstants.ENGLISH, PhetLocalizer.get() );
//                            return StringUtils.lookup( session, LocaleUtils.localeToString( locale ) );
                        }
                    } );
                    csv.addColumnValue( joinDelimited( localeStrings ) ); // Language

                    Set<String> mainCategories = new HashSet<String>();
                    Set<String> parentCategories = new HashSet<String>();
                    Set<String> lreterms = simulation.getLreTermIDs();

                    for ( Object o : simulation.getCategories() ) {
                        Category category = (Category) o;
                        if ( !category.isGradeLevelCategory() ) {
                            mainCategories.add( StringUtils.lookup( session, category.getLocalizationKey() ) );
                            if ( !category.getParent().isRoot() ) {
                                parentCategories.add( StringUtils.lookup( session, category.getParent().getLocalizationKey() ) );
                            }
                        }
                    }

                    csv.addColumnValue( joinDelimited( FunctionalUtils.unique( FunctionalUtils.concat( mainCategories, parentCategories, lreterms ) ) ) ); // Subjects
                    csv.addColumnValue( "Simulation" ); // Learning Resource Type
                    csv.addColumnValue( "Active" ); // Interactivity Type
                    csv.addColumnValue( "Learner" ); // Intended End User
                    csv.addColumnValue( "High" ); // Degree of Interactivity
                    csv.addColumnValue( String.valueOf( simulation.getMinGradeLevel().getLowAge() ) ); // Min Age
                    csv.addColumnValue( String.valueOf( simulation.getMaxGradeLevel().getHighAge() ) ); // Max Age
                    csv.addColumnValue( "Varies" ); // Difficulty
                    csv.addColumnValue( "Context" ); // Context
                    csv.addColumnValue( "URL for JNLP/HTML, then URL for the sim page" ); // Extension

                    List<String> mainStandards = FunctionalUtils.map( simulation.getAlignments(), new Function1() {
                        public String apply( Object o ) {
                            Alignment alignment = (Alignment) o;
                            return alignment.getUrl();
                        }
                    } );
                    List<String> secondaryStandards = FunctionalUtils.map( simulation.getSecondaryAlignments(), new Function1() {
                        public String apply( Object o ) {
                            Alignment alignment = (Alignment) o;
                            return alignment.getUrl();
                        }
                    } );
                    csv.addColumnValue( joinDelimited( FunctionalUtils.concat( mainStandards, secondaryStandards ) ) ); // Standards Code
                    csv.addColumnValue( (mainStandards.isEmpty() && secondaryStandards.isEmpty()) ? "" : "Common Core Math and others" ); // Standards Verbiage

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