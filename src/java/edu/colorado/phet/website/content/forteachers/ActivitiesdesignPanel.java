/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.forteachers;

import org.apache.wicket.Component;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.content.contribution.ContributionBrowsePage;
import edu.colorado.phet.website.content.contribution.ContributionPage;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.panels.SocialBookmarkPanel;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;
import edu.colorado.phet.website.util.wicket.IComponentFactory;
import edu.colorado.phet.website.util.wicket.WicketUtils;

public class ActivitiesdesignPanel extends PhetPanel {
	PageContext context2 = null;
	 boolean addedTips = false;
    public ActivitiesdesignPanel( String id, PageContext context ) {
        super( id, context );
        this.context2 = context;

        // add linkers
        add( ContributionBrowsePage.getLinker().getLink( "browse-activities-link", context, getPhetCycle() ) );

        add( ContributionPage.getLinker( 3448 ).getLink( "contribution-3448-link", context, getPhetCycle() ) );
        add( ContributionPage.getLinker( 3314 ).getLink( "contribution-3314-link", context, getPhetCycle() ) );
        add( ContributionPage.getLinker( 3401 ).getLink( "contribution-3401-link", context, getPhetCycle() ) );
        add( ContributionPage.getLinker( 3503 ).getLink( "contribution-3503-link", context, getPhetCycle() ) );
        add( ContributionPage.getLinker( 3423 ).getLink( "contribution-3423-link", context, getPhetCycle() ) );
        add( ContributionPage.getLinker( 3584 ).getLink( "contribution-3584-link", context, getPhetCycle() ) );
    }
   
    public static String getKey() {
        return "forTeachers.activitesDesign";
    }

    public static String getUrl() {
        return "for-teachers/activitesDesign";
    }
    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        ((PhetMenuPage) this.getPage()).hideSocialBookmarkButtons();
        ((PhetMenuPage) this.getPage()).setContentWidth(1120);
        if ( !addedTips ) {
            add( WicketUtils.componentIf( true, "righthand-menu-panel", new IComponentFactory<Component>() {
                public Component create( String id ) {
                    return new TipsRighthandMenu( "righthand-menu-panel", context2, "activitesDesign" );
                }
            } ) );
            addedTips = true;
        }

    }
     public static RawLinkable getLinker() {
    	         return new AbstractLinker() {
    	             public String getSubUrl( PageContext context ) {
    	                 return getUrl();
    	             }
    	         };
    	     }
}