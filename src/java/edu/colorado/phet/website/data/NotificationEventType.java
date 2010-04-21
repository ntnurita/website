package edu.colorado.phet.website.data;

import org.apache.wicket.PageParameters;
import org.hibernate.Session;

import edu.colorado.phet.website.content.contribution.ContributionPage;
import edu.colorado.phet.website.data.contribution.Contribution;
import edu.colorado.phet.website.util.HibernateTask;
import edu.colorado.phet.website.util.HibernateUtils;

public enum NotificationEventType {
    NEW_CONTRIBUTION;

    public String toString( String data ) {
        // store params as "a=1&b=1" etc.
        PageParameters params = new PageParameters( data );

        switch( this ) {
            case NEW_CONTRIBUTION:
                final int id = params.getInt( "contribution_id" );
                final String contribhref = "href=\"" + ContributionPage.getLinker( id ).getDefaultRawUrl() + "\"";
                final String[] ret = new String[1];
                boolean success = HibernateUtils.wrapSession( new HibernateTask() {
                    public boolean run( Session session ) {
                        Contribution contribution = (Contribution) session.load( Contribution.class, id );
                        ret[0] = "<li>Contribution created: <a " + contribhref + ">" + contribution.getTitle() + "</a> by user " + contribution.getPhetUser().getEmail() + "</li>";
                        return true;
                    }
                } );
                if ( !success ) {
                    ret[0] = "Contribution #" + id + " added, but error encountered. Maybe the contribution was deleted?";
                    ret[0] += " Try <a href=\"" + contribhref + "\">this link</a>";
                }
                return ret[0];
            default:
                return "Unidentified event";
        }
    }
}