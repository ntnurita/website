// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.content.about;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.hibernate.Session;

import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class AboutTeamPanel extends PhetPanel {
    public AboutTeamPanel( String id, PageContext context ) {
        super( id, context );

        final List<PhetUser> members = new LinkedList<PhetUser>();

        HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
            public boolean run( Session session ) {
                List list = session.createQuery( "select u from PhetUser as u where u.teamMember = true" ).list();
                for ( Object o : list ) {
                    members.add( (PhetUser) o );
                }
                return true;
            }
        } );

        Collections.sort( members, new Comparator<PhetUser>() {
            public int compare( PhetUser a, PhetUser b ) {
                return a.getName().compareTo( b.getName() );
            }
        } );

        final boolean isAdmin = PhetSession.get().isSignedIn() && PhetSession.get().getUser().isTeamMember();

        add( new ListView<PhetUser>( "person", members ) {
            protected void populateItem( ListItem<PhetUser> item ) {
                PhetUser user = item.getModelObject();
                item.add( new Label( "name", user.getName() ) );
                item.add( new Label( "title", user.getJobTitle() ) );
                if ( isAdmin ) {
                    Label marker = new Label( "show-admin", "" );
                    marker.setRenderBodyOnly( true );
                    item.add( marker );
                    item.add( new Label( "primary-phone", user.getPhone1() ) );
                    item.add( new Label( "secondary-phone", user.getPhone2() ) );
                }
                else {
                    item.add( new InvisibleComponent( "show-admin" ) );
                    item.add( new InvisibleComponent( "primary-phone" ) );
                    item.add( new InvisibleComponent( "secondary-phone" ) );
                }
            }
        } );
    }

    public static String getKey() {
        return "about.team";
    }

    public static String getUrl() {
        return "about/team";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }
}
