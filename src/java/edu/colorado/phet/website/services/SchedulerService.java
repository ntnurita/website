/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.services;

import it.sauronsoftware.cron4j.Scheduler;

import org.apache.log4j.Logger;

import edu.colorado.phet.website.PhetWicketApplication;
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
