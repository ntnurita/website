/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * Storage for cached panel versions. Should be a singleton, and is thread-safe with a hidden static lock so deadlock
 * shouldn't be an accidental risk.
 * <p/>
 * TODO: store metadata in the cache like timestamp created / expiring / memory footprint / value in previous render and setup time
 */
public class PanelCache {

    /**
     * Singleton instance
     */
    private static final PanelCache instance = new PanelCache();

    /**
     * Stores cached entries. Should be an entry, entry pair where the entries are identical, so that we can look up
     * corresponding entries with filled in cached values.
     */
    private ConcurrentHashMap<IPanelCacheEntry, CacheItem> cache = new ConcurrentHashMap<IPanelCacheEntry, CacheItem>();

    private static final Logger logger = Logger.getLogger( PanelCache.class.getName() );

    private PanelCache() {
        // singleton
    }

    /**
     * @return The singleton instance of the panel cache
     */
    public static PanelCache get() {
        return instance;
    }

    /**
     * @param entry The entry
     * @return Whether the cache contains a cached version for this entry
     */
    public boolean contains( IPanelCacheEntry entry ) {
        return cache.containsKey( entry );
    }

    /**
     * @param entry The entry
     * @return Returns a cached version for this entry that can have fabricate() effectively called upon it, or null if
     *         there is no such component
     */
    public IPanelCacheEntry getMatching( IPanelCacheEntry entry ) {
        CacheItem item = cache.get( entry );
        return item == null ? null : item.getEntry();
    }

    /**
     * Adds an entry if it does not already exist in the cache.
     * Relies on all of the entries' equals() and hashcode() to be correct
     *
     * @param entry The entry to add
     * @return Whether it was added. Will be false if it is already in the cache
     */
    public boolean addIfMissing( IPanelCacheEntry entry ) {
        boolean adding;
        adding = !cache.containsKey( entry );
        if ( adding ) {
            cache.put( entry, new CacheItem( entry ) );
            entry.onEnterCache( this );
        }
        if ( adding ) {
            logger.debug( "added to cache: " + entry );
        }
        return adding;
    }

    /**
     * Removes an entry from the cache
     *
     * @param entry The entry to remove
     */
    public void remove( IPanelCacheEntry entry ) {
        logger.debug( "attempting to remove from cache: " + entry );
        cache.remove( entry );
        entry.onExitCache();
    }

    /**
     * A quick cache dump
     */
    public Set<CacheItem> getEntries() {
        Set<CacheItem> ret = new HashSet<CacheItem>();
        ret.addAll( cache.values() );
        return ret;
    }

    /**
     * Clear all of this cache
     */
    public void clear() {
        cache.clear();
    }

}
