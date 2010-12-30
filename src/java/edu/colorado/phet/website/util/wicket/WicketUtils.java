/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.util.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.list.ListItem;

import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.util.ClassAppender;

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

    /**
     * Display the specified component from the factory if visible is true, otherwise don't show anything.
     * <p/>
     * Had this been a language like Scala or Clojure, we could make this into a much nicer-to-read version.
     *
     * @param visible Whether to show the factory component (true) or invisible component (false)
     * @param id      String id to give the component. Will be passed to the factory
     * @param factory Factory to create a component
     * @return A component
     */
    public static Component componentIf( boolean visible, String id, IComponentFactory<? extends Component> factory ) {
        return ( visible ? factory.create( id ) : new InvisibleComponent( id ) );
    }

    public static Component componentIf( boolean visible, String id, Component component ) {
        return ( visible ? component : new InvisibleComponent( id ) );
    }
}
