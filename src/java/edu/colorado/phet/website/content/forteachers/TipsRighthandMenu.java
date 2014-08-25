// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.content.forteachers;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.Image;

import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.content.contribution.ContributionBrowsePage;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

/**
 * Displays a vertical list of social bookmarking service icons
 */
public class TipsRighthandMenu extends PhetPanel {


	public TipsRighthandMenu(String id, final PageContext context,
			final String bookmarkableUrl) {
		super(id, context);

		add( new StaticImage( "planningToUsePhet", "planningToUsePhet".equalsIgnoreCase(bookmarkableUrl) ? Images.DOWNARROW : Images.BLANK, "downarrow") );
		add( new StaticImage( "usingPhetInLecture", "usingPhetInLecture".equalsIgnoreCase(bookmarkableUrl) ? Images.DOWNARROW : Images.BLANK, "downarrow") );
		add( new StaticImage( "lectureDemo", "lectureDemo".equalsIgnoreCase(bookmarkableUrl) ? Images.DOWNARROW : Images.BLANK, "downarrow") );
		add( new StaticImage( "clickersDemo", "clickersDemo".equalsIgnoreCase(bookmarkableUrl) ? Images.DOWNARROW : Images.BLANK, "downarrow") );
		add( new StaticImage( "activitesDesign", "activitesDesign".equalsIgnoreCase(bookmarkableUrl) ? Images.DOWNARROW : Images.BLANK, "downarrow") );
		add( new StaticImage( "virtualWorkshop", "virtualWorkshop".equalsIgnoreCase(bookmarkableUrl) ? Images.DOWNARROW : Images.BLANK, "downarrow") );
	}
}