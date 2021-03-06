// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.content.forteachers;

import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

/**
 * Displays a vertical list of social bookmarking service icons
 */
public class TipsRighthandMenu extends PhetPanel {

    public TipsRighthandMenu( String id, final PageContext context, final String bookmarkableUrl ) {
		super(id, context);

		add( new StaticImage( "planningToUsePhet", "forTeachers.planningToUsePhet".equalsIgnoreCase(bookmarkableUrl) ? Images.DOWNARROW : Images.BLANK, "downarrow") );
		add( new StaticImage( "usingPhetInLecture", "forTeachers.usingPhetInLecture".equalsIgnoreCase(bookmarkableUrl) ? Images.DOWNARROW : Images.BLANK, "downarrow") );
		add( new StaticImage( "lectureDemo", "forTeachers.lectureDemo".equalsIgnoreCase(bookmarkableUrl) ? Images.DOWNARROW : Images.BLANK, "downarrow") );
		add( new StaticImage( "clickersDemo", "forTeachers.clickersDemo".equalsIgnoreCase(bookmarkableUrl) ? Images.DOWNARROW : Images.BLANK, "downarrow") );
		add( new StaticImage( "activitesDesign", "forTeachers.activitesDesign".equalsIgnoreCase(bookmarkableUrl) ? Images.DOWNARROW : Images.BLANK, "downarrow") );
		add( new StaticImage( "facilitatingActivities", "forTeachers.facilitatingActivities".equalsIgnoreCase(bookmarkableUrl) ? Images.DOWNARROW : Images.BLANK, "downarrow") );
		add( new StaticImage( "virtualWorkshop", "forTeachers.virtualWorkshop".equalsIgnoreCase(bookmarkableUrl) ? Images.DOWNARROW : Images.BLANK, "downarrow") );

        // add linkers
        add ( PlanningPanel.getLinker().getLink( "planning-link-1", context, getPhetCycle() ) );
        add ( PlanningPanel.getLinker().getLink( "planning-link-2", context, getPhetCycle() ) );

        add ( LectureOverviewPanel.getLinker().getLink( "lecture-link-1", context, getPhetCycle() ) );
        add ( LectureOverviewPanel.getLinker().getLink( "lecture-link-2", context, getPhetCycle() ) );

        add ( LectureDemoPanel.getLinker().getLink( "lecture-demo-link-1", context, getPhetCycle() ) );
        add ( LectureDemoPanel.getLinker().getLink( "lecture-demo-link-2", context, getPhetCycle() ) );

        add ( ClickersPanel.getLinker().getLink( "clickers-demo-link-1", context, getPhetCycle() ) );
        add ( ClickersPanel.getLinker().getLink( "clickers-demo-link-2", context, getPhetCycle() ) );

        add ( ActivitiesdesignPanel.getLinker().getLink( "activities-design-link-1", context, getPhetCycle() ) );
        add ( ActivitiesdesignPanel.getLinker().getLink( "activities-design-link-2", context, getPhetCycle() ) );

        add ( FacilitatingActivitiesPanel.getLinker().getLink( "facilitating-activities-link-1", context, getPhetCycle() ) );
        add ( FacilitatingActivitiesPanel.getLinker().getLink( "facilitating-activities-link-2", context, getPhetCycle() ) );

        add ( VirtualWorkshopPanel.getLinker().getLink( "virtual-workshop-link-1", context, getPhetCycle() ) );
        add ( VirtualWorkshopPanel.getLinker().getLink( "virtual-workshop-link-2", context, getPhetCycle() ) );
	}
}