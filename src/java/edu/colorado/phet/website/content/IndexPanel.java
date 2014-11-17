/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content;

import java.util.Calendar;
import java.util.LinkedList;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.cache.EventDependency;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.components.RawLink;
import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.constants.SocialBookmarkService;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.content.about.AboutContactPanel;
import edu.colorado.phet.website.content.about.AboutLicensingPanel;
import edu.colorado.phet.website.content.about.AboutMainPanel;
import edu.colorado.phet.website.content.about.AboutNewsPanel;
import edu.colorado.phet.website.content.about.AboutSponsorsPanel;
import edu.colorado.phet.website.content.contribution.ContributionBrowsePage;
import edu.colorado.phet.website.content.contribution.ContributionCreatePage;
import edu.colorado.phet.website.content.contribution.ContributionPage;
import edu.colorado.phet.website.content.forteachers.TipsPanel;
import edu.colorado.phet.website.content.getphet.FullInstallPanel;
import edu.colorado.phet.website.content.getphet.OneAtATimePanel;
import edu.colorado.phet.website.content.getphet.RunOurSimulationsPanel;
import edu.colorado.phet.website.content.media.TechAwardPage;
import edu.colorado.phet.website.content.simulations.CategoryPage;
import edu.colorado.phet.website.content.troubleshooting.GeneralFAQPanel;
import edu.colorado.phet.website.content.troubleshooting.TroubleshootingMainPanel;
import edu.colorado.phet.website.content.workshops.WorkshopsPanel;	
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Project;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.data.util.HibernateEventListener;
import edu.colorado.phet.website.data.util.IChangeListener;
import edu.colorado.phet.website.newsletter.InitialSubscribePage;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.panels.RotatorFallbackPanel;
import edu.colorado.phet.website.panels.RotatorPanel;
import edu.colorado.phet.website.panels.SocialBookmarkPanel;
import edu.colorado.phet.website.panels.SurveySplashPanel;
import edu.colorado.phet.website.panels.TranslationLinksPanel;
import edu.colorado.phet.website.panels.sponsor.FeaturedSponsorPanel;
import edu.colorado.phet.website.panels.sponsor.Sponsor;
import edu.colorado.phet.website.translation.TranslationMainPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.wicket.IComponentFactory;
import edu.colorado.phet.website.util.wicket.WicketUtils;

/**
 * The panel which represents the main content portion of the home (index) page
 */
public class IndexPanel extends PhetPanel {

    public static final String PLAY_SIMS_ID = "play-sims";

