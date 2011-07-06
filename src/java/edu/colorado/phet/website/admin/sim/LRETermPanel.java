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
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.metadata.LRETerm;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * Displays a list of LRE-0001 (LRETerm) terms that are associated with the simulation, and allows adding and removal of the terms
 */
public class LRETermPanel extends PhetPanel {

    private static final Logger logger = Logger.getLogger( LRETermPanel.class.getName() );

    public LRETermPanel( String id, PageContext context, final Simulation simulation ) {
        super( id, context );

        final List<LRETerm> terms = new LinkedList<LRETerm>( simulation.getLRETerms() );

        add( new Form( "lre-form" ) {
            private TermDropDownChoice dropDownChoice;

            {
                final Form formReference = this;

                // create input with empty non-referenced model. we will pull its value out in a different way
                add( dropDownChoice = new TermDropDownChoice( "available-terms", LRETerm.getSortedTerms() ) );

                /*---------------------------------------------------------------------------*
                * list of existing keys
                *----------------------------------------------------------------------------*/
                add( new ListView<LRETerm>( "terms", terms ) {
                    @Override protected void populateItem( ListItem<LRETerm> item ) {
                        final LRETerm term = item.getModelObject();
                        item.add( new Label( "term", term.englishCaption ) );

                        // "remove" clicked
                        item.add( new AjaxLink( "remove" ) {
                            @Override public void onClick( AjaxRequestTarget target ) {
                                final List<LRETerm> newTerms = new LinkedList<LRETerm>();

                                boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new VoidTask() {
                                    public Void run( Session session ) {
                                        // get a copy of the simulation that is hooked through Hibernate
                                        Simulation sim = (Simulation) session.load( Simulation.class, simulation.getId() );

                                        logger.info( "Removing LRE term " + term.englishCaption + " from sim " + sim.getName() );

                                        // remove the string from the keys
                                        sim.removeLRETerm( term );
                                        session.update( sim );

                                        // store the list of keys, so we can update the UI
                                        newTerms.addAll( sim.getLRETerms() );
                                        return null;
                                    }
                                } );

                                if ( success ) {
                                    // update our list to show the user
                                    terms.clear();
                                    terms.addAll( newTerms );
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
                        final String newId = dropDownChoice.getModelValue();
                        final LRETerm newTerm = LRETerm.getTermFromId( newId );

                        // make sure our key starts with the required prefix. should filter out most bad data
                        if ( !terms.contains( newTerm ) ) {
                            final List<LRETerm> newTerms = new LinkedList<LRETerm>();

                            boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new VoidTask() {
                                public Void run( Session session ) {
                                    // get a copy of the simulation that is hooked through Hibernate
                                    Simulation sim = (Simulation) session.load( Simulation.class, simulation.getId() );

                                    logger.info( "Adding LRE term " + newTerm.englishCaption + " to sim " + sim.getName() );

                                    // add the string to the keys
                                    sim.addLRETerm( newTerm );
                                    session.update( sim );

                                    // store the list of keys, so we can update the UI
                                    newTerms.addAll( sim.getLRETerms() );
                                    return null;
                                }
                            } );

                            if ( success ) {
                                // update our list to show the user
                                terms.clear();
                                terms.addAll( newTerms );
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

    public class TermDropDownChoice extends DropDownChoice<LRETerm> {
        public TermDropDownChoice( String id, List<LRETerm> availableTerms ) {
            super( id, new Model<LRETerm>(), availableTerms, new IChoiceRenderer<LRETerm>() {
                public Object getDisplayValue( LRETerm term ) {
                    return term.englishCaption;
                }

                public String getIdValue( LRETerm term, int index ) {
                    return term.id;
                }
            } );
        }
    }
}