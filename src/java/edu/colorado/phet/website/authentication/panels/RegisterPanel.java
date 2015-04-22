/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.authentication.panels;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.components.StringPasswordTextField;
import edu.colorado.phet.website.components.StringTextField;
import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.newsletter.ConfirmEmailSentPage;
import edu.colorado.phet.website.newsletter.NewsletterUtils;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

public class RegisterPanel extends PhetPanel {

    // TODO: i18n (align the fields correctly within the table!)

    // TODO: use regular form validation, not this hacked version that was done before I knew about form validation feedback

    private TextField firstName;
    private TextField lastName;
    private TextField organization;
    private TextField username; // email
    private PasswordTextField password;
    private PasswordTextField passwordCopy;
    private DropDownChoice<String> description;
    private CheckBox receiveEmail;
    private Model errorModel;
    private PageContext context;

    // I am a... checkboxes
    private CheckBox teacherCheckbox;
    private CheckBox studentCheckbox;
    private CheckBox researcherCheckbox;
    private CheckBox translatorCheckbox;
    private CheckBox otherRoleCheckbox;
    private TextField otherRole;

    // Subjects checkboxes
    private CheckBox generalSciencesCheckbox;
    private CheckBox earthScienceCheckbox;
    private CheckBox biologyCheckbox;
    private CheckBox physicsCheckbox;
    private CheckBox chemistryCheckbox;
    private CheckBox astronomyCheckbox;
    private CheckBox mathCheckbox;
    private CheckBox otherSubjectCheckbox;
    private TextField otherSubject;


    private String destination = null;

    private static final String ERROR_SEPARATOR = "<br/>";

    private static final Logger logger = Logger.getLogger( RegisterPanel.class.getName() );

    public RegisterPanel( String id, PageContext context, String destination ) {
        super( id, context );
        this.context = context;

        this.destination = destination;

        add( new RegisterForm( "register-form" ) );

        errorModel = new Model<String>( "" );

        add( new RawLabel( "register-errors", errorModel ) );
    }

    public final class RegisterForm extends Form {
        private static final long serialVersionUID = 1L;

        private final ValueMap properties = new ValueMap( "receiveEmail=true" );

        public RegisterForm( final String id ) {
            super( id );

            add( firstName = new StringTextField( "firstName", new PropertyModel( properties, "firstName" ) ) );
            add( lastName = new StringTextField( "lastName", new PropertyModel( properties, "lastName" ) ) );
            add( organization = new StringTextField( "organization", new PropertyModel( properties, "organization" ) ) );
//            add( description = new DropDownChoice<String>( "description", new PropertyModel<String>( properties, "description" ), PhetUser.getDescriptionOptions() ) );
            add( username = new StringTextField( "username", new PropertyModel( properties, "username" ) ) );
            add( password = new StringPasswordTextField( "password", new PropertyModel( properties, "password" ) ) );
            add( passwordCopy = new StringPasswordTextField( "passwordCopy", new PropertyModel( properties, "passwordCopy" ) ) );
//            add( receiveEmail = new CheckBox( "receiveEmail", new PropertyModel<Boolean>( properties, "receiveEmail" ) ) );

            // add role checkboxes
            add( teacherCheckbox = new CheckBox( "teacher", new PropertyModel<Boolean>( properties, "teacher" ) ) );
            add( studentCheckbox = new CheckBox( "student", new PropertyModel<Boolean>( properties, "student" ) ) );
            add( researcherCheckbox = new CheckBox( "researcher", new PropertyModel<Boolean>( properties, "researcher" ) ) );
            add( translatorCheckbox = new CheckBox( "translator", new PropertyModel<Boolean>( properties, "translator" ) ) );
            add( otherRoleCheckbox = new CheckBox( "otherRole", new PropertyModel<Boolean>( properties, "otherRole" ) ) );
            add( otherRole = new StringTextField( "otherRoleInput", new PropertyModel( properties, "otherRoleInput" ) ) );

            // add subject checkboxes
            add( generalSciencesCheckbox = new CheckBox( "generalSciences", new PropertyModel<Boolean>( properties, "generalSciences" ) ) );
            add( earthScienceCheckbox = new CheckBox( "earthScience", new PropertyModel<Boolean>( properties, "earthScience" ) ) );
            add( biologyCheckbox = new CheckBox( "biology", new PropertyModel<Boolean>( properties, "biology" ) ) );
            add( physicsCheckbox = new CheckBox( "physics", new PropertyModel<Boolean>( properties, "physics" ) ) );
            add( chemistryCheckbox = new CheckBox( "chemistry", new PropertyModel<Boolean>( properties, "chemistry" ) ) );
            add( astronomyCheckbox = new CheckBox( "astronomy", new PropertyModel<Boolean>( properties, "astronomy" ) ) );
            add( mathCheckbox = new CheckBox( "math", new PropertyModel<Boolean>( properties, "math" ) ) );
            add( otherSubjectCheckbox = new CheckBox( "otherSubject", new PropertyModel<Boolean>( properties, "otherSubject" ) ) );
            add( otherSubject = new StringTextField( "otherSubjectInput", new PropertyModel( properties, "otherSubjectInput" ) ) );

            // so we can respond to the error messages
            password.setRequired( false );
            passwordCopy.setRequired( false );
        }

