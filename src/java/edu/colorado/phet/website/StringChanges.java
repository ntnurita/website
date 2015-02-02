/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

import com.navnorth.learningregistry.util.StringUtil;

/**
 * Contains strings that have been added or modified since the last production deployment. If strings by those key names
 * don't exist they will be created.
 */
public class StringChanges {

    private static Logger logger = Logger.getLogger( StringChanges.class );

    public static void checkNewStrings() {
        Session session = HibernateUtils.getInstance().openSession();

        StringUtils.addString( session, "home.interactiveSimulations", "<strong>INTERACTIVE</strong> SIMULATIONS<br/>FOR SCIENCE AND MATH" );

        StringUtils.addString( session, "home.balancingAct", "Balancing Act" );
        StringUtils.addString( session, "home.colorVision", "Color Vision" );
        StringUtils.addString( session, "home.energySkateParkBasics", "Energy Skate Part Basics" );
        StringUtils.addString( session, "home.faradaysLaw", "Faraday's Law" );
        StringUtils.addString( session, "home.friction", "Friction" );
        StringUtils.addString( session, "home.johnTravoltage", "John Travoltage" );
        StringUtils.addString( session, "home.waveOnAString", "Wave on a String" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
