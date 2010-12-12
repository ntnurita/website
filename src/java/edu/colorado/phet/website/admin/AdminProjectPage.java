package edu.colorado.phet.website.admin;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import edu.colorado.phet.buildtools.util.ProjectPropertiesFile;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.components.StringTextField;
import edu.colorado.phet.website.data.Project;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

public class AdminProjectPage extends AdminPage {

    private Project project;
    private int projectId;
    private Label title;

    private String statusString = null;

    private List<Simulation> simulations = new LinkedList<Simulation>();

    private AdminProjectPage.ProjectForm projectForm;

    public static final String PROJECT_ID = "projectId";
    private static final Logger logger = Logger.getLogger( AdminProjectPage.class.getName() );

    public AdminProjectPage( PageParameters parameters ) {
        super( parameters );

        projectId = parameters.getInt( PROJECT_ID );

        // fill in project, simulation list and status string
        HibernateUtils.wrapTransaction( getHibernateSession(), new StartTask() );

        title = new Label( "project-name", getTitleString() );
        add( title );

        projectForm = new ProjectForm( "edit-form" );
        add( projectForm );

        ProjectPropertiesFile projectPropertiesFile = project.getProjectPropertiesFile( ( (PhetWicketApplication) getApplication() ).getWebsiteProperties().getPhetDocumentRoot() );

        RawLabel projectChecks;
        if ( projectPropertiesFile.exists() ) {
            String str = "Detected project properties: " + projectPropertiesFile.getFullVersionString();
            if ( statusString != null ) {
                str += "<br/>" + statusString;
            }
            projectChecks = new RawLabel( "project-properties", str );
        }
        else {
            projectChecks = new RawLabel( "project-properties", "No project properties detected" );
        }
        add( projectChecks );

        add( new ListView<Simulation>( "simulation", simulations ) {
            protected void populateItem( ListItem<Simulation> item ) {
                Simulation sim = item.getModelObject();
                item.add( new Label( "simulation-name", sim.getName() ) );
            }
        } );

        add( new Link( "synchronize-link" ) {
            public void onClick() {
                Project.synchronizeProject( ( (PhetWicketApplication) getApplication() ).getWebsiteProperties().getPhetDocumentRoot(), getHibernateSession(), project.getName() );
                // TODO: get rid of this ugly way of updating everything on the page
                simulations.clear();
                HibernateUtils.wrapTransaction( getHibernateSession(), new StartTask() );
                projectForm.update( project );
                title.setDefaultModel( new Model<String>( getTitleString() ) );

                PageParameters params = new PageParameters();
                params.put( PROJECT_ID, projectId );
                setResponsePage( AdminProjectPage.class, params );
            }
        } );

    }

    private String getTitleString() {
        return project.getName() + " " + project.getVersionString() + " (" + project.getVersionRevision() + ")";
    }

    private class ProjectForm extends Form {
        private TextField<String> major;
        private TextField<String> minor;
        private TextField<String> dev;
        private TextField<String> revision;
        private TextField<String> timestamp;
        private DropDownChoice visible;

        public void setMajor( int major ) {
            this.major.setModel( new Model<String>( String.valueOf( major ) ) );
        }

        public void setMinor( int minor ) {
            this.minor.setModel( new Model<String>( String.valueOf( minor ) ) );
        }

        public void setDev( int dev ) {
            this.dev.setModel( new Model<String>( String.valueOf( dev ) ) );
        }

        public void setRevision( int revision ) {
            this.revision.setModel( new Model<String>( String.valueOf( revision ) ) );
        }

        public void setTimestamp( long timestamp ) {
            this.timestamp.setModel( new Model<String>( String.valueOf( timestamp ) ) );
        }

        public void update( Project project ) {
            setMajor( project.getVersionMajor() );
            setMinor( project.getVersionMinor() );
            setDev( project.getVersionDev() );
            setRevision( project.getVersionRevision() );
            setTimestamp( project.getVersionTimestamp() );
        }

        public ProjectForm( String id ) {
            super( id );

            visible = new DropDownChoice<String>( "visible", new Model<String>( project.isVisible() ? "True" : "False" ), Arrays.asList( "True", "False" ), new IChoiceRenderer<String>() {
                public Object getDisplayValue( String str ) {
                    return str;
                }

                public String getIdValue( String str, int index ) {
                    return String.valueOf( str );
                }
            } );
            add( visible );

            major = new StringTextField( "major", new Model<String>( String.valueOf( project.getVersionMajor() ) ) );
            add( major );
            minor = new StringTextField( "minor", new Model<String>( String.valueOf( project.getVersionMinor() ) ) );
            add( minor );
            dev = new StringTextField( "dev", new Model<String>( String.valueOf( project.getVersionDev() ) ) );
            add( dev );
            revision = new StringTextField( "revision", new Model<String>( String.valueOf( project.getVersionRevision() ) ) );
            add( revision );
            timestamp = new StringTextField( "timestamp", new Model<String>( String.valueOf( project.getVersionTimestamp() ) ) );
            add( timestamp );
        }

        @Override
        protected void onSubmit() {
            super.onSubmit();

            logger.info( "setting project values for " + project.getName() );
            logger.info( "visible: " + visible.getModelValue() );
            logger.info( "major: " + major.getModelObject() );
            logger.info( "minor: " + minor.getModelObject() );
            logger.info( "dev: " + dev.getModelObject() );
            logger.info( "revision: " + revision.getModelObject() );
            logger.info( "timestamp: " + timestamp.getModelObject() );

            HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                public boolean run( Session session ) {

                    project = (Project) session.load( Project.class, project.getId() );

                    if ( visible.getModelValue().equals( "True" ) ) {
                        project.setVisible( true );
                    }
                    else if ( visible.getModelValue().equals( "False" ) ) {

                        project.setVisible( false );
                    }
                    else {
                        throw new RuntimeException( "True or False?" );
                    }

                    project.setVersionMajor( Integer.valueOf( major.getModelObject() ) );
                    project.setVersionMinor( Integer.valueOf( minor.getModelObject() ) );
                    project.setVersionDev( Integer.valueOf( dev.getModelObject() ) );
                    project.setVersionRevision( Integer.valueOf( revision.getModelObject() ) );
                    project.setVersionTimestamp( Long.valueOf( timestamp.getModelObject() ) );

                    session.update( project );

                    title.setDefaultModelObject( getTitleString() );

                    return true;
                }
            } );

        }
    }

    private class StartTask implements HibernateTask {
        public boolean run( Session session ) {
            project = (Project) session.load( Project.class, projectId );
            for ( Object o : project.getSimulations() ) {
                simulations.add( (Simulation) o );
            }
            statusString = project.consistencyCheck( ( (PhetWicketApplication) getApplication() ).getWebsiteProperties().getPhetDocumentRoot() );
            return true;
        }
    }
}
