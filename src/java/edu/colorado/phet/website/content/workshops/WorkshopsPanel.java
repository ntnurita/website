/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.workshops;

import org.apache.wicket.Component;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.content.TeacherIdeasPanel;
import edu.colorado.phet.website.content.about.AboutNewsPanel;
import edu.colorado.phet.website.content.forteachers.TipsRighthandMenu;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;
import edu.colorado.phet.website.util.wicket.IComponentFactory;
import edu.colorado.phet.website.util.wicket.WicketUtils;

public class WorkshopsPanel extends PhetPanel {
    public WorkshopsPanel( String id, PageContext context ) {
        super( id, context );

        // add linkers
        add( AboutNewsPanel.getLinker().getLink( "about-news-link", context, getPhetCycle() ) );
        add( UgandaWorkshopsPanel.getLinker().getLink( "workshop-uganda-link", context, getPhetCycle() ) );
        add( ExampleWorkshopsPanel.getLinker().getLink( "example-workshops-link", context, getPhetCycle() ) );
    }

    public static String getKey() {
        return "workshops";
    }

    public static String getUrl() {
        return "for-teachers/workshops";
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        // for some reason a 55 pixel offset is needed to actually make this page the same width as the other for teacher pages
        ((PhetMenuPage) this.getPage()).setContentWidth( TeacherIdeasPanel.FOR_TEACHERS_PAGE_WIDTH - 55 );
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            @Override
            public String getRawUrl( PageContext context, PhetRequestCycle cycle ) {
                if ( cycle != null && DistributionHandler.redirectPageClassToProduction( cycle, WorkshopsPanel.class ) ) {
                    return "http://phet.colorado.edu/teacher_ideas/workshops.php";
                }
                else {
                    return super.getRawUrl( context, cycle );
                }
            }

            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }
}