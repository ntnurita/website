/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.admin.sim;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import edu.colorado.phet.website.components.RawLink;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.SimpleTask;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * Displays a list of NSDL Science Literacy Map keys that are used by a simulation, and allows keys to be added or removed
 * <p/>
 * TODO: validation of keys
 */
public class NSDLScienceLiteracyMapPanel extends PhetPanel {

    private static final Logger logger = Logger.getLogger( NSDLScienceLiteracyMapPanel.class.getName() );

    public NSDLScienceLiteracyMapPanel( String id, PageContext context, final Simulation simulation ) {
        super( id, context );

        final List<String> keys = new LinkedList<String>( simulation.getScienceLiteracyMapKeys() );

        add( new Form( "nsdl-form" ) {
            private TextField<String> keywordInput;

            {
                final Form formReference = this;

                // create input with empty non-referenced model. we will pull its value out in a different way
                add( keywordInput = new TextField<String>( "nsdl-key", new Model<String>( "" ) ) );

                /*---------------------------------------------------------------------------*
                * list of existing keys
                *----------------------------------------------------------------------------*/
                add( new ListView<String>( "keys", keys ) {
                    @Override protected void populateItem( ListItem<String> item ) {
                        final String key = item.getModelObject();
                        item.add( new RawLink( "key-link", "http://strandmaps.nsdl.org/?id=" + key ) {{
                            add( new Label( "key-name", key ) );
                        }} );

                        // "remove" clicked
                        item.add( new AjaxLink( "key-remove" ) {
                            @Override public void onClick( AjaxRequestTarget target ) {
                                final List<String> newKeys = new LinkedList<String>();

                                boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new SimpleTask() {
                                    public void run( Session session ) {
                                        // get a copy of the simulation that is hooked through Hibernate
                                        Simulation sim = (Simulation) session.load( Simulation.class, simulation.getId() );

                                        logger.info( "Removing NSDL map key " + key + " from sim " + sim.getName() );

                                        // remove the string from the keys
                                        sim.getScienceLiteracyMapKeys().remove( key );
                                        session.update( sim );

                                        // store the list of keys, so we can update the UI
                                        newKeys.addAll( sim.getScienceLiteracyMapKeys() );
                                    }
                                } );

                                if ( success ) {
                                    // update our list to show the user
                                    keys.clear();
                                    keys.addAll( newKeys );
                                }

                                // redraw everything on the form.
                                target.addComponent( formReference );
                            }
                        } );
                    }
                } );

                /*---------------------------------------------------------------------------*
                * form submission
                *----------------------------------------------------------------------------*/
                add( new AjaxButton( "ajax-button", this ) {
                    @Override protected void onSubmit( AjaxRequestTarget target, Form<?> form ) {
                        final String newKey = keywordInput.getValue();

                        // make sure our key starts with the required prefix. should filter out most bad data
                        if ( newKey.startsWith( "SMS-" ) ) {
                            final List<String> newKeys = new LinkedList<String>();

                            boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new SimpleTask() {
                                public void run( Session session ) {
                                    // get a copy of the simulation that is hooked through Hibernate
                                    Simulation sim = (Simulation) session.load( Simulation.class, simulation.getId() );

                                    logger.info( "Adding NSDL map key " + newKey + " to sim " + sim.getName() );

                                    // add the string to the keys
                                    sim.getScienceLiteracyMapKeys().add( newKey );
                                    session.update( sim );

                                    // store the list of keys, so we can update the UI
                                    newKeys.addAll( sim.getScienceLiteracyMapKeys() );
                                }
                            } );

                            if ( success ) {
                                // update our list to show the user
                                keys.clear();
                                keys.addAll( newKeys );
                            }

                            // redraw everything on the form.
                            target.addComponent( formReference );
                        }
                    }
                } );

                // output markup ID so that the Ajax behavior works correctly
                setOutputMarkupId( true );
            }
        } );
    }
}