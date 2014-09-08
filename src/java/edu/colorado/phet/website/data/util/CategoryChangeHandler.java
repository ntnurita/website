/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.data.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.colorado.phet.website.data.Category;
import edu.colorado.phet.website.data.Simulation;

public class CategoryChangeHandler {
    private static Set<Listener> listeners = new HashSet<Listener>();

    public static synchronized int getListenerCount() {
        return listeners.size();
    }

    public static synchronized String getListenerReport() {
        Map<String, Integer> histogram = new HashMap<String, Integer>();

            for ( Listener listener : listeners ) {
                String listenerClassName = listener.getClass().getName();
                if ( histogram.containsKey( listenerClassName ) ) {
                    histogram.put( listenerClassName, histogram.get( listenerClassName ) + 1 );
                }
                else {
                    histogram.put( listenerClassName, 1 );
                }
            }


        StringBuilder builder = new StringBuilder(  );

        builder.append( "CategoryChangeHandler listeners:<br>" );

        for ( String key : histogram.keySet() ) {
            builder.append( key ).append( ": " ).append( histogram.get( key ) );
        }

        return builder.toString();
    }

    public static synchronized void addListener( Listener listener ) {
        listeners.add( listener );
    }

    public static synchronized void removeListener( Listener listener ) {
        listeners.remove( listener );
    }

    public static synchronized void notifySimulationChange( Category category, Simulation simulation ) {
        for ( Listener listener : listeners.toArray( new Listener[listeners.size()] ) ) {
            listener.categorySimulationChanged( category, simulation );
        }
    }

    public static synchronized void notifyAdded( Category category ) {
        for ( Listener listener : listeners.toArray( new Listener[listeners.size()] ) ) {
            listener.categoryAdded( category );
        }
    }

    public static synchronized void notifyRemoved( Category category ) {
        for ( Listener listener : listeners.toArray( new Listener[listeners.size()] ) ) {
            listener.categoryRemoved( category );
        }
    }

    public static synchronized void notifyChildrenReordered( Category category ) {
        for ( Listener listener : listeners.toArray( new Listener[listeners.size()] ) ) {
            listener.categoryChildrenReordered( category );
        }
    }

    public interface Listener {
        public void categorySimulationChanged( Category category, Simulation simulation );

        public void categoryAdded( Category category );

        public void categoryRemoved( Category category );

        public void categoryChildrenReordered( Category category );
    }

}
