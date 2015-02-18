/*
 * Copyright 2015, University of Colorado
 */

package edu.colorado.phet.website.content.forteachers;

import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class FacilitatingActivitiesPanel extends ForTeachersPanel {

    public FacilitatingActivitiesPanel( String id, PageContext context ) {
        super( id, context );
    }

    public static String getKey() {
        return "forTeachers.facilitatingActivities";
    }

    public static String getUrl() {
        return "for-teachers/facilitatingActivities";
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