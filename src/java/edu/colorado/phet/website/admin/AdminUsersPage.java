/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.admin;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.newsletter.NewsletterUtils;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.SimpleTask;
import edu.colorado.phet.website.util.hibernate.VoidTask;

public class AdminUsersPage extends AdminPage {

    private static final Logger logger = Logger.getLogger( AdminUsersPage.class.getName() );

    public AdminUsersPage( PageParameters parameters ) {
        super( parameters );

        final List<PhetUser> teamMembers = new LinkedList<PhetUser>();

        HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
            public boolean run( Session session ) {
                List list = session.createQuery( "select u from PhetUser as u where u.teamMember = true" ).list();
                for ( Object o : list ) {
                    teamMembers.add( (PhetUser) o );
                }
                return true;
            }
        } );

        add( new ListView<PhetUser>( "member", teamMembers ) {
            protected void populateItem( ListItem<PhetUser> item ) {
                PhetUser user = item.getModelObject();
                Link link = AdminEditProfilePage.getLinker( user ).getLink( "member-link", getPageContext(), getPhetCycle() );
                item.add( link );
                link.add( new Label( "email", user.getEmail() ) );
            }
        } );

        add( new UserEmailForm( "edit-user-profile-form" ) {
            @Override
            protected void onSubmit() {
                PageParameters params = new PageParameters();
                params.add( AdminEditProfilePage.USER_EMAIL, getEmailAddress() );
                setResponsePage( AdminEditProfilePage.class, params );
            }
        } );

        add( new UserEmailForm( "deactivate-form" ) {
            @Override
            protected void onSubmit() {
                HibernateUtils.wrapTransaction( PhetRequestCycle.get().getHibernateSession(), new SimpleTask() {
                    public void run( Session session ) {
                        PhetUser user = (PhetUser) session.createQuery( "select u from PhetUser as u where u.email = :email" )
                                .setString( "email", getEmailAddress() ).uniqueResult();
                        user.setConfirmed( false );
                        session.update( user );
                    }
                } );
            }
        } );

        add( new UserEmailForm( "delete-form" ) {
            @Override
            protected void onSubmit() {
                HibernateUtils.wrapTransaction( PhetRequestCycle.get().getHibernateSession(), new SimpleTask() {
                    public void run( Session session ) {
                        PhetUser user = (PhetUser) session.createQuery( "select u from PhetUser as u where u.email = :email" )
                                .setString( "email", getEmailAddress() ).uniqueResult();

                        { // delete password requests
                            List reqs = session.createQuery( "select r from ResetPasswordRequest as r where r.phetUser = :user" )
                                    .setEntity( "user", user ).list();
                            for ( Object req : reqs ) {
                                session.delete( req );
                            }
                        }

                        session.delete( user );
                    }
                } );
            }
        } );

        add( new SubscribeUsersForm( "subscribe-form" ) );
        add( new UnsubscribeUsersForm( "unsubscribe-form" ) );

    }

    private abstract static class UserEmailForm extends Form {
        private TextField<String> emailField;

        public UserEmailForm( String id ) {
            super( id );

            emailField = new TextField<String>( "email", new Model<String>( "" ) );
            add( emailField );
        }

        public String getEmailAddress() {
            return emailField.getModelObject();
        }
    }

    private class SubscribeUsersForm extends Form {
        private TextArea<String> emailArea;

        private SubscribeUsersForm( String id ) {
            super( id );

            add( emailArea = new TextArea<String>( "emails", new Model<String>( "" ) ) );
        }

        @Override
        protected void onSubmit() {
            String emailText = emailArea.getModelObject();
            logger.info( "Attempting to subscribe a list of users:\n" + emailText );
            for ( String email : emailText.split( "\n" ) ) {
                NewsletterUtils.subscribeUserWithoutEmail( getPageContext(), getHibernateSession(), email.trim(), true );
            }
        }
    }

    private class UnsubscribeUsersForm extends Form {
        private TextArea<String> emailArea;

        private UnsubscribeUsersForm( String id ) {
            super( id );

            add( emailArea = new TextArea<String>( "emails", new Model<String>( "" ) ) );
        }

        @Override
        protected void onSubmit() {
            final String emailText = emailArea.getModelObject();
            logger.info( "Attempting to manually unsubscribe a list of users:\n" + emailText );
            HibernateUtils.wrapCatchTransaction( getHibernateSession(), new SimpleTask() {
                public void run( Session session ) {
                    for ( String rawEmail : emailText.split( "\n" ) ) {
                        String email = rawEmail.trim();
                        List users = session.createQuery( "select u from PhetUser as u where u.email = :email" )
                                .setString( "email", email ).list();
                        if ( !users.isEmpty() ) {
                            PhetUser user = (PhetUser) users.get( 0 );
                            if ( user.isReceiveEmail() ) {
                                logger.info( "manually unsubscribing " + user.getEmail() );
                                user.setReceiveEmail( false );
                                session.update( user );
                            }
                        }
                    }
                }
            } );
        }
    }
}