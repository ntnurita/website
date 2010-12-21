package edu.colorado.phet.website.services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.hibernate.Session;

import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.content.contribution.ContributionPage;
import edu.colorado.phet.website.content.simulations.SimulationPage;
import edu.colorado.phet.website.data.Category;
import edu.colorado.phet.website.data.Keyword;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.data.contribution.Contribution;
import edu.colorado.phet.website.data.contribution.ContributionLevel;
import edu.colorado.phet.website.data.contribution.ContributionSubject;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * Information for the state of North Carolina's repository
 */
public class NCInformationPage extends WebPage {

    private StringBuilder str;

    public NCInformationPage( PageParameters parameters ) {
        super( parameters );

        str = new StringBuilder();

        // column headers
        for ( Column column : Column.values() ) {
            column( column.getName() );
        }
        betweenLines();

        // simulations
        HibernateUtils.wrapTransaction( PhetRequestCycle.get().getHibernateSession(), new VoidTask() {
            public Void run( final Session session ) {
                List list = session.createQuery( "select s from Simulation as s" ).list();
                List<Simulation> simulations = new LinkedList<Simulation>( list );
                for ( Simulation simulation : simulations ) {
                    if ( !simulation.isVisible() ) {
                        continue;
                    }
                    LocalizedSimulation english = simulation.getEnglishSimulation();
                    for ( Column column : Column.values() ) {
                        switch( column ) {
                            case TITLE:
                                column( english.getTitle() );
                                break;
                            case DESCRIPTION:
                                column( lookup( session, simulation.getDescriptionKey() ) );
                                break;
                            case KEYWORDS:
                                column( generalMap( simulation.getKeywords(), new Stringifier<Keyword>() {
                                    public String stringify( Keyword keyword ) {
                                        return lookup( session, keyword.getLocalizationKey() );
                                    }
                                } ) );
                                break;
                            case SUBJECT:
                                column( generalMap( simulation.getCategories(), new Stringifier<Category>() {
                                    public String stringify( Category category ) {
                                        if (
                                                category.getName().equals( "elementary-school" )
                                                || category.getName().equals( "middle-school" )
                                                || category.getName().equals( "high-school" )
                                                || category.getName().equals( "university" )
                                                ) {
                                            return null;
                                        }
                                        return lookup( session, category.getNavLocation( PhetWicketApplication.get().getMenu() ).getLocalizationKey() );
                                    }
                                } ) );
                                break;
                            case RESOURCE_TYPE:
                                column( "Simulation" );
                                break;
                            case EDUCATION_LEVEL:
                                column( generalMap( simulation.getCategories(), new Stringifier<Category>() {
                                    public String stringify( Category category ) {
                                        if (
                                                category.getName().equals( "elementary-school" )
                                                || category.getName().equals( "middle-school" )
                                                || category.getName().equals( "high-school" )
                                                || category.getName().equals( "university" )
                                                ) {
                                            return lookup( session, category.getNavLocation( PhetWicketApplication.get().getMenu() ).getLocalizationKey() );
                                        }
                                        return null;
                                    }
                                } ) );
                                break;
                            case LANGUAGE:
                                column( generalMap( simulation.getLocalizedSimulations(), new Stringifier<LocalizedSimulation>() {
                                    public String stringify( LocalizedSimulation lsim ) {
                                        return lsim.getLocale().getDisplayName();
                                    }
                                } ) );
                                break;
                            case CREATION_DATE:
                                if ( simulation.getCreateTime() != null ) {
                                    column( simulation.getCreateTime().toString() );
                                }
                                else {
                                    emptyColumn();
                                }
                                break;
                            case PUBLICLY_ACCESSIBLE:
                                column( "http://phet.colorado.edu" + SimulationPage.getLinker( simulation ).getDefaultRawUrl() );
                                break;
                            case COPYRIGHT:
                                column( "University of Colorado" );
                                break;
                            case DRM:
                                column( "no" );
                                break;
                        }
                    }
                    betweenLines();
                }
                return null;
            }
        } );

        // activities
        HibernateUtils.wrapTransaction( PhetRequestCycle.get().getHibernateSession(), new VoidTask() {
            public Void run( final Session session ) {
                List list = session.createQuery( "select c from Contribution as c" ).list();
                List<Contribution> contributions = new LinkedList<Contribution>( list );
                for ( Contribution contribution : contributions ) {
                    for ( Column column : Column.values() ) {
                        switch( column ) {
                            case TITLE:
                                column( contribution.getTitle() );
                                break;
                            case DESCRIPTION:
                                column( contribution.getDescription() );
                                break;
                            case KEYWORDS:
                                column( contribution.getKeywords() );
                                break;
                            case SUBJECT:
                                column( generalMap( contribution.getSubjects(), new Stringifier<ContributionSubject>() {
                                    public String stringify( ContributionSubject cSubject ) {
                                        return lookup( session, cSubject.getSubject().getTranslationKey() );
                                    }
                                } ) );
                                break;
                            case RESOURCE_TYPE:
                                column( "Contributed Activity" );
                                break;
                            case EDUCATION_LEVEL:
                                column( generalMap( contribution.getLevels(), new Stringifier<ContributionLevel>() {
                                    public String stringify( ContributionLevel level ) {
                                        return lookup( session, level.getLevel().getTranslationKey() );
                                    }
                                } ) );
                                break;
                            case LANGUAGE:
                                column( contribution.getLocale().getDisplayName() );
                                break;
                            case CREATION_DATE:
                                column( contribution.getDateCreated().toString() );
                                break;
                            case PUBLICLY_ACCESSIBLE:
                                column( "http://phet.colorado.edu" + ContributionPage.getLinker( contribution ).getDefaultRawUrl() );
                                break;
                            case COPYRIGHT:
                                column( contribution.getPhetUser().getName() );
                                break;
                            case DRM:
                                column( "no" );
                                break;
                        }
                    }
                    betweenLines();
                }
                return null;
            }
        } );

        add( new RawLabel( "text", str.toString() ) {{
            setRenderBodyOnly( true );
        }} );

        getResponse().setContentType( "text/csv" );
    }

