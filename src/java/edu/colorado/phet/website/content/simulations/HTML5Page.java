// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.content.simulations;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.model.StringResourceModel;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.content.about.AboutLicensingPanel;
import edu.colorado.phet.website.data.Category;
import edu.colorado.phet.website.menu.NavLocation;
import edu.colorado.phet.website.panels.NavBreadCrumbs;
import edu.colorado.phet.website.panels.TranslationLinksPanel;
import edu.colorado.phet.website.templates.PhetPage;
import edu.colorado.phet.website.templates.PhetRegularPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class HTML5Page extends PhetPage {

    private static final Logger logger = Logger.getLogger( HTML5Page.class.getName() );

    public HTML5Page( final PageParameters parameters ) {
        super( parameters );

        add( new HTML5Panel( "html-panel", getPageContext() ) );

        NavLocation location = getNavMenu().getLocationByKey( "html" );

//        initializeLocation( location );
        add( new NavBreadCrumbs( "breadcrumbs", getPageContext(), location ) );

        setTitle( StringUtils.messageFormat( getLocalizer().getString( "simulationDisplay.title", this ), new Object[] {
                getPhetLocalizer().getString( location.getLocalizationKey(), this )
        } ) );
//        addTitle( new StringResourceModel( "simulationDisplay.title", this, null, new Object[]{new StringResourceModel( location.getLocalizationKey(), this, null )} ) );

        /*---------------------------------------------------------------------------*
        * Footer
        *----------------------------------------------------------------------------*/

        // TODO: remove duplication from this and PhetMenuPage
        if ( !getMyLocale().equals( WebsiteConstants.ENGLISH ) && getPhetLocalizer().getString( "translation.credits", this ).length() > 0 ) {
            add( new LocalizedText( "translation-credits", "translation.credits" ) );
        }
        else {
            add( new InvisibleComponent( "translation-credits" ) );
        }

        if ( DistributionHandler.displayTranslationLinksPanel( (PhetRequestCycle) getRequestCycle() ) ) {
            add( new TranslationLinksPanel( "translation-links", getPageContext() ) );
        }
        else {
            add( new InvisibleComponent( "translation-links" ) );
        }
        //add( HeaderContributor.forCss( CSS.MENU_PAGE ) );

        addCopyright();

        this.getPhetCycle().setMinutesToCache( 15 );
    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        // WARNING: don't change without also changing the old URL redirection
        mapper.addMap( "^simulations/category/html$", HTML5Page.class );
    }

    public static RawLinkable getLinker() {
        // WARNING: don't change without also changing the old URL redirection
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return "simulations/category/html";
            }
        };
    }

}