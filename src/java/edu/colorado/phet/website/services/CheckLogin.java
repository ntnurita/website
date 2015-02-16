package edu.colorado.phet.website.services;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;

import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.data.PhetUser;

public class CheckLogin extends WebPage {
    public CheckLogin( PageParameters parameters ) {
        PhetSession psession = PhetSession.get();
        PhetUser user = psession.getUser();

        if ( user != null ) {
            String username = "\"username\": \"" + user.getName() + "\"";
            String email = "\"email\": \"" + user.getEmail() + "\"";
            String userId = "\"userId\": \"" + user.getId() + "\"";
            String signedIn = "\"loggedIn\": \"" + psession.isSignedIn() + "\"";
            add( new RawLabel( "data", "{ " + username + ", " + userId + ", " + email + ", " + signedIn + " }") {{
                setRenderBodyOnly( true );
            }} );
        }
        else {
            add( new RawLabel( "data", "{ \"loggedIn\": \"false\" }" )  {{
                setRenderBodyOnly( true );
            }} );
        }
    }
}
