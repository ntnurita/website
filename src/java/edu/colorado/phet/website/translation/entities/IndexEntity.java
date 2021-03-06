/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation.entities;

import edu.colorado.phet.website.content.IndexPanel;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.translation.PhetPanelFactory;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;

public class IndexEntity extends TranslationEntity {
    public IndexEntity() {
        addString( "home.header" );
        addString( "home.playWithSims" );
        addString( "home.interactiveSimulations" );
        addString( "home.simulationsDelivered", "{0} will be replaced by the current number of sims delivered" );
        addString( "home.facebookText"  );
        addString( "home.twitterText" );
        addString( "home.blogText" );
        addString( "home.newsletterText" );
        addString( "home.youtubeText" );
        addString( "home.pinterestText" );
        addString( "home.subscribeToNewsletter" );
        addString( "home.stayConnected" );

        addString( "home.balancingAct" );
        addString( "home.colorVision" );
        addString( "home.energySkateParkBasics" );
        addString( "home.faradaysLaw" );
        addString( "home.friction" );
        addString( "home.johnTravoltage" );
        addString( "home.waveOnAString" );

        addString( "home.runOurSims" );
        addString( "home.help" );
        addString( "home.onLine" );
        addString( "home.troubleshooting" );
        addString( "home.faqs" );
        addString( "home.fullInstallation" );
        addString( "home.oneAtATime" );
        addString( "home.teacherIdeasAndActivities" );
        addString( "home.browseActivities" );
        addString( "home.donate" );
        addString( "home.workshops" );
        addString( "tipsForUsingPhet");
        addString( "home.submitActivity" );
        addString( "home.supportPhet" );
        addString( "home.translateSimulations" );
        addString( "home.translateWebsite" );
        addString( "home.browseSims" );
        addString( "home.simulations" );
        addString( "home.rotator.next" );
        addString( "home.rotator.previous" );
        addString( "home.about" );
        addString( "home.about.phet" );
        addString( "home.about.news" );
        addString( "home.about.contact" );
        addString( "home.about.sponsors" );
        addString( "home.about.phetSupportedBy" );
        addString( "home.about.research" );
        addString( "home.about.alongWithOurSponsors" );

        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                            return new IndexPanel( id, context );
                        }
                    }, "Home Page" );
    }

    public String getDisplayName() {
        return "Home";
    }

    @Override
    public int getMinDisplaySize() {
        return 765;
    }
}
