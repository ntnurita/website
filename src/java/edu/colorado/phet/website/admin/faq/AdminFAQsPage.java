// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.admin.faq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import edu.colorado.phet.website.admin.AdminPage;
import edu.colorado.phet.website.data.faq.FAQList;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;

/**
 * Administration page for FAQs that shows a list of FAQs that can be viewed and edited. Also includes
 * the ability to create FAQs.
 */
public class AdminFAQsPage extends AdminPage {

    private static final Logger logger = Logger.getLogger( AdminFAQsPage.class.getName() );

    public AdminFAQsPage( PageParameters parameters ) {
        super( parameters );

        final List<String> faqNames = new ArrayList<String>();

        HibernateUtils.wrapTransaction( getHibernateSession(), new VoidTask() {
            public Void run( Session session ) {
                List faqs = session.createQuery( "select f from FAQList as f" ).list();
                for ( Object faq : faqs ) {
                    faqNames.add( ( (FAQList) faq ).getName() );
                }
                return null;
            }
        } );

        Collections.sort( faqNames );

        add( new ListView<String>( "faq-list", faqNames ) {
            @Override protected void populateItem( ListItem<String> item ) {
                final String name = item.getModelObject();
                item.add( new Label( "name", name ) );
                item.add( new Link( "edit-link" ) {
                    @Override public void onClick() {
                        goToEditPage( name );
                    }
                } );
                item.add( new Link( "preview-link" ) {
                    @Override public void onClick() {
                        goToPreviewPage( name );
                    }
                } );
            }
        } );

        add( new Form( "create-faq-form" ) {
            private TextField<String> nameText;

            {
                add( nameText = new TextField<String>( "name", new Model<String>( "" ) ) );
            }

            @Override protected void onSubmit() {
                final String name = nameText.getModelObject();
                boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                    public boolean run( Session session ) {
                        boolean hasFaqWithSameName = !session.createQuery( "select f from FAQList as f where f.name = :name" ).setString( "name", name ).list().isEmpty();

                        if ( !hasFaqWithSameName ) {
                            // create the FAQ
                            FAQList faq = new FAQList();
                            faq.setName( name );

                            // save it and attempt to return success.
                            session.save( faq );
                            return true;
                        }

                        // failure
                        return false;
                    }
                } );

                // ensure success of both the FAQ creation and hibernate commit before going to the edit page
                if ( success ) {
                    // update the faq names list in case the user goes back to this page.
                    faqNames.add( name );
                    Collections.sort( faqNames );

                    // go to the editing page
                    goToEditPage( name );
                }

                // TODO: add feedback area to note failure?
            }
        } );
    }

    private void goToEditPage( String faqName ) {
        PageParameters params = new PageParameters();
        params.put( AdminEditFAQPage.ADMIN_EDIT_FAQ_PAGE_NAME, faqName );
        setResponsePage( AdminEditFAQPage.class, params );
    }

    private void goToPreviewPage( String faqName ) {
        PageParameters params = new PageParameters();
        params.put( AdminPreviewFAQPage.ADMIN_PREVIEW_FAQ_PAGE_NAME, faqName );
        setResponsePage( AdminPreviewFAQPage.class, params );
    }
}
