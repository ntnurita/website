/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * Contains strings that have been addedor modified since the last production deployment. If strings by those key names
 * don't exist they will be created.
 */
public class StringChanges {

    private static Logger logger = Logger.getLogger( StringChanges.class );

    public static void checkNewStrings() {
        Session session = HibernateUtils.getInstance().openSession();

        StringUtils.addString( session, "emergencyMessage", "" );
        StringUtils.addString( session, "about.licensing.trademarkLicensing", "Trademark Licensing" );
        StringUtils.addString( session, "about.licensing.trademarkText", "The University of Colorado is the owner of the PhET trademark and notifies the public of its common law rights with the designation \"PhET™\". Third party users hereby have the right to use the PhET trademark under a royalty-free, non-exclusive license, only in conjunction with simulations from this site. Use of the ™ after the PhET mark is not required." );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
