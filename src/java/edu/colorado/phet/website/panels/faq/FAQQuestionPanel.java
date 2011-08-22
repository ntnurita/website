// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.panels.faq;

import org.apache.log4j.Logger;

import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.data.faq.FAQItem;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

/**
 * Displays a header inside of a FAQ page
 */
public class FAQQuestionPanel extends PhetPanel {

    private static final Logger logger = Logger.getLogger( FAQQuestionPanel.class.getName() );

    public FAQQuestionPanel( String id, final PageContext context, FAQItem item ) {
        super( id, context );

        add( new RawLabel( "question", getPhetLocalizer().getString( item.getQuestionKey(), this ) ) );
        add( new RawLabel( "answer", getPhetLocalizer().getString( item.getAnswerKey(), this ) ) );
    }

}