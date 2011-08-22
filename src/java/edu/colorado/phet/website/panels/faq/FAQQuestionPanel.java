// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.panels.faq;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.data.faq.FAQItem;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

/**
 * Displays a header inside of a FAQ page
 */
public class FAQQuestionPanel extends PhetPanel {

    private static final Logger logger = Logger.getLogger( FAQQuestionPanel.class.getName() );

    public FAQQuestionPanel( String id, final PageContext context, final FAQItem item ) {
        super( id, context );

        add( new RawLabel( "question", getPhetLocalizer().getString( item.getQuestionKey(), this ) ) );
        add( new RawLabel( "answer", getPhetLocalizer().getString( item.getAnswerKey(), this ) ) );

        // add in the HTML anchor so that we can link to pages like faq#key and it scrolls to the question
        add( new Label( "anchor", "" ) {{
            add( new AttributeModifier( "name", true, new Model<String>( item.getKey() ) ) );
        }} );
    }

}