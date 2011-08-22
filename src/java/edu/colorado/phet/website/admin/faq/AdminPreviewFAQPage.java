// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.admin.faq;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;

import edu.colorado.phet.website.admin.AdminPage;
import edu.colorado.phet.website.panels.faq.FAQPanel;

/**
 * Shows a preview of a FAQ page (for one that may not be visible to non-administrators)
 */
public class AdminPreviewFAQPage extends AdminPage {

    public static final String ADMIN_PREVIEW_FAQ_PAGE_NAME = "admin-faq-page-name"; // key used for parameter passing

    private static final Logger logger = Logger.getLogger( AdminPreviewFAQPage.class.getName() );

    public AdminPreviewFAQPage( PageParameters parameters ) {
        super( parameters );

        String faqName = parameters.getString( ADMIN_PREVIEW_FAQ_PAGE_NAME );

        add( new Label( "faq-preview-header", "FAQ Preview for " + faqName ) );

        add( new FAQPanel( "preview", faqName, getPageContext() ) );
    }
}
