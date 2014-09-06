/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.constants.Linkers;
import edu.colorado.phet.website.content.contribution.ContributionBrowsePage;
import edu.colorado.phet.website.content.contribution.ContributionCreatePage;
import edu.colorado.phet.website.content.contribution.ContributionGuidelinesPanel;
import edu.colorado.phet.website.content.forteachers.ActivitiesdesignPanel;
import edu.colorado.phet.website.content.forteachers.ForTeachersPanel;
import edu.colorado.phet.website.content.forteachers.TipsPanel;
import edu.colorado.phet.website.content.simulations.CategoryPage;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class TeacherIdeasPanel extends PhetPanel {

    public static final int CONTENT_WIDTH = ForTeachersPanel.CONTENT_WIDTH - PhetMenuPage.SOCIAL_ICON_PADDING;

    public TeacherIdeasPanel( String id, PageContext context ) {
        super( id, context );

        // add linkers
        add( CategoryPage.getLinker( "new" ).getLink( "new-sims-link", context, getPhetCycle() ) );
        add( ActivitiesdesignPanel.getLinker().getLink( "activities-guide-link", context, getPhetCycle() ) );

        // add linkers to table elements using WebMarkupContainer
        String tipsLink = TipsPanel.getLinker().getRawUrl( context, getPhetCycle() );
        WebMarkupContainer tipsContainer = new WebMarkupContainer( "tips-button" );
        tipsContainer.add( TipsPanel.getLinker().getLink( "tipsforusingphet-link", context, getPhetCycle() ) );
        tipsContainer.add( new AttributeModifier( "onclick", true, new Model<String>( "document.location = '" + tipsLink + "'" ) ) );
        add( tipsContainer );

        String browseActivitiesLink = ContributionBrowsePage.getLinker().getRawUrl( context, getPhetCycle() );
        WebMarkupContainer browseActivitiesContainer = new WebMarkupContainer( "browse-activities-button" );
        browseActivitiesContainer.add( TipsPanel.getLinker().getLink( "browse-activities-link", context, getPhetCycle() ) );
        browseActivitiesContainer.add( new AttributeModifier( "onclick", true, new Model<String>( "document.location = '" + browseActivitiesLink + "'" ) ) );
        add( browseActivitiesContainer );

        String submitActivityLink = ContributionCreatePage.getLinker().getRawUrl( context, getPhetCycle() );
        WebMarkupContainer submitActivityContainer = new WebMarkupContainer( "submit-activity-button" );
        submitActivityContainer.add( TipsPanel.getLinker().getLink( "submit-activity-link", context, getPhetCycle() ) );
        submitActivityContainer.add( new AttributeModifier( "onclick", true, new Model<String>( "document.location = '" + submitActivityLink + "'" ) ) );
        add( submitActivityContainer );
    }

    public static String getKey() {
        return "teacherIdeas";
    }

    public static String getUrl() {
        return "for-teachers";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }
}