/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.authentication.panels;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
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
import edu.colorado.phet.website.util.wicket.ErrorAppender;

public class RegisterPanel extends PhetPanel {

    // TODO: i18n (align the fields correctly within the table!)

    private TextField firstName;
    private TextField lastName;
    private TextField organization;
    private TextField username; // email
    private PasswordTextField password;
    private PasswordTextField passwordCopy;
    private CheckBox receiveEmail;
    private Model errorModel;
    private PageContext context;
    private DropDownChoice country;

    // I am a... checkboxes
    private CheckBox teacherCheckbox;
    private CheckBox studentCheckbox;
    private CheckBox researcherCheckbox;
    private CheckBox translatorCheckbox;
    private CheckBox educatorCheckbox;
    private CheckBox otherRoleCheckbox;
    private TextField otherRole;
    private WebMarkupContainer roleContainer;
    private ErrorAppender roleErrorAppender;

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
    private WebMarkupContainer subjectContainer;
    private ErrorAppender subjectErrorAppender;

    // grades checkboxes
    private CheckBox elementaryCheckbox;
    private CheckBox gradeKCheckbox;
    private CheckBox grade1Checkbox;
    private CheckBox grade2Checkbox;
    private CheckBox grade3Checkbox;
    private CheckBox grade4Checkbox;
    private CheckBox grade5Checkbox;
    private CheckBox middleCheckbox;
    private CheckBox grade6Checkbox;
    private CheckBox grade7Checkbox;
    private CheckBox grade8Checkbox;
    private CheckBox highCheckbox;
    private CheckBox grade9Checkbox;
    private CheckBox grade10Checkbox;
    private CheckBox grade11Checkbox;
    private CheckBox grade12Checkbox;
    private CheckBox universityCheckbox;
    private CheckBox year1Checkbox;
    private CheckBox year2plusCheckbox;
    private CheckBox graduateCheckbox;
    private CheckBox adultEducationCheckbox;
    private CheckBox otherGradeCheckbox;
    private TextField otherGrade;
    private WebMarkupContainer gradeContainer;
    private ErrorAppender gradeErrorAppender;

    // teaching experience
    private TextField yearsTeaching;

    // PhET experience
    private RadioGroup phetExperienceRadioGroup;
    private WebMarkupContainer phetExperienceRadioGroupContainer;
    private ErrorAppender phetExperienceErrorAppender;

    private String destination = null;

    private static final String ERROR_SEPARATOR = "<br/>";

    private static final Logger logger = Logger.getLogger( RegisterPanel.class.getName() );

    FeedbackPanel feedback;

    public RegisterPanel( String id, PageContext context, String destination ) {
        super( id, context );
        this.context = context;

        this.destination = destination;

        add( new RegisterForm( "register-form" ) );

        errorModel = new Model<String>( "" );

        add( new RawLabel( "register-errors", errorModel ) );

        feedback = new FeedbackPanel( "feedback" );
        feedback.setVisible( false );
        add( feedback );
    }

    public final class RegisterForm extends Form {
        private static final long serialVersionUID = 1L;

        private final ValueMap properties = new ValueMap( "receiveEmail=true" );

