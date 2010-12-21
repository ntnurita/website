package edu.colorado.phet.website.services;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.hibernate.Session;

import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Simulation;
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
            public Void run( Session session ) {
                List list = session.createQuery( "select s from Simulation as s" ).list();
                List<Simulation> simulations = new LinkedList<Simulation>( list );
                for ( Simulation simulation : simulations ) {
                    LocalizedSimulation english = simulation.getEnglishSimulation();
                    for ( Column column : Column.values() ) {
                        switch( column ) {
                            case TITLE:
                                column( english.getTitle() );
                                break;
                            case DESCRIPTION:
                                column( PhetLocalizer.get().getDefaultString( session, simulation.getDescriptionKey(), "", true ) );
                                break;
                            case KEYWORDS:
                                emptyColumn();
                                break;
                            case SUBJECT:
                                emptyColumn();
                                break;
                            case RESOURCE_TYPE:
                                emptyColumn();
                                break;
                            case EDUCATION_LEVEL:
                                emptyColumn();
                                break;
                            case LANGUAGE:
                                emptyColumn();
                                break;
                            case CREATION_DATE:
                                emptyColumn();
                                break;
                            case PUBLICLY_ACCESSIBLE:
                                emptyColumn();
                                break;
                            case COPYRIGHT:
                                emptyColumn();
                                break;
                            case DRM:
                                emptyColumn();
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