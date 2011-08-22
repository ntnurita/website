// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.admin.faq;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;

import edu.colorado.phet.website.admin.AdminPage;

/**
 * Page for editing FAQs by administrators
 */
public class AdminEditFAQPage extends AdminPage {

    public static final String ADMIN_EDIT_FAQ_PAGE_NAME = "admin-faq-page-name";

    private static final Logger logger = Logger.getLogger( AdminEditFAQPage.class.getName() );

    public AdminEditFAQPage( PageParameters parameters ) {
        super( parameters );

        String faqName = parameters.getString( ADMIN_EDIT_FAQ_PAGE_NAME );

        System.out.println( "FAQ name: " + faqName );
    }
}
