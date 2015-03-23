/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.panels;

import org.apache.wicket.RedirectToUrlException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.admin.AdminMainPage;
import edu.colorado.phet.website.authentication.EditProfilePage;
import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.authentication.RegisterPage;
import edu.colorado.phet.website.authentication.ResetPasswordRequestPage;
import edu.colorado.phet.website.authentication.SignOutPage;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.StringPasswordTextField;
import edu.colorado.phet.website.components.StringTextField;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.StringUtils;

/**
 * Panel that shows either the "Login / Register" link if not signed in, or the "Edit Profile | Sign out" links if
 * signed in.
 * <p/>
 * Additionally, if the user is a team member, an additional "administration" link is shown that will go to the
 * administration interface
 */
public class LogInOutPanel extends PhetPanel {

    public static final String SIGN_IN_ID = "sign-in-link";
    public static final String SIGN_OUT_ID = "sign-out-link";

    private TextField username;
    private PasswordTextField password;

//    FeedbackPanel feedback;

    /**
     * Whether to remember the user or not.
     * NOTE: don't convert to local variable, PropertyModel uses reflection to change this value
     */
    private boolean remember = true;

    public LogInOutPanel( String id, final PageContext context ) {
        super( id, context );

        final PhetSession psession = PhetSession.get();

        String path = getFullPath( context );

        if ( psession != null && psession.isSignedIn() ) {
            // user is signed in
            addWithId( SignOutPage.getLinker().getLink( "sign-out", context, getPhetCycle() ), SIGN_OUT_ID );
            add( EditProfilePage.getLinker( path ).getLink( "edit-profile", context, getPhetCycle() ) );
            add( new InvisibleComponent( "sign-in" ) );
            add( new Label( "current-email", psession.getUser().getEmail() ) );
            if ( PhetSession.get().getUser().isTeamMember() ) {
                BookmarkablePageLink link = new BookmarkablePageLink<Void>( "admin-link", AdminMainPage.class );
                add( link );
            }
            else {
                add( new InvisibleComponent( "admin-link" ) );
            }
        }
        else {
            // user is not signed in
            add( new InvisibleComponent( "current-email" ) );
            add( new InvisibleComponent( "edit-profile" ) );
            add( new InvisibleComponent( "sign-out" ) );
            if ( DistributionHandler.displayLogin( getPhetCycle() ) ) {
                add( new WebMarkupContainer( "sign-in" ) );
            }
            else {
                add( new InvisibleComponent( "sign-in" ) );
            }
            add( new InvisibleComponent( "team-member" ) );
        }

        WebMarkupContainer formContainer = new WebMarkupContainer( "login-form-container" ) {{
//            feedback = new FeedbackPanel( "feedback" );
//            feedback.setVisible( false );
//            add( feedback );
            add( new SignInForm( "sign-in-form", context ) );
        }};
        add( formContainer );
    }


    public final class SignInForm extends Form {
        private static final long serialVersionUID = 1L;

        private final ValueMap properties = new ValueMap();

        private String path;

        public SignInForm( final String id, PageContext context ) {
            super( id );

            path = "/" + context.getLocale() + "/" + context.getPath();

            add( username = new StringTextField( "username", new PropertyModel( properties, "username" ) ) );

            password = new StringPasswordTextField( "password", new PropertyModel( properties, "password" ) );
            password.setRequired( false );
            add( password );

            final WebMarkupContainer rememberBox = new WebMarkupContainer( "remember" );
            add( rememberBox );

            rememberBox.add( new CheckBox( "remember-check", new PropertyModel( LogInOutPanel.this, "remember" ) ) );
            rememberBox.add( ResetPasswordRequestPage.getLinker().getLink( "password-link", context, getPhetCycle() ) );

            add( RegisterPage.getLinker( "/" ).getLink( "register-link", context, getPhetCycle() ) );

            username.setPersistent( remember );

            add( new AbstractFormValidator() {
                public FormComponent[] getDependentFormComponents() {
                    return new FormComponent[]{username, password};
                }

                public void validate( Form form ) {
                    System.out.println( "validate called" );
                    if ( !PhetSession.get().signIn( (PhetRequestCycle) getRequestCycle(), username.getInput(), password.getInput() ) ) {
                        String url = StringUtils.makeUrlAbsolute( path );
                        url += "?login-failed";
                        System.out.println( "validate error" );
                        throw new RedirectToUrlException( url );

//                        error( password, "signIn.validation.failed" );
                    }
                }
            } );
        }

        @Override
        protected void onValidate() {
            super.onValidate();
//            feedback.setVisible( feedback.anyMessage() );
        }

        public final void onSubmit() {
            String url = StringUtils.makeUrlHTTPS( path );
            throw new RedirectToUrlException( url );
        }
    }
}
