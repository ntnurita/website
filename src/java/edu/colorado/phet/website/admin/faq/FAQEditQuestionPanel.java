// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.admin.faq;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.model.Model;

import edu.colorado.phet.website.data.faq.FAQItem;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.StringUtils;

/**
 * Displays an editable (or deletable) FAQ question and answer
 */
public class FAQEditQuestionPanel extends FAQEditItemPanel {

    private static final Logger logger = Logger.getLogger( FAQEditQuestionPanel.class.getName() );

    public FAQEditQuestionPanel( String id, PageContext context, final AdminEditFAQPage page, final FAQItem item ) {
        super( id, context, page, item );

        // set up the models for the strings we are using, and initialize them
        String initialQuestionString = StringUtils.getEnglishStringDirect( getHibernateSession(), item.getQuestionKey() );
        String initialAnswerString = StringUtils.getEnglishStringDirect( getHibernateSession(), item.getAnswerKey() );
        final Model<String> questionModel = new Model<String>( StringUtils.mapStringForEditing( initialQuestionString ) );
        final Model<String> answerModel = new Model<String>( StringUtils.mapStringForEditing( initialAnswerString ) );

        // question textbox
        add( new AjaxEditableMultiLineLabel<String>( "question", questionModel ) {
            {
                setCols( 80 );
            }

            @Override protected void onSubmit( AjaxRequestTarget target ) {
                super.onSubmit( target );
                logger.info( "setting FAQ string " + item.getQuestionKey() + " to " + questionModel.getObject() );
                StringUtils.setEnglishString( getHibernateSession(), item.getQuestionKey(), questionModel.getObject() );

                // update this after the change
                page.updatePreview( target );
                target.addComponent( FAQEditQuestionPanel.this.getParent() );
            }
        } );

        // answer textbox
        add( new AjaxEditableMultiLineLabel<String>( "answer", answerModel ) {
            {
                setCols( 80 );
                setRows( 20 );
            }

            @Override protected void onSubmit( AjaxRequestTarget target ) {
                super.onSubmit( target );
                logger.info( "setting FAQ string " + item.getAnswerKey() + " to " + answerModel.getObject() );
                StringUtils.setEnglishString( getHibernateSession(), item.getAnswerKey(), answerModel.getObject() );

                // update this after the change
                page.updatePreview( target );
                target.addComponent( FAQEditQuestionPanel.this.getParent() );
            }
        } );
    }
}