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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

/**
 * Like the OrderList, however items are automatically sorted (and thus cannot be rearranged). Applicable for use with sets
 */
public class SortedCheckboxList extends PhetPanel {
    public final CheckGroup<SimOrderItem> checkGroup;
    private List<SimOrderItem> items;
    private List<SimOrderItem> allItems;
    private Label listEmpty;

    private static final Logger logger = Logger.getLogger( SortedCheckboxList.class.getName() );

    public SortedCheckboxList( String id, PageContext context, final LinkedList<SimOrderItem> items, final LinkedList<SimOrderItem> allItems ) {
        super( id, context );
        this.items = items;
        this.allItems = allItems;

        // output a markup ID so this component can be updated within ajax
//        setOutputMarkupId( true );

        sortItems( items );
        sortItems( allItems );

        Form form = new Form( "form" );
        add( form );

        Model<LinkedList<SimOrderItem>> itemsModel = new Model<LinkedList<SimOrderItem>>( allItems );

        checkGroup = new CheckGroup<SimOrderItem>( "group", new ArrayList<SimOrderItem>() );

        form.add( checkGroup );
        checkGroup.add( new CheckGroupSelector( "groupselector" ) );
        ListView<SimOrderItem> sims = new ListView<SimOrderItem>( "sims", allItems ) {
            @Override
            protected void populateItem( final ListItem<SimOrderItem> listItem ) {
                final SimOrderItem item = listItem.getModelObject();
                listItem.add( new Check<SimOrderItem>( "checkbox", listItem.getModel() ) );
                listItem.add( item.getDisplayComponent( "name" ) );
            }
        };

        sims.setReuseItems( true );
        checkGroup.add( sims );
    }

    private void updateEmpty() {
        listEmpty.setVisible( items.isEmpty() );
    }

    private void sortItems( List<SimOrderItem> list ) {
        Collections.sort( list, new Comparator<SimOrderItem>() {
            public int compare( SimOrderItem a, SimOrderItem b ) {
                return a.compareTo( b, getLocale() );
            }
        } );
    }

    public CheckGroup<SimOrderItem> getFormComponent() {
        return checkGroup;
    }
}
