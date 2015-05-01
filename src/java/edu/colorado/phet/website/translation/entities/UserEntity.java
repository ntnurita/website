/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation.entities;

import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.authentication.panels.ChangePasswordPanel;
import edu.colorado.phet.website.authentication.panels.EditProfilePanel;
import edu.colorado.phet.website.authentication.panels.RegisterPanel;
import edu.colorado.phet.website.authentication.panels.ResetPasswordRequestPanel;
import edu.colorado.phet.website.authentication.panels.ResetPasswordRequestSuccessPanel;
import edu.colorado.phet.website.authentication.panels.SignInPanel;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.translation.PhetPanelFactory;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;

public class UserEntity extends TranslationEntity {
    public UserEntity() {

        addString( "session.loginRegister" );
        addString( "session.editProfile" );
        addString( "session.logout" );

        addString( "signIn.signIn" );
        addString( "signIn.email" );
        addString( "signIn.password" );
        addString( "signIn.rememberMe" );
        addString( "signIn.submit" );
        addString( "signIn.reset" );
        addString( "signIn.toRegister" );
        addString( "signIn.validation.failed" );
        addString( "signIn.resetYourPassword" );
        addString( "signIn.noAccount" );
        addString( "signIn.needAccount" );

        addString( "validation.user.email" );
        addString( "validation.user.user" );
        addString( "validation.user.passwordMatch" );
        addString( "validation.user.password" );
        addString( "validation.user.description" );
        addString( "validation.user.emailUsed" );
        addString( "validation.user.problems" );

        addString( "validation.user.role" );
        addString( "validation.user.subject" );
        addString( "validation.user.grade" );
        addString( "validation.user.otherRole" );
        addString( "validation.user.otherSubject" );
        addString( "validation.user.country" );
        addString( "validation.user.firstName" );
        addString( "validation.user.lastName" );
        addString( "validation.user.phetExperience" );
        addString( "validation.user.teachingExperience" );
        addString( "validation.user.organization" );
        addString( "validation.user.state" );
        addString( "validation.user.city" );

        addString( "register.teacher" );
        addString( "register.student" );
        addString( "register.researcher" );
        addString( "register.translator" );
        addString( "register.educator" );
        addString( "register.other" );
        addString( "register.generalSciences" );
        addString( "register.earthScience" );
        addString( "register.biology" );
        addString( "register.physics" );
        addString( "register.chemistry" );
        addString( "register.astronomy" );
        addString( "register.math" );
        addString( "register.years" );
        addString( "register.newUser" );
        addString( "register.occasionalUser" );
        addString( "register.experiencedUser" );
        addString( "register.powerUser" );

        addString( "register.elementary" );
        addString( "register.gradeK" );
        addString( "register.grade1" );
        addString( "register.grade2" );
        addString( "register.grade3" );
        addString( "register.grade4" );
        addString( "register.grade5" );
        addString( "register.middle" );
        addString( "register.grade6" );
        addString( "register.grade7" );
        addString( "register.grade8" );
        addString( "register.high" );
        addString( "register.grade9" );
        addString( "register.grade10" );
        addString( "register.grade11" );
        addString( "register.grade12" );
        addString( "register.university" );
        addString( "register.year1" );
        addString( "register.year2plus" );
        addString( "register.graduate" );
        addString( "register.adultEducation" );

        addString( "register.none" );
        addString( "register.oneToTwo" );
        addString( "register.threeToFive" );
        addString( "register.sixToTen" );
        addString( "register.elevenToTwenty" );
        addString( "register.twentyOnePlus" );

        addString( "register.checkAll" );

        addString( "profile.register" );
        addString( "profile.update" );
        addString( "profile.name" );
        addString( "profile.firstName" );
        addString( "profile.lastName" );
        addString( "profile.organization" );
        addString( "profile.organizationSchool" );
        addString( "profile.description" );
        addString( "profile.email" );
        addString( "profile.password" );
        addString( "profile.passwordCopy" );
        addString( "profile.register.info" );
        addString( "profile.register.submit" );
        addString( "profile.register.reset" );
        addString( "profile.jobTitle" );
        addString( "profile.address1" );
        addString( "profile.address2" );
        addString( "profile.city" );
        addString( "profile.state" );
        addString( "profile.country" );
        addString( "profile.zip" );
        addString( "profile.phone1" );
        addString( "profile.phone2" );
        addString( "profile.fax" );
        addString( "profile.receiveEmail" );
        addString( "profile.receiveSimulationNotifications", "PLEASE NOTE: Translated versions should commet that these emails will only be sent out in English!" );

        addString( "profile.edit" );
        addString( "profile.edit.submit" );
        addString( "profile.edit.reset" );
        addString( "profile.edit.changePassword" );

        addString( "signOut.beingSignedOut" );

        addString( "nav.changePasswordSuccess" );
        addString( "changePasswordSuccess.title" );
        addString( "changePasswordSuccess.message" );
        addString( "changePassword.validation.newPasswordBlank" );
        addString( "changePassword.submit" );
        addString( "changePassword.currentPassword" );
        addString( "changePassword.newPassword" );
        addString( "changePassword.confirmNewPassword" );
        addString( "changePassword.validation.mismatch" );
        addString( "changePassword.validation.incorrectPassword" );
        addString( "changePassword.title" );
        addString( "changePassword.header" );

        addString( "resetPasswordRequest.title" );
        addString( "resetPasswordRequest.submit" );
        addString( "resetPasswordRequest.emailAddress" );
        addString( "resetPasswordRequest.description" );
        addString( "resetPasswordRequest.header" );
        addString( "resetPasswordRequest.validation.noAccountFound" );
        addString( "resetPasswordRequest.emailBody" );
        addString( "resetPasswordRequest.emailSubject" );
        addString( "resetPasswordRequestSuccess.message" );

        addString( "resetPasswordRequestSuccess.title" );
        addString( "resetPasswordCallback.title" );

        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                            return new SignInPanel( id, context, "/" );
                        }
                    }, "Sign In Page" );

        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                            return new RegisterPanel( id, context, "/" );
                        }
                    }, "Register Page" );

        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                            return new EditProfilePanel( id, context, null );
                        }
                    }, "Edit Profile Page" );

        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                            return new ChangePasswordPanel( id, context, PhetSession.get().getUser(), true );
                        }
                    }, "Change Password Page" );

        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                            return new ResetPasswordRequestPanel( id, context );
                        }
                    }, "Reset Password Page" );

        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                            return new ResetPasswordRequestSuccessPanel( id, context );
                        }
                    }, "Reset Password Success Page" );

    }

    public String getDisplayName() {
        return "Users";
    }
}