        public RegisterForm( final String id ) {
            super( id );

            add( firstName = new StringTextField( "firstName", new PropertyModel( properties, "firstName" ) ) );
            add( lastName = new StringTextField( "lastName", new PropertyModel( properties, "lastName" ) ) );
            add( organization = new StringTextField( "organization", new PropertyModel( properties, "organization" ) ) );
            add( username = new StringTextField( "username", new PropertyModel( properties, "username" ) ) );
            add( password = new StringPasswordTextField( "password", new PropertyModel( properties, "password" ) ) );
            add( passwordCopy = new StringPasswordTextField( "passwordCopy", new PropertyModel( properties, "passwordCopy" ) ) );
            add( receiveEmail = new CheckBox( "receiveEmail", new PropertyModel<Boolean>( properties, "receiveEmail" ) ) );
            add( country = new DropDownChoice<String>( "country", new PropertyModel<String>( properties, "country" ), PhetUser.getCountries() ) );

            firstName.add( new ErrorAppender() );
            lastName.add( new ErrorAppender() );
            organization.add( new ErrorAppender() );
            username.add( new ErrorAppender() );
            password.add( new ErrorAppender() );
            passwordCopy.add( new ErrorAppender() );
            receiveEmail.add( new ErrorAppender() );
            country.add( new ErrorAppender() );

            // add role checkboxes
            add( roleContainer = new WebMarkupContainer( "roleContainer" ) );
            roleContainer.add( roleErrorAppender = new ErrorAppender( false ) );
            roleContainer.add( teacherCheckbox = new CheckBox( "teacher", new PropertyModel<Boolean>( properties, "teacher" ) ) );
            roleContainer.add( studentCheckbox = new CheckBox( "student", new PropertyModel<Boolean>( properties, "student" ) ) );
            roleContainer.add( researcherCheckbox = new CheckBox( "researcher", new PropertyModel<Boolean>( properties, "researcher" ) ) );
            roleContainer.add( translatorCheckbox = new CheckBox( "translator", new PropertyModel<Boolean>( properties, "translator" ) ) );
            roleContainer.add( educatorCheckbox = new CheckBox( "educator", new PropertyModel<Boolean>( properties, "educator" ) ) );
            roleContainer.add( otherRoleCheckbox = new CheckBox( "otherRole", new PropertyModel<Boolean>( properties, "otherRole" ) ) );
            roleContainer.add( otherRole = new StringTextField( "otherRoleInput", new PropertyModel( properties, "otherRoleInput" ) ) );

            // add subject checkboxes
            add( subjectContainer = new WebMarkupContainer( "subjectContainer" ) );
            subjectContainer.add( subjectErrorAppender = new ErrorAppender( false ) );
            subjectContainer.add( generalSciencesCheckbox = new CheckBox( "generalSciences", new PropertyModel<Boolean>( properties, "generalSciences" ) ) );
            subjectContainer.add( earthScienceCheckbox = new CheckBox( "earthScience", new PropertyModel<Boolean>( properties, "earthScience" ) ) );
            subjectContainer.add( biologyCheckbox = new CheckBox( "biology", new PropertyModel<Boolean>( properties, "biology" ) ) );
            subjectContainer.add( physicsCheckbox = new CheckBox( "physics", new PropertyModel<Boolean>( properties, "physics" ) ) );
            subjectContainer.add( chemistryCheckbox = new CheckBox( "chemistry", new PropertyModel<Boolean>( properties, "chemistry" ) ) );
            subjectContainer.add( astronomyCheckbox = new CheckBox( "astronomy", new PropertyModel<Boolean>( properties, "astronomy" ) ) );
            subjectContainer.add( mathCheckbox = new CheckBox( "math", new PropertyModel<Boolean>( properties, "math" ) ) );
            subjectContainer.add( otherSubjectCheckbox = new CheckBox( "otherSubject", new PropertyModel<Boolean>( properties, "otherSubject" ) ) );
            subjectContainer.add( otherSubject = new StringTextField( "otherSubjectInput", new PropertyModel( properties, "otherSubjectInput" ) ) );

            // add grade checkboxes
            add( gradeContainer = new WebMarkupContainer( "gradeContainer" ) );
            gradeContainer.add( gradeErrorAppender = new ErrorAppender( false ) );
            gradeContainer.add( elementaryCheckbox = new CheckBox( "elementary", new PropertyModel<Boolean>( properties, "elementary" ) ) );
            gradeContainer.add( gradeKCheckbox = new CheckBox( "gradeK", new PropertyModel<Boolean>( properties, "gradeK" ) ) );
            gradeContainer.add( grade1Checkbox = new CheckBox( "grade1", new PropertyModel<Boolean>( properties, "grade1" ) ) );
            gradeContainer.add( grade2Checkbox = new CheckBox( "grade2", new PropertyModel<Boolean>( properties, "grade2" ) ) );
            gradeContainer.add( grade3Checkbox = new CheckBox( "grade3", new PropertyModel<Boolean>( properties, "grade3" ) ) );
            gradeContainer.add( grade4Checkbox = new CheckBox( "grade4", new PropertyModel<Boolean>( properties, "grade4" ) ) );
            gradeContainer.add( grade5Checkbox = new CheckBox( "grade5", new PropertyModel<Boolean>( properties, "grade5" ) ) );
            gradeContainer.add( middleCheckbox = new CheckBox( "middle", new PropertyModel<Boolean>( properties, "middle" ) ) );
            gradeContainer.add( grade6Checkbox = new CheckBox( "grade6", new PropertyModel<Boolean>( properties, "grade6" ) ) );
            gradeContainer.add( grade7Checkbox = new CheckBox( "grade7", new PropertyModel<Boolean>( properties, "grade7" ) ) );
            gradeContainer.add( grade8Checkbox = new CheckBox( "grade8", new PropertyModel<Boolean>( properties, "grade8" ) ) );
            gradeContainer.add( highCheckbox = new CheckBox( "high", new PropertyModel<Boolean>( properties, "high" ) ) );
            gradeContainer.add( grade9Checkbox = new CheckBox( "grade9", new PropertyModel<Boolean>( properties, "grade9" ) ) );
            gradeContainer.add( grade10Checkbox = new CheckBox( "grade10", new PropertyModel<Boolean>( properties, "grade10" ) ) );
            gradeContainer.add( grade11Checkbox = new CheckBox( "grade11", new PropertyModel<Boolean>( properties, "grade11" ) ) );
            gradeContainer.add( grade12Checkbox = new CheckBox( "grade12", new PropertyModel<Boolean>( properties, "grade12" ) ) );
            gradeContainer.add( universityCheckbox = new CheckBox( "university", new PropertyModel<Boolean>( properties, "university" ) ) );
            gradeContainer.add( year1Checkbox = new CheckBox( "year1", new PropertyModel<Boolean>( properties, "year1" ) ) );
            gradeContainer.add( year2plusCheckbox = new CheckBox( "year2plus", new PropertyModel<Boolean>( properties, "year2plus" ) ) );
            gradeContainer.add( graduateCheckbox = new CheckBox( "graduate", new PropertyModel<Boolean>( properties, "graduate" ) ) );
            gradeContainer.add( adultEducationCheckbox = new CheckBox( "adultEducation", new PropertyModel<Boolean>( properties, "adultEducation" ) ) );
            gradeContainer.add( otherGradeCheckbox = new CheckBox( "otherGrade", new PropertyModel<Boolean>( properties, "otherGrade" ) ) );
            gradeContainer.add( otherGrade = new StringTextField( "otherGradeInput", new PropertyModel( properties, "otherGradeInput" ) ) );

            // teaching experience
            add( yearsTeaching = new StringTextField( "teachingExperience", new PropertyModel( properties, "teachingExperience" ) ) );
            yearsTeaching.add( new ErrorAppender() );

            // phet experience
            add( phetExperienceRadioGroupContainer = new WebMarkupContainer( "phetExperienceContainer" ) );
            phetExperienceRadioGroupContainer.add( phetExperienceErrorAppender = new ErrorAppender( false ) );

            phetExperienceRadioGroup = new RadioGroup( "phetExperienceRadios", new PropertyModel( properties, "phetExperience" ) );
            phetExperienceRadioGroup.add( new ErrorAppender() );

            Radio newUserRadio = new Radio( "newUserRadio", new Model<String>( "NEW_USER" ) );
            phetExperienceRadioGroup.add( newUserRadio );
            phetExperienceRadioGroup.add( new FormComponentLabel( "newUserLabel", newUserRadio ) );

            Radio occasionalUserRadio = new Radio( "occasionalUserRadio", new Model<String>( "OCCASIONAL_USER" ) );
            phetExperienceRadioGroup.add( occasionalUserRadio );
            phetExperienceRadioGroup.add( new FormComponentLabel( "occasionalUserLabel", occasionalUserRadio ) );

            Radio experiencedUserRadio = new Radio( "experiencedUserRadio", new Model<String>( "EXPERIENCED_USER" ) );
            phetExperienceRadioGroup.add( experiencedUserRadio );
            phetExperienceRadioGroup.add( new FormComponentLabel( "experiencedUserLabel", experiencedUserRadio ) );

            Radio powerUserRadio = new Radio( "powerUserRadio", new Model<String>( "POWER_USER" ) );
            phetExperienceRadioGroup.add( powerUserRadio );
            phetExperienceRadioGroup.add( new FormComponentLabel( "powerUserLabel", powerUserRadio ) );

            phetExperienceRadioGroupContainer.add( phetExperienceRadioGroup );

            // so we can respond to the error messages
            password.setRequired( false );
            passwordCopy.setRequired( false );

            add( new AbstractFormValidator() {
                public FormComponent[] getDependentFormComponents() {
                    return new FormComponent[]{firstName, lastName, password, passwordCopy, username, phetExperienceRadioGroup, country,
                            otherRole, otherSubject,

                            // role checkboxes
                            teacherCheckbox, studentCheckbox, researcherCheckbox, translatorCheckbox, otherRoleCheckbox,

                            // subject checkboxes
                            generalSciencesCheckbox, earthScienceCheckbox, biologyCheckbox, physicsCheckbox, chemistryCheckbox,
                            astronomyCheckbox, mathCheckbox, otherSubjectCheckbox,

                            // grade checkboxes
                            elementaryCheckbox, gradeKCheckbox, grade1Checkbox, grade2Checkbox, grade3Checkbox, grade4Checkbox,
                            grade5Checkbox, middleCheckbox, grade6Checkbox, grade7Checkbox, grade8Checkbox, highCheckbox, grade9Checkbox, grade10Checkbox,
                            grade11Checkbox, grade12Checkbox, universityCheckbox, year1Checkbox, year2plusCheckbox, graduateCheckbox, otherGradeCheckbox
                    };
                }

                public void validate( Form form ) {
                    phetExperienceRadioGroup.validate();
                    phetExperienceRadioGroup.updateModel();

                    if ( firstName.getInput() == null || firstName.getInput().length() == 0 ) {
                        error( firstName, "validation.user.firstName" );
                    }

                    if ( lastName.getInput() == null || lastName.getInput().length() == 0 ) {
                        error( lastName, "validation.user.lastName" );
                    }

                    if ( !password.getInput().equals( passwordCopy.getInput() ) ) {
                        error( password, "validation.user.passwordMatch" );
                    }

                    if ( password.getInput().length() == 0 ) {
                        error( password, "validation.user.password" );
                    }

                    String err = PhetUser.validateEmail( username.getInput() );
                    if ( err != null ) {
                        error( username, "validation.user.email" );
                    }

                    if ( phetExperienceRadioGroup.getModelObject() == null ) {
                        error( phetExperienceRadioGroup, "validation.user.phetExperience" );
                        phetExperienceErrorAppender.isValid = false;
                    }
                    else {
                        phetExperienceErrorAppender.isValid = true;
                    }

                    if ( otherRoleCheckbox.getConvertedInput() && ( otherRole.getInput() == null || otherRole.getInput().length() == 0 ) ) {
                        error( otherRole, "validation.user.otherRole" );
                    }

                    if ( otherSubjectCheckbox.getConvertedInput() && ( otherSubject.getInput() == null || otherSubject.getInput().length() == 0 ) ) {
                        error( otherSubject, "validation.user.otherSubject" );
                    }

                    if ( country.getInput().length() == 0 ) {
                        error( country, "validation.user.country" );
                    }

                    if ( !( teacherCheckbox.getConvertedInput() || studentCheckbox.getConvertedInput() || researcherCheckbox.getConvertedInput() ||
                            translatorCheckbox.getConvertedInput() || otherRoleCheckbox.getConvertedInput() ) ) {
                        error( teacherCheckbox, "validation.user.role" );
                        roleErrorAppender.isValid = false;
                    }
                    else {
                        roleErrorAppender.isValid = true;
                    }

                    if ( !( generalSciencesCheckbox.getConvertedInput() || earthScienceCheckbox.getConvertedInput() || biologyCheckbox.getConvertedInput() ||
                            physicsCheckbox.getConvertedInput() || chemistryCheckbox.getConvertedInput() || astronomyCheckbox.getConvertedInput() ||
                            mathCheckbox.getConvertedInput() || otherSubjectCheckbox.getConvertedInput() ) ) {
                        error( generalSciencesCheckbox, "validation.user.subject" );
                        subjectErrorAppender.isValid = false;
                    }
                    else {
                        subjectErrorAppender.isValid = true;
                    }

                    if ( !( elementaryCheckbox.getConvertedInput() || gradeKCheckbox.getConvertedInput() || grade1Checkbox.getConvertedInput() ||
                            grade2Checkbox.getConvertedInput() || grade3Checkbox.getConvertedInput() || grade4Checkbox.getConvertedInput() ||
                            grade5Checkbox.getConvertedInput() || middleCheckbox.getConvertedInput() || grade6Checkbox.getConvertedInput() ||
                            grade7Checkbox.getConvertedInput() || grade8Checkbox.getConvertedInput() || highCheckbox.getConvertedInput() ||
                            grade9Checkbox.getConvertedInput() || grade10Checkbox.getConvertedInput() || grade11Checkbox.getConvertedInput() ||
                            grade12Checkbox.getConvertedInput() || universityCheckbox.getConvertedInput() || year1Checkbox.getConvertedInput() ||
                            year2plusCheckbox.getConvertedInput() || graduateCheckbox.getConvertedInput() || otherGradeCheckbox.getConvertedInput() ) ) {
                        error( generalSciencesCheckbox, "validation.user.grade" );
                        gradeErrorAppender.isValid = false;
                    }
                    else {
                        gradeErrorAppender.isValid = true;
                    }

                    if ( !Pattern.matches( "^[0-9]*$", yearsTeaching.getInput() ) ) {
                        error( yearsTeaching, "validation.user.yearsTeaching" );
                    }
                }
            } );
        }

