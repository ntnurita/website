/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.authentication.SignInPage;
import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.notification.NotificationHandler;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

public class CollaborationRequestPage extends TranslationPage {

    private int translationId;

    public static final String TRANSLATION_ID = "translationId";
    private static final Logger logger = Logger.getLogger( CollaborationRequestPage.class.getName() );

    public CollaborationRequestPage( PageParameters parameters ) {
        super( parameters );

        if ( !PhetSession.get().isSignedIn() ) {
            throw new RestartResponseAtInterceptPageException( SignInPage.class );
        }

        translationId = Integer.parseInt( parameters.getString( TRANSLATION_ID ) );

        add( TranslationMainPage.getLinker().getLink( "return", getPageContext(), getPhetCycle() ) );

        add( new RequestForm( "request-form" ) );
    }

    public class RequestForm extends Form {

        private TextArea<String> message;

        public RequestForm( String id ) {
            super( id );

            add( message = new TextArea<String>( "message", new Model<String>() ) );
        }

        @Override
        protected void onSubmit() {
            boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                public boolean run( Session session ) {
                    Translation t = (Translation) session.load( Translation.class, translationId );

                    List<PhetUser> users = new LinkedList<PhetUser>();
                    for ( Object u : t.getAuthorizedUsers() ) {
                        users.add( (PhetUser) u );
                    }

                    PhetUser currentUser = PhetSession.get().getUser();

                    if ( !DistributionHandler.allowNotificationEmails( PhetRequestCycle.get() ) ) {
                        return false; // on dev server, ignore this
                    }

                    return NotificationHandler.sendTranslationRequestForCollaboration( translationId, t.getLocale(), users, currentUser, message.getModelObject() );
                }
            } );

            if ( success ) {
                setResponsePage( CollaborationRequestSubmittedPage.class );
            }
        }
    }

}
