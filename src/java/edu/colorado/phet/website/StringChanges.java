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

        StringUtils.addString( session, "social.blog.tooltip", "Visit our blog" );
        StringUtils.addString( session, "social.newsletter.tooltip", "Subscribe to our newsletter" );
        StringUtils.addString( session, "social.pinterest.tooltip", "Pinterest" );

        StringUtils.deleteString( session, "home.playWithSims" );
        StringUtils.addString( session, "home.playWithSims", "Play with Simulations" );

        StringUtils.deleteString( session, "home.facebookText" );
        StringUtils.deleteString( session, "home.twitterText" );
        StringUtils.deleteString( session, "home.blogText" );
        StringUtils.deleteString( session, "home.subheader" );

        StringUtils.addString( session, "home.blogText", "our blog" );
        StringUtils.addString( session, "home.newsletterText", "newsletter" );
        StringUtils.addString( session, "home.facebookText", "find us" );
        StringUtils.addString( session, "home.youtubeText", "watch us" );
        StringUtils.addString( session, "home.twitterText", "follow us" );
        StringUtils.addString( session, "home.pinterestText", "pin us" );

        StringUtils.deleteString( session, "html5.tryOurNewHTML5Sims" );
        StringUtils.addString( session, "html5.tryOurNewHTML5Sims", "HTML5 Sims" );

        StringUtils.deleteString( session, "html5.crowdfundedProjects" );
        StringUtils.addString( session, "html5.crowdfundedProjects", "Donate:" );

        StringUtils.deleteString( session, "html5.bringCCK" );
        StringUtils.addString( session, "html5.bringCCK", "Build HTML5 Circuit Sim" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
