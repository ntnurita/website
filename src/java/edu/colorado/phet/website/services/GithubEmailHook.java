// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.services;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.io.IOUtils;

import edu.colorado.phet.website.util.EmailUtils;
import edu.colorado.phet.website.util.PhetRequestCycle;

/**
 * Returns a simple list of locales for all of the visible translations, separated by commas (no spaces)
 */
public class GithubEmailHook extends WebPage {

    private static final Logger logger = Logger.getLogger( GithubEmailHook.class );

    public GithubEmailHook( PageParameters parameters ) {
        super( parameters );

//        /assert( parameters.getString( "email" ).equals( "<INSERT TOKEN HERE>" ) );
        String email = parameters.getString( "email" );


        logger.info( "sending github notification to " + email );
        try {
            HttpServletRequest httpServletRequest = ( (PhetRequestCycle) getRequestCycle() ).getWebRequest().getHttpServletRequest();

            assert ( httpServletRequest.getMethod().equals( "POST" ) );

            InputStream postDataStream = httpServletRequest.getInputStream();
            String postData = IOUtils.toString( postDataStream );
            EmailUtils.GeneralEmailBuilder message = new EmailUtils.GeneralEmailBuilder( "PhET Github Hook Test", "phetmail@colorado.edu" );
            message.setFromName( "PhET Interactive Simulations" );
            String body = postData;
            String textBody = postData;
            message.setBody( body );
            message.setPlainTextAlternative( textBody );
            message.addRecipient( email );
            message.addReplyTo( "phetmail@colorado.edu" );
            EmailUtils.sendMessage( message );
        }
        catch( Exception e ) {
            logger.warn( "message send error: ", e );
        }
    }
}
