// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.content;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.content.about.AboutLicensingPanel;
import edu.colorado.phet.website.content.simulations.HTML5Panel;
import edu.colorado.phet.website.menu.NavLocation;
import edu.colorado.phet.website.panels.NavBreadCrumbs;
import edu.colorado.phet.website.panels.TranslationLinksPanel;
import edu.colorado.phet.website.templates.PhetPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class TeachWithPhetDemoPage extends WebPage {

    private static final Logger logger = Logger.getLogger( TeachWithPhetDemoPage.class.getName() );

    public TeachWithPhetDemoPage( final PageParameters parameters ) {
        super( parameters );
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        // WARNING: don't change without also changing the old URL redirection
        mapper.addMap( "^teachwithphet$", TeachWithPhetDemoPage.class );
    }

    public static RawLinkable getLinker() {
        // WARNING: don't change without also changing the old URL redirection
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return "teachwithphet";
            }
        };
    }

}