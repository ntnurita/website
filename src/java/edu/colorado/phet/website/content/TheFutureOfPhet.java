// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.content;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;

import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.constants.Linkers;
import edu.colorado.phet.website.content.about.AboutNewsPanel;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.templates.PhetPage;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.PhetUrlMapper;

public class TheFutureOfPhet extends PhetPage {

    public TheFutureOfPhet( PageParameters parameters ) {
        super( parameters );

        setTitle( "The Future of PhET - August 15th, 2013" );

        // add linkers
        add( AboutNewsPanel.getLinker().getLink( "about-news-link", getPageContext(), PhetRequestCycle.get() ) );
    }
}