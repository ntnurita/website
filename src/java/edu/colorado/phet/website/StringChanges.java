/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * Contains strings that have been added or modified since the last production deployment. If strings by those key names
 * don't exist they will be created.
 */
public class StringChanges {

    private static Logger logger = Logger.getLogger( StringChanges.class );

    public static void checkNewStrings() {
        Session session = HibernateUtils.getInstance().openSession();

//        StringUtils.deleteString( session, "simulationMainPanel.version" );
//        StringUtils.deleteString( session, "simulationMainPanel.kilobytes" );
//        StringUtils.addString( session, "simulationMainPanel.version", "Version {0}" );


        StringUtils.addString( session, "nav.forTeachers.facilitatingActivities", "Facilitating PhET Activities for the K12 Classroom" );
        StringUtils.addString( session, "forTeachers.facilitatingActivities.title", "Facilitating PhET Activities for the K12 Classroom" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
