// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.admin.sim;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import edu.colorado.phet.common.phetcommon.util.function.VoidFunction1;
import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.content.simulations.SimulationPage;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.notification.NotificationHandler;
import edu.colorado.phet.website.util.StringUtils;

import static edu.colorado.phet.website.notification.NotificationHandler.getSimulationNotificationMessage;

/**
 * Simulation notification user interface for administrators. uses the HTML located with AdminSimPage.html
 */
public class SimulationNotificationForm extends Form {

    private TextField<String> subjectText;
    private TextArea<String> bodyText;
    private final Model<String> previewSubjectModel;
    private final Model<String> previewBodyModel;
    private final AjaxButton submitButton;
    private final Simulation simulation;

    public SimulationNotificationForm( String id, final Simulation simulation ) {
        super( id );
        this.simulation = simulation;

        final Form formReference = this;

        // we make this container (and all the editing tools visible) when the user hits the "compose" button
        final WebMarkupContainer container = new WebMarkupContainer( "notification-compose-table" );
        container.setOutputMarkupId( true );
        add( container );

        previewSubjectModel = new Model<String>( "" );
        previewBodyModel = new Model<String>( "" );

        container.add( subjectText = new TextField<String>( "subject", new Model<String>( "" ) ) );
        container.add( bodyText = new TextArea<String>( "body", new Model<String>( "" ) ) );
        final Label subjectPreview = new Label( "preview-subject", previewSubjectModel ) {{
            setOutputMarkupId( true );
        }};
        final MultiLineLabel bodyPreview = new MultiLineLabel( "preview-body", previewBodyModel ) {{
            setOutputMarkupId( true );
        }};
        container.add( subjectPreview );
        container.add( bodyPreview );
        submitButton = new AjaxButton( "submit", this ) {
            @Override protected void onSubmit( AjaxRequestTarget target, Form<?> form ) {
                target.addComponent( formReference );

                NotificationHandler.kickOffSimulationNotificationEmails( subjectText.getModelObject(),
                                                                         bodyText.getModelObject(),
                                                                         simulation );

                // hide the container afterwards
                container.setVisible( false );
                target.addComponent( container );
            }
        };
        container.add( submitButton );

        container.setVisible( false );


        AjaxButton composeButton = new AjaxButton( "compose-button", this ) {
            @Override protected void onSubmit( AjaxRequestTarget target, Form<?> form ) {
                target.addComponent( formReference );

                // hide the button
                setVisible( false );
                container.setVisible( true );
            }
        };
        add( composeButton );

        subjectText.add( new OnChangeAjaxBehavior() {
            @Override protected void onUpdate( AjaxRequestTarget target ) {
                target.addComponent( subjectPreview );
                target.addComponent( bodyPreview );
                updatePreview();
            }
        } );
        bodyText.add( new OnChangeAjaxBehavior() {
            @Override protected void onUpdate( AjaxRequestTarget target ) {
                target.addComponent( subjectPreview );
                target.addComponent( bodyPreview );
                updatePreview();
            }
        } );
    }

    private void updatePreview() {
        previewSubjectModel.setObject( subjectText.getModelObject() );
        previewBodyModel.setObject( getSimulationNotificationMessage( simulation.getEnglishSimulation().getTitle(),
                                                                      StringUtils.makeUrlAbsolute( SimulationPage.getLinker( simulation ).getDefaultRawUrl() ),
                                                                      bodyText.getModelObject(), PhetSession.get().getUser().getConfirmationKey() ) );
    }
}
