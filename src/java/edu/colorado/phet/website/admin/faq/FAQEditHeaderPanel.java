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
 * Displays an editable (or deletable) FAQ header
 */
public class FAQEditHeaderPanel extends FAQEditItemPanel {

    private static final Logger logger = Logger.getLogger( FAQEditHeaderPanel.class.getName() );

    public FAQEditHeaderPanel( String id, PageContext context, final AdminEditFAQPage page, final FAQItem item ) {
        super( id, context, page, item );
        // set up the models for the strings we are using, and initialize them
        String initialString = StringUtils.getEnglishStringDirect( getHibernateSession(), item.getHeaderKey() );
        final Model<String> headerModel = new Model<String>( StringUtils.mapStringForEditing( initialString ) );

        // header textbox
        add( new AjaxEditableMultiLineLabel<String>( "header", headerModel ) {
            {
                setCols( 80 );
            }

            @Override protected void onSubmit( AjaxRequestTarget target ) {
                super.onSubmit( target );
                logger.info( "setting FAQ string " + item.getHeaderKey() + " to " + headerModel.getObject() );
                StringUtils.setEnglishString( getHibernateSession(), item.getHeaderKey(), headerModel.getObject() );

                // update this after the change
                page.updatePreview( target );
                target.addComponent( FAQEditHeaderPanel.this.getParent() );
            }
        } );
    }
}