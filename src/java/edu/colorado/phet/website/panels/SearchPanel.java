/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.panels;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainerWithAssociatedMarkup;
import org.apache.wicket.model.Model;

import edu.colorado.phet.website.constants.JS;
import edu.colorado.phet.website.util.PageContext;

public class SearchPanel extends PhetPanel {
    public SearchPanel( String id, PageContext context ) {
        super( id, context );

        add( JavascriptPackageResource.getHeaderContribution( JS.JQUERY_AUTOCOMPLETE ) );

        WebMarkupContainerWithAssociatedMarkup form = new WebMarkupContainerWithAssociatedMarkup( "search-form" );
        form.add( new AttributeAppender( "action", true, new Model<String>( context.getPrefix() + "search" ), " " ) );
        add( form );
    }
}