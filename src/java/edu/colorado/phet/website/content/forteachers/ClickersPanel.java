/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.forteachers;

import org.apache.wicket.Component;

import edu.colorado.phet.website.content.contribution.ContributionBrowsePage;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;
import edu.colorado.phet.website.util.wicket.IComponentFactory;
import edu.colorado.phet.website.util.wicket.WicketUtils;

public class ClickersPanel extends ForTeachersPanel {

    public ClickersPanel( String id, PageContext context ) {
        super( id, context );

        // add linkers
        add( ContributionBrowsePage.getLinker().getLink( "browse-activities-link", context, getPhetCycle() ) );
    }

    public static String getKey() {
        return "forTeachers.clickersDemo";
    }

    public static String getUrl() {
        return "for-teachers/clickersDemo";
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