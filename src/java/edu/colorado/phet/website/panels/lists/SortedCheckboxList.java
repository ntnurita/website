/*
 * Copyright 2015, University of Colorado
 */

package edu.colorado.phet.website.panels.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

/**
 * Like the OrderList, however items are automatically sorted (and thus cannot be rearranged). Applicable for use with sets
 */
public class SortedCheckboxList<Item extends SortableListItem> extends PhetPanel {
    public final CheckGroup<Item> checkGroup;
    private List<Item> items;
    private List<Item> allItems;

    private static final Logger logger = Logger.getLogger( SortedCheckboxList.class.getName() );

    public SortedCheckboxList( String id, PageContext context, final LinkedList<Item> items, final LinkedList<Item> allItems, boolean groupSelector ) {
        super( id, context );
        this.items = items;
        this.allItems = allItems;

        sortItems( items );
        sortItems( allItems );

        checkGroup = new CheckGroup<Item>( "group", new ArrayList<Item>() );

        Form form = new Form( "form" )
        {
            @Override
            protected void onSubmit()
            {
                info( "selected items(s): " + checkGroup.getDefaultModelObjectAsString() );
            }
        };
        add( form );

        form.add( checkGroup );
        if ( groupSelector ) {
            checkGroup.add( new CheckGroupSelector( "groupselector" ) );
        }
        else {
            checkGroup.add( new InvisibleComponent( "groupselector" ) );
        }
        ListView<Item> sims = new ListView<Item>( "sims", allItems ) {
            @Override
            protected void populateItem( final ListItem<Item> listItem ) {
                final Item item = listItem.getModelObject();
                listItem.add( new Check<Item>( "checkbox", listItem.getModel() ) );
                listItem.add( item.getDisplayComponent( "name" ) );
            }
        };

        sims.setReuseItems( true );
        checkGroup.add( sims );
    }

    private void sortItems( List<Item> list ) {
        Collections.sort( list, new Comparator<Item>() {
            public int compare( Item a, Item b ) {
                return a.compareTo( b, getLocale() );
            }
        } );
    }

    public CheckGroup<Item> getFormComponent() {
        return checkGroup;
    }
}
