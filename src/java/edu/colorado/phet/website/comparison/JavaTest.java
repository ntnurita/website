package edu.colorado.phet.website.comparison;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.hibernate.Session;

import edu.colorado.phet.website.data.Project;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.hibernate.VoidTask;

import static edu.colorado.phet.website.util.hibernate.HibernateUtils.wrapTransaction;
import static java.util.Collections.sort;

/**
 * This is our test Java page. It will show some text, probably from the database
 */
public class JavaTest extends WebPage {
    public JavaTest( PageParameters parameters ) {
        super( parameters );

        // enter into a database transaction
        wrapTransaction( PhetRequestCycle.get().getHibernateSession(), new VoidTask() {
            public Void run( Session session ) {

                // get a list of projects from the database
                List projects = session.createQuery( "select p from Project as p" ).list();

                // extract the project names from the projects
                List<String> projectNames = new LinkedList<String>();
                for ( Object project : projects ) {
                    projectNames.add( ( (Project) project ).getName() );
                }

                // sort the project names
                sort( projectNames );

                // create a list of newline-separated project names
                String result = "";
                for ( String projectName : projectNames ) {
                    if ( result.length() > 0 ) {
                        result += "\n";
                    }
                    result += projectName;
                }

                // add the list to the output
                add( new Label( "text", result ) );

                return null;
            }
        } );
    }
}
