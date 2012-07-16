// Copyright 2002-2012, University of Colorado

package edu.colorado.phet.website.admin;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.Link;

import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.cache.CacheUtils;
import edu.colorado.phet.website.metadata.MetadataUtils;
import edu.colorado.phet.website.util.SearchUtils;

/**
 * Useful metadata links, and additional links that will trigger metadata publishing or output
 */
public class AdminMetadataPage extends AdminPage {

    private static final Logger logger = Logger.getLogger( AdminMetadataPage.class.getName() );

    public AdminMetadataPage( PageParameters parameters ) {
        super( parameters );

        add( new Link( "write-master-format-metadata" ) {
            public void onClick() {
                MetadataUtils.writeSimulationsFromSession();
            }
        } );

        add( new Link( "learning-registry-full-publish" ) {
            public void onClick() {
                MetadataUtils.publishLearningRegistryAllSimulationsFromSession();
            }
        } );
    }

}