// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.content.simulations;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.content.DonatePanel;
import edu.colorado.phet.website.data.Category;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.panels.SocialBookmarkPanel;
import edu.colorado.phet.website.panels.sponsor.SimSponsorPanel;
import edu.colorado.phet.website.panels.sponsor.Sponsor;
import edu.colorado.phet.website.util.PageContext;

public class HTML5Panel extends PhetPanel {

    public HTML5Panel( String id, final PageContext context ) {
        super( id, context );

        add( DonatePanel.getLinker().getLink( "donate-link", context, getPhetCycle() ) );

        if ( getPhetCycle().isInstaller() ) {
            add( new WebMarkupContainer( "sim-sponsor-installer-js" ) );
        }
        else {
            add( new InvisibleComponent( "sim-sponsor-installer-js" ) );
        }

        if ( DistributionHandler.showSimSponsor( getPhetCycle() ) ) {
            // this gets cached, so it will stay the same for the sim (but will be different for different sims)
            add( new SimSponsorPanel( "html-sponsor", context, Sponsor.chooseRandomSimSponsor() ) );
        }
        else {
            add( new InvisibleComponent( "html-sponsor" ) );
        }

        add( new SocialBookmarkPanel( "social-bookmark-panel", context, getFullPath( context ), getPhetLocalizer().getString( "nav.html", this ) ) );

        if ( this.getLocale().equals( WebsiteConstants.ENGLISH ) ) {
            add( new InvisibleComponent( "html-translations-coming-soon" ) );
        } else {
            add( new WebMarkupContainer( "html-translations-coming-soon" ) );
        }

//        add( new StaticImage( "html-video-thumbnail", Images.HTML5_VIDEO_THUMBNAIL_220, null ) );
    }

}