        public final void onSubmit() {
            Session session = getHibernateSession();

            boolean error = false;
            String errorString = "";
            String err = null;

            String nom = firstName.getModelObject().toString() + " " + lastName.getModelObject().toString();
            String org = organization.getModelObject().toString();
            String email = username.getModelObject().toString();
            String pass = password.getInput();
            String desc = description.getModelObject();
            String confirmationKey = null;
            boolean receiveNewsletters = receiveEmail.getModelObject();

            logger.debug( "name: " + nom );
            logger.debug( "org: " + org );
            logger.debug( "desc: " + desc );

            if ( nom == null || nom.length() == 0 ) {
                error = true;
                errorString += ERROR_SEPARATOR + getPhetLocalizer().getString( "validation.user.user", this, "Please fill in the name field" );
            }

            if ( !pass.equals( passwordCopy.getInput() ) ) {
                error = true;
                errorString += ERROR_SEPARATOR + getPhetLocalizer().getString( "validation.user.passwordMatch", this, "The entered passwords do not match" );
            }

            if ( pass.length() == 0 ) {
                error = true;
                errorString += ERROR_SEPARATOR + getPhetLocalizer().getString( "validation.user.password", this, "Please enter a password" );
            }

            err = PhetUser.validateEmail( email );
            if ( err != null ) {
                error = true;
                errorString += ERROR_SEPARATOR + getPhetLocalizer().getString( "validation.user.email", this, "Please enter a valid email address" );
            }

            if ( desc == null || desc.length() == 0 ) {
                error = true;
                errorString += ERROR_SEPARATOR + getPhetLocalizer().getString( "validation.user.description", this, "Please pick a description" );
            }

            PhetUser user = null;

            // TODO: a bunch of refactoring and cleanup around here
            if ( !error ) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();

                    List users = session.createQuery( "select u from PhetUser as u where u.email = :email" ).setString( "email", email ).list();
                    if ( !users.isEmpty() ) {
                        if ( users.size() > 1 ) {
                            throw new RuntimeException( "More than one user for email " + email );
                        }
                        user = (PhetUser) users.get( 0 );
                        if ( !user.isNewsletterOnlyAccount() && !user.isConfirmed() ) {
                            error = true;
                            errorString += ERROR_SEPARATOR + getPhetLocalizer().getString( "validation.user.emailUsed", this, "That email address is already in use" );
                        }
                        else {
                            // overwrite newsletter user! (but reset "confirmed")
                            user.setNewsletterOnlyAccount( false );
                            user.setConfirmed( false );
                            user.setConfirmationKey( PhetUser.generateConfirmationKey() ); // regenerate
                            confirmationKey = user.getConfirmationKey();
                            user.setName( nom );
                            user.setOrganization( org );
                            user.setDescription( desc );
                            user.setPassword( pass, email );
                            user.setReceiveEmail( receiveNewsletters );
                            session.update( user );
                        }
                    }
                    else {
                        user = new PhetUser( email, false );
                        confirmationKey = user.getConfirmationKey();
                        user.setName( nom );
                        user.setOrganization( org );
                        user.setDescription( desc );
                        user.setPassword( pass, email );
                        user.setReceiveEmail( receiveNewsletters );
                        session.save( user );
                    }

                    tx.commit();
                }
                catch ( RuntimeException e ) {
                    logger.warn( e );
                    if ( tx != null && tx.isActive() ) {
                        try {
                            tx.rollback();
                        }
                        catch ( HibernateException e1 ) {
                            logger.error( "ERROR: Error rolling back transaction", e1 );
                        }
                        throw e;
                    }
                    error = true;
                    errorString += ERROR_SEPARATOR + getPhetLocalizer().getString( "error.internalError", this, "Internal error occurred" );
                }
            }

            if ( !error ) {
                error = !NewsletterUtils.sendConfirmRegisterEmail( context, email, confirmationKey, destination );
            }

            if ( error ) {
                logger.error( "Error registering" );
                logger.error( "Reason: " + errorString );
                errorString = getPhetLocalizer().getString( "validation.user.problems", this, "Please fix the following problems with the form:" ) + "<br/>" + errorString;
                errorModel.setObject( errorString );
            }
            else {
                errorModel.setObject( "" );
                setResponsePage( ConfirmEmailSentPage.class, ConfirmEmailSentPage.getParameters( user ) );
            }
        }
    }

}