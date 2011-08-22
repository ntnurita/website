// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.admin.faq;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import edu.colorado.phet.common.phetcommon.util.function.VoidFunction1;
import edu.colorado.phet.website.admin.AdminPage;
import edu.colorado.phet.website.data.faq.FAQItem;
import edu.colorado.phet.website.data.faq.FAQList;
import edu.colorado.phet.website.panels.faq.FAQPanel;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.Result;
import edu.colorado.phet.website.util.hibernate.Task;

/**
 * Page for editing FAQs by administrators
 */
public class AdminEditFAQPage extends AdminPage {

    public static final String ADMIN_EDIT_FAQ_PAGE_NAME = "admin-faq-page-name";

    private static final Logger logger = Logger.getLogger( AdminEditFAQPage.class.getName() );

    private List<FAQItem> faqItems = new LinkedList<FAQItem>();
    private FAQList list;

    private final MarkupContainer previewContainer; // holds the preview, so we can have a consistent AJAX ID to update
    private FAQPanel preview; // current preview component

    private final MarkupContainer itemsContainer; // holds the list of items being edited. needed since we can't AJAX update a ListView itself, and updating the entire page is inefficient
    private String faqName;

    public AdminEditFAQPage( PageParameters parameters ) {
        super( parameters );

        // pull the name from the parameters
        faqName = parameters.getString( ADMIN_EDIT_FAQ_PAGE_NAME );

        // extract all of the FAQ items that are in the FAQList
        boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
            public boolean run( Session session ) {
                list = (FAQList) session.createQuery( "select f from FAQList as f where f.name = :name" ).setString( "name", faqName ).uniqueResult();
                for ( Object o : list.getFaqItems() ) {
                    faqItems.add( (FAQItem) o );
                }
                return true;
            }
        } );

        add( new Label( "title", "FAQs for " + faqName ) );

        previewContainer = new MarkupContainer( "preview-container" ) {{
            setOutputMarkupId( true );
        }};
        add( previewContainer );

        itemsContainer = new MarkupContainer( "items-container" ) {{
            setOutputMarkupId( true );
        }};
        add( itemsContainer );

        // display all of the currently active items
        itemsContainer.add( new ListView<FAQItem>( "items", faqItems ) {
            {
                setOutputMarkupId( true );
            }

            @Override protected void populateItem( ListItem<FAQItem> item ) {
                if ( item.getModelObject().isQuestion() ) {
                    item.add( new FAQEditQuestionPanel( "item", getPageContext(), AdminEditFAQPage.this, item.getModelObject() ) );
                }
                else {
                    item.add( new FAQEditHeaderPanel( "item", getPageContext(), AdminEditFAQPage.this, item.getModelObject() ) );
                }
                item.setOutputMarkupId( true );
            }
        } );

        updatePreview();

        // form to add a question
        add( new Form( "add-question" ) {{
            final TextField<String> questionField;

            add( questionField = new TextField<String>( "question", new Model<String>( "" ) ) );

            final Form formReference = this;
            add( new AjaxButton( "submit", this ) {
                @Override protected void onSubmit( AjaxRequestTarget target, Form<?> form ) {
                    addNewQuestion( questionField.getModelObject(), "(add HTML answer here)" );
                    questionField.getModel().setObject( "" );
                    updateItems( target );
                    updatePreview( target );
                }
            } );

            setOutputMarkupId( true );
        }} );

        // form to add a header
        add( new Form( "add-header" ) {{
            final TextField<String> textField;

            add( textField = new TextField<String>( "text", new Model<String>( "" ) ) );

            final Form formReference = this;
            add( new AjaxButton( "submit", this ) {
                @Override protected void onSubmit( AjaxRequestTarget target, Form<?> form ) {
                    addNewHeader( textField.getModelObject() );
                    textField.getModel().setObject( "" );
                    updateItems( target );
                    updatePreview( target );
                }
            } );

            setOutputMarkupId( true );
        }} );

        setOutputMarkupId( true );
    }

    public void updateItems( AjaxRequestTarget target ) {
        target.addComponent( itemsContainer );
    }

    public void updatePreview( AjaxRequestTarget target ) {
        updatePreview();
        target.addComponent( previewContainer );
    }

    public void updatePreview() {
        if ( preview != null ) {
            previewContainer.remove( preview );
        }
        preview = new FAQPanel( "preview", faqName, getPageContext(), false );
        previewContainer.add( preview );
    }

    /**
     * @return A randomly-generated key that will be used for the translation keys of FAQ items
     */
    private static String generateKey() {
        String timeString = Long.toOctalString( System.currentTimeMillis() );
        String doubleString = Double.toHexString( Math.random() * 100 );
        return timeString.substring( Math.max( 0, timeString.length() - 6 ) ) + doubleString.substring( Math.max( 0, doubleString.length() - 6 ) );
    }

    private void addFAQItem( final FAQItem item, final VoidFunction1<Session> postUpdateAction ) {
        Result<FAQItem> result = HibernateUtils.resultTransaction( getHibernateSession(), new Task<FAQItem>() {
            public FAQItem run( Session session ) {
                FAQList faqList = (FAQList) session.load( FAQList.class, list.getId() );

                // initialize the question
                item.setKey( generateKey() );// TODO
                item.setList( faqList );

                // add it to the list
                faqList.getFaqItems().add( item );

                // let hibernate know what to change
                session.save( item );
                session.update( faqList );

                postUpdateAction.apply( session );

                // return the instance
                return item;
            }
        } );

        // if transaction is successful, add it into our in-memory copy
        if ( result.success ) {
            faqItems.add( result.value );
        }
    }

    private void addNewQuestion( final String questionText, final String answerText ) {
        final FAQItem item = new FAQItem();
        item.setQuestion( true );
        addFAQItem( item, new VoidFunction1<Session>() {
            public void apply( Session session ) {
                // set up the English strings, and do it within the transaction so if one part fails, everything is rolled back
                StringUtils.setEnglishStringWithinTransaction( session, item.getQuestionKey(), questionText );
                StringUtils.setEnglishStringWithinTransaction( session, item.getAnswerKey(), answerText );
            }
        } );
    }

    private void addNewHeader( final String headerText ) {
        final FAQItem item = new FAQItem();
        item.setQuestion( false );
        addFAQItem( item, new VoidFunction1<Session>() {
            public void apply( Session session ) {
                // set up the English strings, and do it within the transaction so if one part fails, everything is rolled back
                StringUtils.setEnglishStringWithinTransaction( session, item.getHeaderKey(), headerText );
            }
        } );
    }

    public void delete( final FAQItem item ) {
        final int index = faqItems.indexOf( item );
        boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
            public boolean run( Session session ) {
                FAQList aList = (FAQList) session.load( FAQList.class, list.getId() );
                FAQItem anItem = (FAQItem) session.load( FAQItem.class, item.getId() );

                // clean up the deleted strings
                if ( anItem.isQuestion() ) {
                    StringUtils.deleteStringWithinTransaction( session, anItem.getQuestionKey() );
                    StringUtils.deleteStringWithinTransaction( session, anItem.getAnswerKey() );
                }
                else {
                    StringUtils.deleteStringWithinTransaction( session, anItem.getHeaderKey() );
                }

                // beware of https://hibernate.onjira.com/browse/HHH-1268
                aList.getFaqItems().remove( index );
                session.delete( anItem );
                session.update( aList );
                return true;
            }
        } );
        if ( success ) {
            faqItems.remove( index );
        }
    }

    public void moveUp( FAQItem item ) {
        int index = faqItems.indexOf( item );
        if ( index <= 0 ) {
            return;
        }
        swapItems( index, index - 1 );
    }

    public void moveDown( FAQItem item ) {
        int index = faqItems.indexOf( item );
        if ( index >= faqItems.size() - 1 ) {
            return;
        }
        swapItems( index, index + 1 );
    }

    public void swapItems( final int a, final int b ) {
        boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
            public boolean run( Session session ) {
                FAQList aList = (FAQList) session.load( FAQList.class, list.getId() );
                FAQItem aItem = (FAQItem) session.load( FAQItem.class, faqItems.get( a ).getId() );
                FAQItem bItem = (FAQItem) session.load( FAQItem.class, faqItems.get( b ).getId() );

                Collections.swap( aList.getFaqItems(), a, b );
                session.update( aList );
                session.update( aItem );
                session.update( bItem );
                return true;
            }
        } );
        if ( success ) {
            Collections.swap( faqItems, a, b );
        }
    }
}
