/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.forteachers;

import org.apache.wicket.Component;

import edu.colorado.phet.website.content.ForTranslatorsPanel;
import edu.colorado.phet.website.content.contribution.ContributionBrowsePage;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;
import edu.colorado.phet.website.util.wicket.IComponentFactory;
import edu.colorado.phet.website.util.wicket.WicketUtils;

public class TipsPanel extends ForTeachersPanel {

     public TipsPanel( String id, PageContext context ) {
         super( id, context );

         // add linkers
         add( ContributionBrowsePage.getLinker().getLink( "browse-activities-link", context, getPhetCycle() ) );
         add( PlanningPanel.getLinker().getLink( "planning-to-use-phet-link", context, getPhetCycle() ) );
         add( LectureOverviewPanel.getLinker().getLink( "using-phet-in-lecture-link", context, getPhetCycle() ) );
         add( LectureDemoPanel.getLinker().getLink( "lecture-demo-link", context, getPhetCycle() ) );
         add( ClickersPanel.getLinker().getLink( "clickers-demo-link", context, getPhetCycle() ) );
         add( ActivitiesdesignPanel.getLinker().getLink( "activities-design-link", context, getPhetCycle() ) );
         add( FacilitatingActivitiesPanel.getLinker().getLink( "facilitating-activities-link", context, getPhetCycle() ) );
         add( VirtualWorkshopPanel.getLinker().getLink( "virtual-workshop-link", context, getPhetCycle() ) );
    }

    public static String getKey() {
        return "forTeachers.tipsForUsingPhet";
    }

    public static String getUrl() {
        return "for-teachers/tipsForUsingPhet";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        addRighthandMenu( getKey() );
    }
}