    private String lookup( Session session, String key ) {
        return PhetLocalizer.get().getDefaultString( session, key, "", true ).replace( "&#039;", "'" ).replace( "&quot;", "\"" ).replace( "&amp;", "&" ).replace( "&lt;", "<" ).replace( "&gt;", ">" );
    }

    private <T> String generalMap( Collection collection, Stringifier<T> stringifier ) {
        StringBuffer buf = new StringBuffer();
        int count = 0;
        for ( Object o : collection ) {
            if ( o != null ) {
                String toAdd = stringifier.stringify( (T) o );
                if ( toAdd != null ) {
                    toAdd = toAdd.replace( ",", "" );
                    if ( count > 0 ) {buf.append( "," );}
                    buf.append( toAdd ); // get rid of commas
                    count++;
                }
            }
        }
        return buf.toString();
    }

    private int columnsPrinted = 0;

    private void emptyColumn() {
        column( null );
    }

    private void column( String value ) {
        if ( columnsPrinted > 0 ) {
            str.append( "," );
        }
        if ( value != null && value.length() >= 0 ) {
            // do proper CSV escaping of double-quotes. see http://en.wikipedia.org/wiki/Comma-separated_values
            str.append( "\"" ).append( value.replace( "\"", "\"\"" ) ).append( "\"" );
        }
        columnsPrinted++;
    }

    private void betweenLines() {
        if ( columnsPrinted > 0 ) {
            str.append( "\n" );
            columnsPrinted = 0;
        }
    }
}

interface Stringifier<T> {
    public String stringify( T ob );
}

enum Column {
    TITLE( "Title" ),
    DESCRIPTION( "Description" ),
    KEYWORDS( "Keywords" ),
    SUBJECT( "Subject/Discipline" ),
    RESOURCE_TYPE( "Learning Resource Type" ),
    EDUCATION_LEVEL( "Education Level" ),
    LANGUAGE( "Language" ),
    CREATION_DATE( "Creation date" ),
    PUBLICLY_ACCESSIBLE( "Publicly Accessible" ),
    COPYRIGHT( "Copyright" ),
    DRM( "DRM" );

    private String name;

    Column( String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}