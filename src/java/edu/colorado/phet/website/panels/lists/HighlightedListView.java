package edu.colorado.phet.website.panels.lists;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import edu.colorado.phet.website.util.ClassAppender;

public class HighlightedListView extends ListView {
    public HighlightedListView( String id ) {
        super( id );
    }

    public HighlightedListView( String id, IModel model ) {
        super( id, model );
    }

    public HighlightedListView( String id, List list ) {
        super( id, list );
    }

    protected void populateItem( ListItem item ) {
        if ( item.getIndex() % 2 == 0 ) {
            item.add( new ClassAppender( "list-highlight-background" ) );
        }
    }
}
