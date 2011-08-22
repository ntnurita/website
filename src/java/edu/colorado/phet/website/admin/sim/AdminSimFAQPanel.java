// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.admin.sim;

import org.apache.log4j.Logger;

import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

public class AdminSimFAQPanel extends PhetPanel {

    private static final Logger logger = Logger.getLogger( AdminSimFAQPanel.class.getName() );

    public AdminSimFAQPanel( String id, PageContext context, final Simulation simulation ) {
        super( id, context );

    }
}