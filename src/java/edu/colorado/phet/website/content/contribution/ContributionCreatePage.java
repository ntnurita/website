/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.contribution;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;

import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.constants.Linkers;
import edu.colorado.phet.website.data.contribution.Contribution;
import edu.colorado.phet.website.panels.contribution.ContributionEditPanel;
import edu.colorado.phet.website.templates.PhetRegularPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.AuthenticatedLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class ContributionCreatePage extends PhetRegularPage {

    private static final Logger logger = Logger.getLogger( ContributionCreatePage.class.getName() );

    public ContributionCreatePage( PageParameters parameters ) {
        super( parameters );

        // these parameters are used to pre-populate the sim that the contribution should reference
        String simName = parameters.getString( "simulation" );
        String isHTMLString = parameters.getString( "isHTML" );
        boolean isHTML = Boolean.parseBoolean( isHTMLString );

        setContentWidth( 1120 );

        initializeLocation( getNavMenu().getLocationByKey( "teacherIdeas.submit" ) );

        verifySignedIn();

        setTitle( getLocalizer().getString( "contribution.create.pageTitle", this ) );

        add( new ContributionEditPanel( "contribution-edit-panel", getPageContext(), new Contribution(), simName, isHTML ) );

        add( new LocalizedText( "check-guidelines", "contribution.create.checkGuidelines", new Object[] {
                ContributionGuidelinesPanel.getLinker().getHref( getPageContext(), getPhetCycle() ),
                Linkers.CONTRIBUTION_GUIDELINES_PDF.getHref( getPageContext(), getPhetCycle() )
        } ) );

        hideSocialBookmarkButtons();

    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        mapper.addMap( "^teaching-resources/submit-activity$", ContributionCreatePage.class );
    }

    public static RawLinkable getLinker() {
        return new AuthenticatedLinker() {
            public String getSubUrl( PageContext context ) {
                return "teaching-resources/submit-activity";
            }
        };
    }

    public static RawLinkable getLinker( final String query ) {
        return new AuthenticatedLinker() {
            public String getSubUrl( PageContext context ) {
                return "teaching-resources/submit-activity?" + query;
            }
        };
    }

}
