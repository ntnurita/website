/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.services;

import it.sauronsoftware.cron4j.Scheduler;

import org.apache.log4j.Logger;

import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.cache.CacheUtils;
import edu.colorado.phet.website.metadata.MetadataUtils;
import edu.colorado.phet.website.newsletter.InitialSubscribePanel;
import edu.colorado.phet.website.notification.NotificationHandler;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.util.SearchUtils;

/**
 * Consolidates schedulers so we can run background tasks. Single initialization and destruction for schedulers.
 */
public class SchedulerService {

    private static Scheduler masterScheduler;

    private static final Logger logger = Logger.getLogger( NotificationHandler.class.getName() );

    public static synchronized void initialize( final PhetWicketApplication app, final PhetLocalizer localizer ) {
        masterScheduler = new Scheduler();

        // re-index our on-site search every night
        masterScheduler.schedule( "15 23 * * *", new Runnable() {
            public void run() {
                logger.info( "Running SearchUtils.reindex" );
                SearchUtils.reindex( app, localizer );
            }
        } );

        // reset our security limiter in the middle of every hour
        masterScheduler.schedule( "32 * * * *", new Runnable() {
            public void run() {
                logger.info( "Running InitialSubscribePanel.resetSecurity" );
                InitialSubscribePanel.resetSecurity();
            }
        } );

        // clear our string cache 20 minutes into every 3 hours
        masterScheduler.schedule( "20 0,3,6,9,12,15,18,21 * * *", new Runnable() {
            public void run() {
                logger.info( "Running CacheUtils.clearTranslatedStringCache()" );
                CacheUtils.clearTranslatedStringCache();
            }
        } );

        // clear our panel cache 25 minutes into every 3 hours
        masterScheduler.schedule( "25 1,4,7,10,13,16,19,22 * * *", new Runnable() {
            public void run() {
                logger.info( "Running CacheUtils.clearPanelCache()" );
                CacheUtils.clearPanelCache();
            }
        } );

        // clear our second-level cache 45 minutes into every 3 hours
        masterScheduler.schedule( "45 2,5,8,11,14,17,20,23 * * *", new Runnable() {
            public void run() {
                logger.info( "Running CacheUtils.clearSecondLevelCache()" );
                CacheUtils.clearSecondLevelCache();
            }
        } );

        // clear all of our other caches 12 minutes into every 6 hours
        masterScheduler.schedule( "12 4,10,16,22 * * *", new Runnable() {
            public void run() {
                logger.info( "Running CacheUtils.clear{TranslationEntity|Simulation|Image|Installer}Cache" );
                CacheUtils.clearTranslationEntityCache();
                CacheUtils.clearSimulationCache();
                CacheUtils.clearImageCache();
                CacheUtils.clearInstallerCache();
            }
        } );

        // update metadata on Saturday nights
        masterScheduler.schedule( "30 22 * * 6", new Runnable() {
            public void run() {
                MetadataUtils.writeSimulationsWithoutSession();
            }
        } );

        if ( app.isProductionServer() ) {
            // send out end-of-week notifications
            masterScheduler.schedule( "59 23 * * fri", new Runnable() {
                public void run() {
                    logger.info( "Running NotificationHandler.sendNotifications" );
                    NotificationHandler.sendNotifications();
                }
            } );
        }
        else {
            logger.info( "Not starting NotificationHandler, hostname: " + app.getWebsiteProperties().getWebHostname() );
        }

        masterScheduler.start();
    }

    public static synchronized void destroy() {
        if ( masterScheduler != null ) {
            masterScheduler.stop();
        }
    }

}
