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
        StringUtils.addString( session, "html5.crowdfundedProjects", "Support PhET's Annual Campaign:" );

        StringUtils.addString( session, "html5.donateToday", "Donate Today" );

        StringUtils.deleteString( session, "html5.bringCCK" );
        StringUtils.deleteString( session, "html5.buildTeachWithPhet" );

        StringUtils.addString( session, "troubleshooting.mac.q7.title", "When I click \"run now\" my computer asks me which application to use to open the file, but Java isn’t listed." );
        StringUtils.addString( session, "troubleshooting.mac.q7.answer", "<p> " +
                                                                         "In order to open a '.jnlp' file (the type of file used for “run now”), you will need to use Java Web Start. Java Web Start is part of " +
                                                                         "the default Java installation. " +
                                                                         "</p> " +
                                                                         "<p> " +
                                                                         "When you are prompted with the dialog asking for the app to use to open the file, click \"Choose program\", your finder window should " +
                                                                         "open. Press Cmd-Shift-G to open the go to folder. Paste this folder: /System/Library/CoreServices " +
                                                                         "This is where Java Web Start should be located on default mac installation. There should be an executable called 'Java-Web-Start'. " +
                                                                         "Select this app. " +
                                                                         "</p>" );

        StringUtils.addString( session, "nav.ipad-tablet", "iPad/Tablet" );
        StringUtils.addString( session, "nav.chromebook", "Chromebook" );
        StringUtils.addString( session, "nav.by-device", "By Device" );

        StringUtils.addString( session, "social.homepage.blog.tooltip", "Visit our blog" );
        StringUtils.addString( session, "social.homepage.newsletter.tooltip", "Subscribe to our newsletter" );
        StringUtils.addString( session, "social.homepage.facebook.tooltip", "Find us on Facebook" );
        StringUtils.addString( session, "social.homepage.youtube.tooltip", "Watch us on Youtube" );
        StringUtils.addString( session, "social.homepage.twitter.tooltip", "Follow us on Twitter" );
        StringUtils.addString( session, "social.homepage.pinterest.tooltip", "Pin us on Pinterest" );

        StringUtils.addString( session, "donation-banner.html5.tooltip", "Go to HTML5 Sims" );

        StringUtils.deleteString( session, "home.simulationsDelivered" );
        StringUtils.addString( session, "home.simulationsDelivered", "Over 200 million simulations delivered" );
        
        StringUtils.deleteString( session, "installer.mostUpToDate" );
        StringUtils.addString( session, "installer.mostUpToDate", "Created {0,date,short}. Updates available <a {1}>online</a>." );

        StringUtils.addString( session, "home.about.research", "Research on PhET" );

        StringUtils.deleteString( session, "social.digg.tooltip" );
        StringUtils.deleteString( session, "social.delicious.tooltip" );

        StringUtils.deleteString( session, "search.search" );
        StringUtils.addString( session, "search.search", "Search" );
        StringUtils.addString( session, "search.label", "Search the PhET Website" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