    public IndexPanel( String id, final PageContext context ) {
        super( id, context );

        // tech award logo
        Link techAwardTitleLink = TechAwardPage.getLinker().getLink( "techAwardLink", context, getPhetCycle() );
        techAwardTitleLink.add( new StaticImage( "award-logo", Images.LOGO_TECH_AWARDS_SMALL, null ) );
        add( techAwardTitleLink );

        // add social icons
        add( WicketUtils.componentIf( true, "social-bookmark-panel", new IComponentFactory<Component>() {
            public Component create( String id ) {
                return new SocialBookmarkPanel( "social-bookmark-panel", context, "", "home.title" );
            }
        } ) );

        Link playSimsLink = CategoryPage.getDefaultLinker().getLink( "play-sims-link", context, getPhetCycle() );
        playSimsLink.add( new StaticImage( "phet-airplane", Images.PHET_AIRPLANE, null ) );
        addWithId( playSimsLink , PLAY_SIMS_ID );

        Link runOurSimsLink = RunOurSimulationsPanel.getLinker().getLink( "run-our-sims-link", context, getPhetCycle() );
        runOurSimsLink.add( new LocalizedText( "run-our-sims-label", getPhetCycle().isOfflineInstaller() ? "home.help" : "home.runOurSims" ) );
        add( runOurSimsLink );

        add( WicketUtils.componentIf( !getPhetCycle().isOfflineInstaller(), "on-line-link", new IComponentFactory<Component>() {
            public Component create( String id ) {
                return CategoryPage.getDefaultLinker().getLink( id, context, getPhetCycle() );
            }
        } ) );
        add( WicketUtils.componentIf( !getPhetCycle().isOfflineInstaller(), "full-install-link", new IComponentFactory<Component>() {
            public Component create( String id ) {
                return FullInstallPanel.getLinker().getLink( id, context, getPhetCycle() );
            }
        } ) );
        add( WicketUtils.componentIf( !getPhetCycle().isOfflineInstaller(), "one-at-a-time-link", new IComponentFactory<Component>() {
            public Component create( String id ) {
                return OneAtATimePanel.getLinker().getLink( id, context, getPhetCycle() );
            }
        } ) );

        add( TroubleshootingMainPanel.getLinker().getLink( "troubleshooting-link", context, getPhetCycle() ) );
        add( GeneralFAQPanel.getLinker().getLink( "faqs-link", context, getPhetCycle() ) );

        add( WorkshopsPanel.getLinker().getLink( "workshops-link", context, getPhetCycle() ) );

        add( DonatePanel.getLinker().getLink( "support-phet-link", context, getPhetCycle() ) );
        add( TranslationUtilityPanel.getLinker().getLink( "translate-sims-link", context, getPhetCycle() ) );
       

        add( AboutMainPanel.getLinker().getLink( "about-general", context, getPhetCycle() ) );
        add( AboutMainPanel.getLinker().getLink( "about-phet", context, getPhetCycle() ) );
        add( AboutNewsPanel.getLinker().getLink( "about-news", context, getPhetCycle() ) );
        add( AboutContactPanel.getLinker().getLink( "about-contact", context, getPhetCycle() ) );
        add( new LocalizedText( "other-sponsors", "home.about.alongWithOurSponsors", new Object[] {
                AboutSponsorsPanel.getLinker().getHref( context, getPhetCycle() )
        } ) );
        add( AboutSponsorsPanel.getLinker().getLink( "sponsors-general", context, getPhetCycle() ) );

        add( new FeaturedSponsorPanel( "featured-sponsor-panel", Sponsor.chooseRandomHomeSponsor(), context ) );

        if ( getPhetCycle().isInstaller() ) {
            add( new WebMarkupContainer( "featured-sponsor-installer-js" ) );
        }
        else {
            add( new InvisibleComponent( "featured-sponsor-installer-js" ) );
        }

        if ( context.getLocale().equals( WebsiteConstants.ENGLISH ) && DistributionHandler.displayTranslationEditLink( (PhetRequestCycle) getRequestCycle() ) ) {
            add( TranslationMainPage.getLinker().getLink( "test-translation", context, getPhetCycle() ) );
        }
        else {
            add( new InvisibleComponent( "test-translation" ) );
        }

        if ( !getMyLocale().equals( WebsiteConstants.ENGLISH ) && getPhetLocalizer().getString( "translation.credits", this ).length() > 0 ) {
            add( new LocalizedText( "translation-credits", "translation.credits" ) );
        }
        else {
            add( new InvisibleComponent( "translation-credits" ) );
        }

        if ( DistributionHandler.displayTranslationLinksPanel( (PhetRequestCycle) getRequestCycle() ) ) {
            add( new TranslationLinksPanel( "translation-links", context ) );
        }
        else {
            add( new InvisibleComponent( "translation-links" ) );
        }

        if ( DistributionHandler.redirectActivities( (PhetRequestCycle) getRequestCycle() ) ) {
            add( new RawLink( "activities-link", "http://phet.colorado.edu/teacher_ideas/index.php" ) );
            add( new RawLink( "browse-activities-link", "http://phet.colorado.edu/teacher_ideas/browse.php" ) );
            add( new RawLink( "submit-activity-link", "http://phet.colorado.edu/en/for-teachers/submit-activity" ) );
        }
        else {
            add( TeacherIdeasPanel.getLinker().getLink( "activities-link", context, getPhetCycle() ) );
            if ( getPhetCycle().isOfflineInstaller() ) {
                add( new InvisibleComponent( "browse-activities-link" ) );
            }
            else {
                add( ContributionBrowsePage.getLinker().getLink( "browse-activities-link", context, getPhetCycle() ) );
            }

            add( TipsPanel.getLinker().getLink( "tipsforusingphet-link", context, getPhetCycle() ) );
            add( ContributionCreatePage.getLinker().getLink( "submit-activity-link", context, getPhetCycle() ) );
        }

//        if ( DistributionHandler.showRotatorFallback( getPhetCycle() ) ) {
//            add( new RotatorFallbackPanel( "rotator-panel", context ) );
//        }
//        else {
//            // show the survey if we would show the rotator AND we are in an English translation (since it's English-only text)
////            if ( !getPhetCycle().isInstaller() && context.getLocale().equals( WebsiteConstants.ENGLISH ) ) {
////                add( new SurveySplashPanel( "rotator-panel", context ) );
////            }
////            else {
//                add( new RotatorPanel( "rotator-panel", context ) );
////            }
//        }

        /*---------------------------------------------------------------------------*
        * sponsors
        *----------------------------------------------------------------------------*/
//        add( new StaticImage( "hewlett-logo", Images.LOGO_HEWLETT_FRONT_PAGE, null ) );
//        add( new StaticImage( "nsf-logo", Images.LOGO_NSF_FRONT_PAGE, null ) );
        add( new StaticImage( "odf-logo", Images.LOGO_ODF_COMBINED_SMALL, null ) );
        add( new StaticImage( "moore-logo", Images.LOGO_MOORE, null ) );

        // I tried resizing these in photoshop and it looked horrible
        // Somehow the browser does a much better job
        add( new WebMarkupContainer( "nsf-logo" ) {{
            add( new AttributeModifier( "width", true, new Model<String>( "60px" ) ) );
            add( new AttributeModifier( "src", true, new Model<String>( "/images/support/nsf1.gif" ) ) );
        }} );

        add( new WebMarkupContainer( "hewlett-logo" ) {{
            add( new AttributeModifier( "width", true, new Model<String>( "132px" ) ) );
            add( new AttributeModifier( "src", true, new Model<String>( "/images/support/hewlett-logo-full-res.gif" ) ) );
        }} );

        /*---------------------------------------------------------------------------*
        * footer social icons
        *----------------------------------------------------------------------------*/
        ListView socialFooter = new ListView<SocialBookmarkService>( "social-footer-list", SocialBookmarkService.HOMEPAGE_SERVICES ) {
            private boolean isFirst = true; // the first link should have no left separator like the others

            protected void populateItem( ListItem<SocialBookmarkService> item ) {
                final SocialBookmarkService mark = item.getModelObject();
                Link link;
                if ( mark.getName().equals( "newsletter" ) ) {
                    link = InitialSubscribePage.getLinker().getLink( "link", context, getPhetCycle() );
                } else {
                    link = mark.getLinker( "", "home.title" ).getLink( "link", context, getPhetCycle() );
                }
                link.add( new AttributeModifier( "title", true, new ResourceModel( mark.getHomePageTooltipLocalizationKey() ) ) ); // tooltip
                if ( isFirst ) {
                    link.add( new AttributeModifier( "class", true, new Model<String>( "footer-link first-footer-link" ) ) );
                    isFirst = false;
                }
                else {
                    link.add( new AttributeModifier( "class", true, new Model<String>( "footer-link" ) ) );
                }
                item.add( link );
                link.add( new LocalizedText( "label", mark.getFooterLabel() ) );
                link.add( new WebMarkupContainer( "icon" ) {{
                    add( new AttributeModifier( "class", true, new Model<String>( "footer-icon" ) ) );
                    add( new AttributeModifier( "src", true, new Model<String>( mark.getIconPath() ) ) );
                }} );
            }
        };
        add( socialFooter );
        add( new WebMarkupContainer( "copyright" ) {{
            int year = Calendar.getInstance().get( Calendar.YEAR );
            add ( new Label( "copyright-label", "© " + year + " University of Colorado. ") );
            add( AboutLicensingPanel.getLinker().getLink( "some-rights-link", context, getPhetCycle() ) );
        }} );

        addDependency( new EventDependency() {
            private IChangeListener stringListener;

            @Override
            protected void addListeners() {
                stringListener = createTranslationChangeInvalidator( context.getLocale() );
                HibernateEventListener.addListener( Project.class, getAnyChangeInvalidator() );
                HibernateEventListener.addListener( TranslatedString.class, stringListener );
                HibernateEventListener.addListener( Simulation.class, getAnyChangeInvalidator() );
                HibernateEventListener.addListener( LocalizedSimulation.class, getAnyChangeInvalidator() );
            }

            @Override
            protected void removeListeners() {
                HibernateEventListener.removeListener( Project.class, getAnyChangeInvalidator() );
                HibernateEventListener.removeListener( TranslatedString.class, stringListener );
                HibernateEventListener.removeListener( Simulation.class, getAnyChangeInvalidator() );
                HibernateEventListener.removeListener( LocalizedSimulation.class, getAnyChangeInvalidator() );
            }
        } );
    }

}