        @Override
        protected void onValidate() {
            super.onValidate();
            feedback.setVisible( feedback.anyMessage() );
        }

        public final void onSubmit() {
            Session session = getHibernateSession();

            boolean error = false;
            String errorString = "";

            String nom = firstName.getModelObject().toString() + " " + lastName.getModelObject().toString();
            String org = organization.getModelObject().toString();
            String email = username.getModelObject().toString();
            String pass = password.getInput();
            String desc = phetExperienceRadioGroup.getModelObject().toString();
            String confirmationKey = null;
            boolean receiveNewsletters = receiveEmail.getModelObject();

            logger.warn( "name: " + nom );
            logger.warn( "org: " + org );
            logger.warn( "desc: " + desc );

            PhetUser user = null;

            boolean update = false;

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
                            update = true;
                        }
                    }
                    else {
                        user = new PhetUser( email, false );
                    }

                    confirmationKey = user.getConfirmationKey();
                    user.setName( nom );
                    user.setOrganization( org );
                    user.setDescription( desc );
                    user.setPassword( pass, email );
                    user.setReceiveEmail( receiveNewsletters );

                    user.setTeacher( teacherCheckbox.getModelObject() );
                    user.setStudent( studentCheckbox.getModelObject() );
                    user.setResearcher( researcherCheckbox.getModelObject() );
                    user.setTranslator( translatorCheckbox.getModelObject() );
                    user.setOtherRole( otherRoleCheckbox.getModelObject() );
                    user.setOtherRoleText( otherRole.getModelObject().toString() );

