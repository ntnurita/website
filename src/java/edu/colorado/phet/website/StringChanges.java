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

        StringUtils.addString( session, "webglDisabled.windows", "Windows" );
        StringUtils.addString( session, "webglDisabled.mac", "Mac" );
        StringUtils.addString( session, "webglDisabled.mobile", "Mobile" );

//        StringUtils.deleteString( session, "simulationMainPanel.teachingResources" );
//        StringUtils.deleteString( session, "simulationMainPanel.mainTopics" );

        StringUtils.addString( session, "simulationMainPanel.about", "About" );
        StringUtils.addString( session, "simulationMainPanel.topics", "Topics" );
        StringUtils.addString( session, "simulationMainPanel.description", "Description" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
