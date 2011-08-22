// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.admin.faq;

import org.apache.log4j.Logger;

import edu.colorado.phet.website.data.faq.FAQItem;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

/**
 * Displays an editable (or deletable) FAQ Item, either a question or header.
 */
public class FAQEditItemPanel extends PhetPanel {

    private static final Logger logger = Logger.getLogger( FAQEditItemPanel.class.getName() );

    public FAQEditItemPanel( String id, PageContext context, AdminEditFAQPage page, FAQItem item ) {
        super( id, context );

        // don't show the external div tag that we wrap this with in AdminEditFAQPage.html
        setRenderBodyOnly( true );

        // remember to output the markup ID, so that Ajax requests can update just this part
        setOutputMarkupId( true );
    }
}