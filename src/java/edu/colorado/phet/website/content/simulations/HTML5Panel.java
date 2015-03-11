// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.content.simulations;

import org.apache.wicket.markup.html.WebMarkupContainer;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.content.DonatePanel;
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

        // add linkers
        add( SimulationPage.getLinker( "acid-base-solutions" ).getLink( "acid-base-solutions-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "area-builder" ).getLink( "area-builder-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "balancing-act" ).getLink( "balancing-act-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "balancing-chemical-equations" ).getLink( "balancing-chemical-equations-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "balloons-and-static-electricity" ).getLink( "balloons-and-static-electricity-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "beers-law-lab" ).getLink( "beers-law-lab-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "build-an-atom" ).getLink( "build-an-atom-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "color-vision" ).getLink( "color-vision-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "concentration" ).getLink( "concentration-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "energy-skate-park-basics" ).getLink( "energy-skate-park-basics-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "faradays-law" ).getLink( "faradays-law-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "forces-and-motion-basics" ).getLink( "forces-and-motion-basics-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "fraction-matcher" ).getLink( "fraction-matcher-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "friction" ).getLink( "friction-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "graphing-lines" ).getLink( "graphing-lines-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "gravity-force-lab" ).getLink( "gravity-force-lab-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "john-travoltage" ).getLink( "john-travoltage-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "molarity" ).getLink( "molarity-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "ohms-law" ).getLink( "ohms-law-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "ph-scale" ).getLink( "ph-scale-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "ph-scale-basics" ).getLink( "ph-scale-basics-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "reactants-products-and-leftovers" ).getLink( "reactants-products-and-leftovers-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "resistance-in-a-wire" ).getLink( "resistance-in-a-wire-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "under-pressure" ).getLink( "under-pressure-link", context, getPhetCycle() ) );
        add( SimulationPage.getLinker( "wave-on-a-string" ).getLink( "wave-on-a-string-link", context, getPhetCycle() ) );

//        add( new StaticImage( "html-video-thumbnail", Images.HTML5_VIDEO_THUMBNAIL_220, null ) );
    }

}