// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.admin.stats;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.hibernate.Session;

import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.admin.AdminPage;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * Shows "share" statistics about certain website pages and simulations, pulled from the Facebook Graph API
 */
public class FacebookStatisticsPage extends AdminPage {

    private static final Logger logger = Logger.getLogger( FacebookStatisticsPage.class.getName() );

    public FacebookStatisticsPage( PageParameters parameters ) {
        super( parameters );

        // get a sorted list of simulation names that we can query with
        final List<String> simNames = new ArrayList<String>();
        HibernateUtils.wrapTransaction( getHibernateSession(), new VoidTask() {
            public Void run( Session session ) {
                final List simList = session.createQuery( "select s from Simulation as s" ).list();

                for ( Object o : simList ) {
                    Simulation sim = (Simulation) o;
                    if ( sim.isVisible() ) {
                        simNames.add( sim.getName() );
                    }
                }
                return null;
            }
        } );
        Collections.sort( simNames );

        // show simulation statistics
        add( new ListView<String>( "sim-stats", simNames ) {
            @Override protected void populateItem( ListItem<String> item ) {
                item.add( new Label( "name", item.getModelObject() ) );
                item.add( new Label( "shares", "" + getFacebookSharesForPath( "/en/simulation/" + item.getModelObject() ) ) );
            }
        } );

        // show per-language statistics
        add( new ListView<String>( "language-stats", PhetWicketApplication.get().getTranslationLocaleStrings() ) {
            @Override protected void populateItem( ListItem<String> item ) {
                item.add( new Label( "name", item.getModelObject() ) );
                item.add( new Label( "shares", "" + getFacebookSharesForPath( "/" + item.getModelObject() + "/" ) ) );
            }
        } );

        // show per-page statistics
        add( new Label( "home-page", "" + getFacebookSharesForPath( "" ) ) );
        add( new Label( "new-sims-page", "" + getFacebookSharesForPath( "/en/simulations/category/new" ) ) );
    }

    public static int getFacebookSharesForPath( String relativePath ) {
        try {
            // TODO: check non-English shares?
            URL url = new URL( "http://graph.facebook.com/http://phet.colorado.edu" + relativePath );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod( "GET" );
            conn.setDoInput( true );

            BufferedReader reader = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );

            String line = reader.readLine().trim();

            // this uses a poor-man's JSON parser for now.
            final String shareString = "\"shares\":";
            int index = line.indexOf( shareString );
            if ( index != -1 ) {
                index += shareString.length();
                String parseString = line.substring( index, line.indexOf( "}", index ) );
                return Integer.parseInt( parseString );
            }
            else {
                return 0;
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
            return -1;
        }
    }

}