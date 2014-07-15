/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.cache;

import org.apache.log4j.Logger;
import org.hibernate.Cache;
import org.hibernate.SessionFactory;

import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.translation.entities.TranslationEntity;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

public class CacheUtils {

    private static final Logger logger = Logger.getLogger( CacheUtils.class.getName() );

    /**
     * Clears all of the caches
     */
    public static void clearAllCaches() {
        clearSecondLevelCache();
        clearTranslatedStringCache();
        clearTranslationEntityCache();
        clearPanelCache();
        clearSimulationCache();
        clearImageCache();
        clearInstallerCache();
    }

    private static void clearImageCache() {
        ImageCache.invalidate();
    }

    /**
     * Clears the Hibernate second-level cache. This cache is where data pulled or sent to the database is cached.
     */
    public static void clearSecondLevelCache() {
        logger.info( "clearing second level cache" );
        SessionFactory factory = HibernateUtils.getInstance();

        Cache cache = factory.getCache();
        //cache.evictQueryRegions();
        cache.evictEntityRegions();
        cache.evictCollectionRegions();

        try {
            cache.evictDefaultQueryRegion();
            cache.evictQueryRegions();
        } catch( RuntimeException e ) {
            logger.error( "Cache clear error", e );
        }

        // deprecated, but cache.evictQueryRegions is throwing exceptions
        factory.evictQueries();
    }

    /**
     * Clears the cache where translated strings are held.
     */
    public static void clearTranslatedStringCache() {
        logger.info( "clearing translated string cache" );
        PhetLocalizer.get().clearCache();
    }

    /**
     * Clears the cache of what string keys are translatable in what entities.
     * Could need refreshing if we are adding in strings!
     */
    public static void clearTranslationEntityCache() {
        TranslationEntity.clearCache();
    }

    /**
     * Clears the panel cache, which is composed of HTML copies of parts of pages
     */
    public static void clearPanelCache() {
        logger.info( "clearing the panel cache" );
        PanelCache.get().clear();
    }

    /**
     * Clears the simulation cache, where the preferred order of best simulations for a locale is cached.
     */
    public static void clearSimulationCache() {
        logger.info( "clearing the simulation cache" );
        SimulationCache.invalidate();
    }

    /**
     * Re-read information about the installer state
     */
    public static void clearInstallerCache() {
        InstallerCache.invalidate();
    }
}
