// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.panels.faq;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.hibernate.Session;

import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.data.faq.FAQItem;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.Result;
import edu.colorado.phet.website.util.hibernate.Task;

public class FAQPanel extends PhetPanel implements Serializable {

    private static final Logger logger = Logger.getLogger( FAQPanel.class.getName() );

    public FAQPanel( String id, final String faqName, final PageContext context ) {
        this( id, faqName, context, true );
    }

    public FAQPanel( String id, final String faqName, final PageContext context, boolean hideAnswers ) {
        super( id, context );

        Result<List<FAQItem>> items = HibernateUtils.resultTransaction( getHibernateSession(), new Task<List<FAQItem>>() {
            public List<FAQItem> run( final Session session ) {
                ArrayList<FAQItem> result = new ArrayList<FAQItem>();
                result.addAll( session.createQuery( "select f from FAQItem as f where f.list.name = :name" ).setString( "name", faqName ).list() );
                return result;
            }
        } );

        add( new ListView<FAQItem>( "items", items.value ) {
            @Override protected void populateItem( ListItem<FAQItem> viewItem ) {
                FAQItem item = viewItem.getModelObject();
                viewItem.setRenderBodyOnly( true );

                if ( item.isQuestion() ) {
                    viewItem.add( new FAQQuestionPanel( "item", context, item ) );
                }
                else {
                    viewItem.add( new FAQHeaderPanel( "item", context, item ) );
                }
            }
        } );

        if ( hideAnswers ) {
            // show some JS that hides the answers initially, and hooks up the other parts
            add( new MarkupContainer( "activation-js" ) {
            } );
        }
        else {
            add( new InvisibleComponent( "activation-js" ) );
        }
    }

}