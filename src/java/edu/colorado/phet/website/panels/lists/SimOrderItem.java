/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.panels.lists;

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

import edu.colorado.phet.website.data.Simulation;

/**
 * Used to display a simulation in a list where simulations can be added or removed. Sortable by localized title
 */
public class SimOrderItem implements SortableListItem, Serializable {
    private Simulation simulation;
    private String title;

    /**
     * @param simulation The simulation to represent
     * @param title      The localized title to use
     */
    public SimOrderItem( Simulation simulation, String title ) {
        this.simulation = simulation;
        this.title = title;
    }

    public String getDisplayValue() {
        return title;
    }

    public Component getDisplayComponent( String id ) {
        return new Label( id, title );
    }

    public int getId() {
        return simulation.getId();
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public int compareTo( SortableListItem item, Locale locale ) {

        // append an extra "z" to the titles of legacy sims so that they appear below the HTML5 sims in the dropdown menu
        // see https://github.com/phetsims/website/issues/99
        String legacyCompareThis = ( !simulation.isHTML() ) ? "z" : "";
        String legacyCompareItem = ( !( (SimOrderItem) item ).getSimulation().isHTML() ) ? "z" : "";
        return Collator.getInstance( locale ).compare( getDisplayValue() + legacyCompareThis, item.getDisplayValue() + legacyCompareItem );
    }
}
