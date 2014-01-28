// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.services;

import org.apache.wicket.markup.html.WebPage;

import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.components.RawBodyLabel;
import edu.colorado.phet.website.util.PhetRequestCycle;

/**
 * Checked by Varnish/Nagios/etc. to determine health
 */
public class HealthCheck extends WebPage {
    public HealthCheck() {
        add( new RawBodyLabel( "text", "Health check is OK" ) );
    }

    @Override
    protected void configureResponse() {
        super.configureResponse();
        getWebRequestCycle().getWebResponse().setContentType( "text/plain" );
    }
}
