/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.authentication.panels;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
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

import edu.colorado.phet.website.authentication.PhetSession;
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
    private CountryStateDropdownPanel countryStatePanel;

    // I am a... checkboxes
    private CheckBox teacherCheckbox;
    private CheckBox studentCheckbox;
    private CheckBox researcherCheckbox;
    private CheckBox translatorCheckbox;
    private CheckBox educatorCheckbox;
    private CheckBox otherRoleCheckbox;
    private TextField otherRole;
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
    private ErrorAppender gradeErrorAppender;

    // teaching experience
    private RadioGroup teachingExperienceRadioGroup;
    private ErrorAppender teachingExperienceErrorAppender;

    // PhET experience
    private RadioGroup phetExperienceRadioGroup;
    private ErrorAppender phetExperienceErrorAppender;

    private String destination = null;
    private boolean updateProfile;

    private static final String ERROR_SEPARATOR = "<br/>";

    private static final Logger logger = Logger.getLogger( RegisterPanel.class.getName() );

    FeedbackPanel feedback;

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String ORGANIZATION = "organization";
    public static final String RECEIVE_EMAIL = "receiveEmail";
    public static final String EMAIL = "username";
    public static final String PASSWORD = "password";
    public static final String PASSWORD_COPY = "passwordCopy";

    public static final String TEACHER = "teacher";
    public static final String STUDENT = "student";
    public static final String RESEARCHER = "researcher";
    public static final String TRANSLATOR = "translator";
    public static final String TEACHER_EDUCATOR = "teacherEducator";
    public static final String OTHER_ROLE = "otherRole";
    public static final String OTHER_ROLE_INPUT = "otherRoleInput";

    public static final String GENERAL_SCIENCES = "generalSciences";
    public static final String EARTH_SCIENCE = "earthScience";
    public static final String BIOLOGY = "biology";
    public static final String PHYSICS = "physics";
    public static final String CHEMISTRY = "chemistry";
    public static final String ASTRONOMY = "astronomy";
    public static final String MATH = "math";
    public static final String OTHER_SUBJECT = "otherSubject";
    public static final String OTHER_SUBJECT_INPUT = "otherSubjectInput";

    public static final String ELEMENTARY = "elementary";
    public static final String MIDDLE = "middle";
    public static final String HIGH = "high";
    public static final String UNIVERSITY = "university";
    public static final String GRADE_K = "gradeK";
    public static final String GRADE_1 = "grade1";
    public static final String GRADE_2 = "grade2";
    public static final String GRADE_3 = "grade3";
    public static final String GRADE_4 = "grade4";
    public static final String GRADE_5 = "grade5";
    public static final String GRADE_6 = "grade6";
    public static final String GRADE_7 = "grade7";
    public static final String GRADE_8 = "grade8";
    public static final String GRADE_9 = "grade9";
    public static final String GRADE_10 = "grade10";
    public static final String GRADE_11 = "grade11";
    public static final String GRADE_12 = "grade12";
    public static final String YEAR_1 = "year1";
    public static final String YEAR_2 = "year2plus";
    public static final String GRADUATE = "graduate";
    public static final String ADULT_ED = "adultEducation";
    public static final String OTHER_GRADE = "otherGrade";
    public static final String OTHER_GRADE_INPUT = "otherGradeInput";

    public RegisterPanel( String id, PageContext context, String destination ) {
        this( id, context, destination, false );
    }

    public RegisterPanel( String id, PageContext context, String destination, boolean updateProfile ) {
        super( id, context );
        this.context = context;

        this.destination = destination;
        this.updateProfile = updateProfile;

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

            if ( updateProfile ) {
                PhetUser user = PhetSession.get().getUser();
                String[] name = user.getName().split( " " );
                if ( name.length == 1 ) {
                    properties.add( FIRST_NAME, name[0] );
                }
                if ( name.length == 2 ) {
                    properties.add( FIRST_NAME, name[0] );
                    properties.add( LAST_NAME, name[1] );
                }
                properties.add( ORGANIZATION, user.getOrganization() );
                properties.add( EMAIL, user.getEmail() );
                properties.add( RECEIVE_EMAIL, String.valueOf( user.isReceiveEmail() ) );

                properties.add( TEACHER, String.valueOf( user.isTeacher() ) );
                properties.add( STUDENT, String.valueOf( user.isStudent() ) );
                properties.add( RESEARCHER, String.valueOf( user.isResearcher() ) );
                properties.add( TRANSLATOR, String.valueOf( user.isTranslator() ) );
                properties.add( TEACHER_EDUCATOR, String.valueOf( user.isTeacherEducator() ) );
                properties.add( OTHER_ROLE, String.valueOf( user.isOtherRole() ) );
                properties.add( OTHER_ROLE_INPUT, user.getOtherRoleText() );

                properties.add( GENERAL_SCIENCES, String.valueOf( user.isGeneralScience() ) );
                properties.add( EARTH_SCIENCE, String.valueOf( user.isEarthScience() ) );
                properties.add( BIOLOGY, String.valueOf( user.isBiology() ) );
                properties.add( PHYSICS, String.valueOf( user.isPhysics() ) );
                properties.add( CHEMISTRY, String.valueOf( user.isChemistry() ) );
                properties.add( ASTRONOMY, String.valueOf( user.isAstronomy() ) );
                properties.add( MATH, String.valueOf( user.isMath() ) );
                properties.add( OTHER_SUBJECT, String.valueOf( user.isOtherSubject() ) );
                properties.add( OTHER_SUBJECT_INPUT, user.getOtherSubjectText() );

                properties.add( ELEMENTARY, String.valueOf( user.isGeneralScience() ) );
                properties.add( GRADE_1, String.valueOf( user.isEarthScience() ) );
                properties.add( GRADE_2, String.valueOf( user.isEarthScience() ) );
                properties.add( GRADE_3, String.valueOf( user.isEarthScience() ) );
                properties.add( GRADE_4, String.valueOf( user.isEarthScience() ) );
                properties.add( GRADE_5, String.valueOf( user.isEarthScience() ) );
                properties.add( GRADE_6, String.valueOf( user.isEarthScience() ) );
                properties.add( GRADE_7, String.valueOf( user.isEarthScience() ) );
                properties.add( GRADE_8, String.valueOf( user.isEarthScience() ) );
                properties.add( GRADE_9, String.valueOf( user.isEarthScience() ) );
                properties.add( GRADE_10, String.valueOf( user.isEarthScience() ) );
                properties.add( GRADE_11, String.valueOf( user.isEarthScience() ) );
                properties.add( GRADE_12, String.valueOf( user.isEarthScience() ) );
                properties.add( OTHER_SUBJECT, String.valueOf( user.isOtherSubject() ) );
                properties.add( OTHER_SUBJECT_INPUT, user.getOtherSubjectText() );
            }

            add( firstName = new StringTextField( "firstName", new PropertyModel( properties, FIRST_NAME ) ) );
            add( lastName = new StringTextField( "lastName", new PropertyModel( properties, LAST_NAME ) ) );
            add( organization = new StringTextField( "organization", new PropertyModel( properties, ORGANIZATION ) ) );
            add( username = new StringTextField( "username", new PropertyModel( properties, EMAIL ) ) );
            add( receiveEmail = new CheckBox( "receiveEmail", new PropertyModel<Boolean>( properties, RECEIVE_EMAIL ) ) );
            add( password = new StringPasswordTextField( "password", new PropertyModel( properties, PASSWORD ) ) );
            add( passwordCopy = new StringPasswordTextField( "passwordCopy", new PropertyModel( properties, PASSWORD_COPY ) ) );

            firstName.add( new ErrorAppender() );
            lastName.add( new ErrorAppender() );
            organization.add( new ErrorAppender() );
            username.add( new ErrorAppender() );
            receiveEmail.add( new ErrorAppender() );
            password.add( new ErrorAppender() );
            passwordCopy.add( new ErrorAppender() );

            add( countryStatePanel = new CountryStateDropdownPanel( "countryState", context ) );
            countryStatePanel.getCountryDropdown().add( new ErrorAppender() );
            countryStatePanel.getStateDropdown().add( new ErrorAppender() );
            countryStatePanel.getCityTextField().add( new ErrorAppender() );

            // add role checkboxes
            WebMarkupContainer roleContainer;
            add( roleContainer = new WebMarkupContainer( "roleContainer" ) );
            roleContainer.add( roleErrorAppender = new ErrorAppender( false ) );
            roleContainer.add( teacherCheckbox = new CheckBox( "teacher", new PropertyModel<Boolean>( properties, TEACHER ) ) );
            roleContainer.add( studentCheckbox = new CheckBox( "student", new PropertyModel<Boolean>( properties, STUDENT ) ) );
            roleContainer.add( researcherCheckbox = new CheckBox( "researcher", new PropertyModel<Boolean>( properties, RESEARCHER ) ) );
            roleContainer.add( translatorCheckbox = new CheckBox( "translator", new PropertyModel<Boolean>( properties, TRANSLATOR ) ) );
            roleContainer.add( educatorCheckbox = new CheckBox( "educator", new PropertyModel<Boolean>( properties, TEACHER_EDUCATOR ) ) );
            roleContainer.add( otherRoleCheckbox = new CheckBox( "otherRole", new PropertyModel<Boolean>( properties, OTHER_ROLE ) ) );
            roleContainer.add( otherRole = new StringTextField( "otherRoleInput", new PropertyModel( properties, OTHER_ROLE_INPUT ) ) );
            otherRole.add ( new ErrorAppender() );

            // add subject checkboxes
            WebMarkupContainer subjectContainer;
            add( subjectContainer = new WebMarkupContainer( "subjectContainer" ) );
            subjectContainer.add( subjectErrorAppender = new ErrorAppender( false ) );
            subjectContainer.add( generalSciencesCheckbox = new CheckBox( "generalSciences", new PropertyModel<Boolean>( properties, GENERAL_SCIENCES ) ) );
            subjectContainer.add( earthScienceCheckbox = new CheckBox( "earthScience", new PropertyModel<Boolean>( properties, EARTH_SCIENCE ) ) );
            subjectContainer.add( biologyCheckbox = new CheckBox( "biology", new PropertyModel<Boolean>( properties, BIOLOGY ) ) );
            subjectContainer.add( physicsCheckbox = new CheckBox( "physics", new PropertyModel<Boolean>( properties, PHYSICS ) ) );
            subjectContainer.add( chemistryCheckbox = new CheckBox( "chemistry", new PropertyModel<Boolean>( properties, CHEMISTRY ) ) );
            subjectContainer.add( astronomyCheckbox = new CheckBox( "astronomy", new PropertyModel<Boolean>( properties, ASTRONOMY ) ) );
            subjectContainer.add( mathCheckbox = new CheckBox( "math", new PropertyModel<Boolean>( properties, MATH ) ) );
            subjectContainer.add( otherSubjectCheckbox = new CheckBox( "otherSubject", new PropertyModel<Boolean>( properties, OTHER_SUBJECT ) ) );
            subjectContainer.add( otherSubject = new StringTextField( "otherSubjectInput", new PropertyModel( properties, OTHER_SUBJECT_INPUT ) ) );
            otherSubject.add ( new ErrorAppender() );

            // add grade checkboxes
            WebMarkupContainer gradeContainer;
            add( gradeContainer = new WebMarkupContainer( "gradeContainer" ) );
            gradeContainer.add( gradeErrorAppender = new ErrorAppender( false ) );
            gradeContainer.add( elementaryCheckbox = new CheckBox( "elementary", new PropertyModel<Boolean>( properties, ELEMENTARY ) ) );
            gradeContainer.add( gradeKCheckbox = new CheckBox( "gradeK", new PropertyModel<Boolean>( properties, GRADE_K ) ) );
            gradeContainer.add( grade1Checkbox = new CheckBox( "grade1", new PropertyModel<Boolean>( properties, GRADE_1 ) ) );
            gradeContainer.add( grade2Checkbox = new CheckBox( "grade2", new PropertyModel<Boolean>( properties, GRADE_2 ) ) );
            gradeContainer.add( grade3Checkbox = new CheckBox( "grade3", new PropertyModel<Boolean>( properties, GRADE_3 ) ) );
            gradeContainer.add( grade4Checkbox = new CheckBox( "grade4", new PropertyModel<Boolean>( properties, GRADE_4 ) ) );
            gradeContainer.add( grade5Checkbox = new CheckBox( "grade5", new PropertyModel<Boolean>( properties, GRADE_5 ) ) );
            gradeContainer.add( middleCheckbox = new CheckBox( "middle", new PropertyModel<Boolean>( properties, MIDDLE ) ) );
            gradeContainer.add( grade6Checkbox = new CheckBox( "grade6", new PropertyModel<Boolean>( properties, GRADE_6 ) ) );
            gradeContainer.add( grade7Checkbox = new CheckBox( "grade7", new PropertyModel<Boolean>( properties, GRADE_7 ) ) );
            gradeContainer.add( grade8Checkbox = new CheckBox( "grade8", new PropertyModel<Boolean>( properties, GRADE_8 ) ) );
            gradeContainer.add( highCheckbox = new CheckBox( "high", new PropertyModel<Boolean>( properties, HIGH ) ) );
            gradeContainer.add( grade9Checkbox = new CheckBox( "grade9", new PropertyModel<Boolean>( properties, GRADE_9 ) ) );
            gradeContainer.add( grade10Checkbox = new CheckBox( "grade10", new PropertyModel<Boolean>( properties, GRADE_10 ) ) );
            gradeContainer.add( grade11Checkbox = new CheckBox( "grade11", new PropertyModel<Boolean>( properties, GRADE_11 ) ) );
            gradeContainer.add( grade12Checkbox = new CheckBox( "grade12", new PropertyModel<Boolean>( properties, GRADE_12 ) ) );
            gradeContainer.add( universityCheckbox = new CheckBox( "university", new PropertyModel<Boolean>( properties, UNIVERSITY ) ) );
            gradeContainer.add( year1Checkbox = new CheckBox( "year1", new PropertyModel<Boolean>( properties, YEAR_1 ) ) );
            gradeContainer.add( year2plusCheckbox = new CheckBox( "year2plus", new PropertyModel<Boolean>( properties, YEAR_2) ) );
            gradeContainer.add( graduateCheckbox = new CheckBox( "graduate", new PropertyModel<Boolean>( properties, GRADUATE ) ) );
            gradeContainer.add( adultEducationCheckbox = new CheckBox( "adultEducation", new PropertyModel<Boolean>( properties, ADULT_ED ) ) );
            gradeContainer.add( otherGradeCheckbox = new CheckBox( "otherGrade", new PropertyModel<Boolean>( properties, OTHER_GRADE ) ) );
            gradeContainer.add( otherGrade = new StringTextField( "otherGradeInput", new PropertyModel( properties, OTHER_GRADE_INPUT ) ) );
            otherGrade.add ( new ErrorAppender() );

            // teaching experience
            WebMarkupContainer teachingExperienceRadioGroupContainer;
            add( teachingExperienceRadioGroupContainer = new WebMarkupContainer( "teachingExperienceContainer" ) );
            teachingExperienceRadioGroupContainer.add( teachingExperienceErrorAppender = new ErrorAppender( false ) );

            teachingExperienceRadioGroup = new RadioGroup( "teachingExperienceRadios", new PropertyModel( properties, "teachingExperience" ) );

            Radio noneRadio = new Radio( "noneRadio", new Model<String>( "NONE" ) );
            teachingExperienceRadioGroup.add( noneRadio );
            teachingExperienceRadioGroup.add( new FormComponentLabel( "noneLabel", noneRadio ) );

            Radio oneToTwoRadio = new Radio( "oneToTwoRadio", new Model<String>( "ONE_TO_TWO" ) );
            teachingExperienceRadioGroup.add( oneToTwoRadio );
            teachingExperienceRadioGroup.add( new FormComponentLabel( "oneToTwoLabel", oneToTwoRadio ) );

            Radio threeToFiveRadio = new Radio( "threeToFiveRadio", new Model<String>( "THREE_TO_FIVE" ) );
            teachingExperienceRadioGroup.add( threeToFiveRadio );
            teachingExperienceRadioGroup.add( new FormComponentLabel( "threeToFiveLabel", threeToFiveRadio ) );

            Radio sixToTenRadio = new Radio( "sixToTenRadio", new Model<String>( "SIX_TO_TEN" ) );
            teachingExperienceRadioGroup.add( sixToTenRadio );
            teachingExperienceRadioGroup.add( new FormComponentLabel( "sixToTenLabel", sixToTenRadio ) );

            Radio elevenToTwentyRadio = new Radio( "elevenToTwentyRadio", new Model<String>( "ELEVEN_TO_TWENTY" ) );
            teachingExperienceRadioGroup.add( elevenToTwentyRadio );
            teachingExperienceRadioGroup.add( new FormComponentLabel( "elevenToTwentyLabel", elevenToTwentyRadio ) );

            Radio twentyOnePlusRadio = new Radio( "twentyOnePlusRadio", new Model<String>( "TWENTY_ONE_PLUS" ) );
            teachingExperienceRadioGroup.add( twentyOnePlusRadio );
            teachingExperienceRadioGroup.add( new FormComponentLabel( "twentyOnePlusLabel", twentyOnePlusRadio ) );

            teachingExperienceRadioGroupContainer.add( teachingExperienceRadioGroup );

            // phet experience
            WebMarkupContainer phetExperienceRadioGroupContainer;
            add( phetExperienceRadioGroupContainer = new WebMarkupContainer( "phetExperienceContainer" ) );
            phetExperienceRadioGroupContainer.add( phetExperienceErrorAppender = new ErrorAppender( false ) );

            phetExperienceRadioGroup = new RadioGroup( "phetExperienceRadios", new PropertyModel( properties, "phetExperience" ) );

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

            if ( updateProfile ) {
                password.setVisible( false );
                passwordCopy.setVisible( false );
            }

            add( new AbstractFormValidator() {
                public FormComponent[] getDependentFormComponents() {
                    return new FormComponent[]{firstName, lastName, password, passwordCopy, username, phetExperienceRadioGroup,
                            countryStatePanel.getCountryDropdown(), countryStatePanel.getStateDropdown(), otherRole, otherSubject,

                            // role checkboxes
                            teacherCheckbox, studentCheckbox, researcherCheckbox, translatorCheckbox, educatorCheckbox, otherRoleCheckbox,

                            // subject checkboxes
                            generalSciencesCheckbox, earthScienceCheckbox, biologyCheckbox, physicsCheckbox, chemistryCheckbox,
                            astronomyCheckbox, mathCheckbox, otherSubjectCheckbox,

                            // grade checkboxes
                            elementaryCheckbox, gradeKCheckbox, grade1Checkbox, grade2Checkbox, grade3Checkbox, grade4Checkbox,
                            grade5Checkbox, middleCheckbox, grade6Checkbox, grade7Checkbox, grade8Checkbox, highCheckbox, grade9Checkbox, grade10Checkbox,
                            grade11Checkbox, grade12Checkbox, universityCheckbox, year1Checkbox, year2plusCheckbox, graduateCheckbox, adultEducationCheckbox,
                            otherGradeCheckbox
                    };
                }

                public void validate( Form form ) {
                    phetExperienceRadioGroup.validate();
                    phetExperienceRadioGroup.updateModel();
                    teachingExperienceRadioGroup.validate();
                    teachingExperienceRadioGroup.updateModel();

                    if ( firstName.getInput() == null || firstName.getInput().length() == 0 ) {
                        error( firstName, "validation.user.firstName" );
                    }

                    if ( lastName.getInput() == null || lastName.getInput().length() == 0 ) {
                        error( lastName, "validation.user.lastName" );
                    }

                    if ( !updateProfile && !password.getInput().equals( passwordCopy.getInput() ) ) {
                        error( password, "validation.user.passwordMatch" );
                    }

                    if ( !updateProfile && password.getInput().length() == 0 ) {
                        error( password, "validation.user.password" );
                    }

                    String err = PhetUser.validateEmail( username.getInput() );
                    if ( err != null ) {
                        error( username, "validation.user.email" );
                    }

                    if ( organization.getInput() == null || organization.getInput().length() == 0 ) {
                        error( organization, "validation.user.organization" );
                    }

                    if ( phetExperienceRadioGroup.getModelObject() == null ) {
                        error( phetExperienceRadioGroup, "validation.user.phetExperience" );
                        phetExperienceErrorAppender.isValid = false;
                    }
                    else {
                        phetExperienceErrorAppender.isValid = true;
                    }

                    if ( teachingExperienceRadioGroup.getModelObject() == null ) {
                        error( teachingExperienceRadioGroup, "validation.user.teachingExperience" );
                        teachingExperienceErrorAppender.isValid = false;
                    }
                    else {
                        teachingExperienceErrorAppender.isValid = true;
                    }

                    if ( otherRoleCheckbox.getConvertedInput() && ( otherRole.getInput() == null || otherRole.getInput().length() == 0 ) ) {
                        error( otherRole, "validation.user.otherRole" );
                    }

                    if ( otherSubjectCheckbox.getConvertedInput() && ( otherSubject.getInput() == null || otherSubject.getInput().length() == 0 ) ) {
                        error( otherSubject, "validation.user.otherSubject" );
                    }

                    if ( otherGradeCheckbox.getConvertedInput() && ( otherGrade.getInput() == null || otherGrade.getInput().length() == 0 ) ) {
                        error( otherGrade, "validation.user.otherGrade" );
                    }

                    if ( countryStatePanel.getCountryDropdown().getInput().length() == 0 ) {
                        error( countryStatePanel.getCountryDropdown(), "validation.user.country" );
                    }

                    if ( countryStatePanel.getStateDropdown().getInput().length() == 0 ) {
                        error( countryStatePanel.getStateDropdown(), "validation.user.state" );
                    }

                    if ( countryStatePanel.getCityTextField().getInput() == null || countryStatePanel.getCityTextField().getInput().length() == 0 ) {
                        error( countryStatePanel.getCityTextField(), "validation.user.city" );
                    }

                    if ( !( teacherCheckbox.getConvertedInput() || studentCheckbox.getConvertedInput() || researcherCheckbox.getConvertedInput() ||
                            translatorCheckbox.getConvertedInput() || educatorCheckbox.getConvertedInput() || otherRoleCheckbox.getConvertedInput() ) ) {
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
                            year2plusCheckbox.getConvertedInput() || graduateCheckbox.getConvertedInput() || otherGradeCheckbox.getConvertedInput() ||
                            adultEducationCheckbox.getConvertedInput() ) ) {
                        error( generalSciencesCheckbox, "validation.user.grade" );
                        gradeErrorAppender.isValid = false;
                    }
                    else {
                        gradeErrorAppender.isValid = true;
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
                        if ( updateProfile ) {
                            update = true;
                        }
                        else if ( !user.isNewsletterOnlyAccount() && !user.isConfirmed() ) {
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
                    user.setReceiveEmail( receiveNewsletters );
                    if ( !updateProfile ) {
                        user.setPassword( password.getInput(), email );
                    }

                    user.setTeacher( teacherCheckbox.getModelObject() );
                    user.setStudent( studentCheckbox.getModelObject() );
                    user.setResearcher( researcherCheckbox.getModelObject() );
                    user.setTranslator( translatorCheckbox.getModelObject() );
                    user.setTeacherEducator( educatorCheckbox.getModelObject() );
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
                    user.setAdultEducation( adultEducationCheckbox.getModelObject() );
                    user.setOtherGrade( otherGradeCheckbox.getModelObject() );

                    user.setYearsTeaching( teachingExperienceRadioGroup.getModelObject().toString() );

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

            if ( !error && !updateProfile ) {
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