                    user.setGeneralScience( generalSciencesCheckbox.getModelObject() );
                    user.setEarthScience( earthScienceCheckbox.getModelObject() );
                    user.setBiology( biologyCheckbox.getModelObject() );
                    user.setPhysics( physicsCheckbox.getModelObject() );
                    user.setChemistry( chemistryCheckbox.getModelObject() );
                    user.setAstronomy( astronomyCheckbox.getModelObject() );
                    user.setMath( mathCheckbox.getModelObject() );
                    user.setOtherSubject( otherSubjectCheckbox.getModelObject() );
                    user.setOtherSubjectText( otherSubject.getModelObject().toString() );

                    user.setElementary( elementaryCheckbox.getModelObject() );
                    user.setGradeK( gradeKCheckbox.getModelObject() );
                    user.setGrade1( grade1Checkbox.getModelObject() );
                    user.setGrade2( grade2Checkbox.getModelObject() );
                    user.setGrade3( grade3Checkbox.getModelObject() );
                    user.setGrade4( grade4Checkbox.getModelObject() );
                    user.setGrade5( grade5Checkbox.getModelObject() );
                    user.setMiddle( middleCheckbox.getModelObject() );
                    user.setGrade7( grade6Checkbox.getModelObject() );
                    user.setGrade8( grade7Checkbox.getModelObject() );
                    user.setGrade9( grade8Checkbox.getModelObject() );
                    user.setHigh( highCheckbox.getModelObject() );
                    user.setGrade9( grade9Checkbox.getModelObject() );
                    user.setGrade10( grade10Checkbox.getModelObject() );
                    user.setGrade11( grade11Checkbox.getModelObject() );
                    user.setGrade12( grade12Checkbox.getModelObject() );
                    user.setUniversity( universityCheckbox.getModelObject() );
                    user.setYear1( year1Checkbox.getModelObject() );
                    user.setYear2plus( year2plusCheckbox.getModelObject() );
                    user.setGraduate( graduateCheckbox.getModelObject() );
                    user.setOtherGrade( otherGradeCheckbox.getModelObject() );

                    user.setYearsTeaching( yearsTeaching.getModelObject().toString() );

                    if ( update ) {
                        session.update( user );
                    }
                    else {
                        session.save( user );
                    }

                    tx.commit();
                }
                catch( RuntimeException e ) {
                    logger.warn( e );
                    if ( tx != null && tx.isActive() ) {
                        try {
                            tx.rollback();
                        }
                        catch( HibernateException e1 ) {
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