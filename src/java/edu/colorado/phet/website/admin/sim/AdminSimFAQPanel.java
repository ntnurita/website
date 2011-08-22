// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.admin.sim;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import edu.colorado.phet.website.admin.faq.AdminEditFAQPage;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.data.faq.FAQList;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * FAQ section of the simulation-edit page
 */
public class AdminSimFAQPanel extends PhetPanel {

    private static final Logger logger = Logger.getLogger( AdminSimFAQPanel.class.getName() );

    public AdminSimFAQPanel( String id, PageContext context, final Simulation simulation ) {
        super( id, context );

        // checkbox form to update simulation FAQ visibility
        add( new Form( "visible-form" ) {{
            final Model<Boolean> model = new Model<Boolean>( simulation.isFaqVisible() );
            add( new AjaxCheckBox( "visible-box", model ) {
                @Override protected void onUpdate( AjaxRequestTarget target ) {
                    HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                        public boolean run( Session session ) {
                            Simulation sim = (Simulation) session.load( Simulation.class, simulation.getId() );
                            sim.setFaqVisible( model.getObject() );
                            session.update( sim );
                            return true;
                        }
                    } );
                }
            } );
            setVisible( simulation.getFaqList() != null );
        }} );

        add( new Link( "edit-faq" ) {
            @Override public void onClick() {
                boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                    public boolean run( Session session ) {
                        Simulation sim = (Simulation) session.load( Simulation.class, simulation.getId() );

                        // we need to check to see if we are not hooked to a FAQ. If not, handle it and hook one up
                        if ( sim.getFaqList() == null ) {
                            // see if there are any squatting on our desired name
                            List options = session.createQuery( "select f from FAQList as f where f.name = :name" ).setString( "name", sim.getName() ).list();
                            if ( options.size() > 1 ) {
                                throw new RuntimeException( "duplicate FAQs with the name " + sim.getName() );
                            }

                            if ( options.size() == 1 ) {
                                // there is an existing FAQ with our desired name. hook it up, since it is probably someone mucking around
                                sim.setFaqList( (FAQList) options.get( 0 ) );
                            }
                            else {
                                // no existing faq. create it
                                FAQList list = new FAQList();
                                list.setName( sim.getName() );
                                sim.setFaqList( list );
                                session.save( list );
                            }
                            session.update( sim );
                        }
                        return true;
                    }
                } );
                if ( success ) {
                    goToEditPage( simulation.getName() );
                }
            }
        } );
    }

    private void goToEditPage( String faqName ) {
        PageParameters params = new PageParameters();
        params.put( AdminEditFAQPage.ADMIN_EDIT_FAQ_PAGE_NAME, faqName );
        setResponsePage( AdminEditFAQPage.class, params );
    }
}