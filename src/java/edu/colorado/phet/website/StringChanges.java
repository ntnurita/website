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

        StringUtils.addString( session, "profile.receiveSimulationNotifications", "Email when PhET posts a new sim" );
        StringUtils.overwriteString( session, "profile.receiveSimulationNotifications", "Email when PhET posts a new sim", "Email me when new simulations are posted" );
        StringUtils.overwriteString( session, "profile.receiveEmail", "Receive PhET Email:", "Receive PhET Email" );
        StringUtils.overwriteString( session, "profile.receiveEmail", "Receive PhET Email", "Email me PhET's Newsletter (~4/year)" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
