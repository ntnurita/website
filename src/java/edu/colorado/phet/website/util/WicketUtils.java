/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.util;

import org.apache.wicket.markup.html.list.ListItem;

public class WicketUtils {

    /**
     * Highlight every other row of a list view. Call within the populateItem( ListItem item ) loop.
     *
     * @param item The list item to possibly highlight
     */
    public static void highlightListItem( ListItem item ) {
        if ( item.getIndex() % 2 == 0 ) {
            item.add( new ClassAppender( "list-highlight-background" ) );
        }
    }
}
