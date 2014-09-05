/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.forteachers;

import org.apache.wicket.Component;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.content.contribution.ContributionBrowsePage;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;
import edu.colorado.phet.website.util.wicket.IComponentFactory;
import edu.colorado.phet.website.util.wicket.WicketUtils;

public class LectureOverviewPanel extends PhetPanel {
	 boolean addedTips = false;
	 PageContext context2 = null;
    public LectureOverviewPanel( String id, PageContext context ) {
        super( id, context );
        this.context2 = context;
        add( ContributionBrowsePage.getLinker().getLink( "browse-activities-link", context, getPhetCycle() ) );

    }

    public static String getKey() {
        return "forTeachers.usingPhetInLecture";
    }

    public static String getUrl() {
        return "for-teachers/usingPhetInLecture";
    }
    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        ((PhetMenuPage) this.getPage()).hideSocialBookmarkButtons();
        ((PhetMenuPage) this.getPage()).setContentWidth(1120);
        if ( !addedTips ) {
            add( WicketUtils.componentIf( true, "righthand-menu-panel", new IComponentFactory<Component>() {
                public Component create( String id ) {
                    return new TipsRighthandMenu( "righthand-menu-panel", context2, "usingPhetInLecture" );
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