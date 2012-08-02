// Copyright 2002-2012, University of Colorado

package edu.colorado.phet.website.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import edu.colorado.phet.common.phetcommon.util.FunctionalUtils;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.content.simulations.SimulationPage;
import edu.colorado.phet.website.data.Keyword;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;

import static edu.colorado.phet.common.phetcommon.util.FunctionalUtils.concat;
import static edu.colorado.phet.common.phetcommon.util.FunctionalUtils.mkString;

/**
 * Displays metadata related to Facebook's Open Graph, keywords, LRMI, etc. that is placed directly into the HTML
 */
public class SimulationPageMetadataPanel extends PhetPanel {
    public SimulationPageMetadataPanel( String id, final PageContext context, final LocalizedSimulation simulation ) {
        super( id, context );

        // don't show the div around the wrapper for this panel. we want the tags right in the head tag with no other divs, etc.
        setRenderBodyOnly( true );

        final List<String> keywords = new ArrayList<String>();

        // make sure everything is loaded properly in this transaction, so we can get what we need out properly
        HibernateUtils.ensureTransaction( getHibernateSession(), new VoidTask() {
            public void run( Session session ) {
                LocalizedSimulation lsim = (LocalizedSimulation) session.load( LocalizedSimulation.class, simulation.getId() );

                // fill the keywords list
                for ( Object o : lsim.getSimulation().getKeywords() ) {
                    Keyword keyword = (Keyword) o;
                    keywords.add( getPhetLocalizer().getBestStringWithinTransaction( session, keyword.getLocalizationKey(), context.getLocale() ) );
                }
            }
        } );

        List<MetaElement> openGraphTags = Arrays.asList(
                new MetaElement( "og:title", simulation.getTitle() ),
                new MetaElement( "og:type", "phet:simulation" ),
                new MetaElement( "og:url", StringUtils.makeUrlAbsolute( SimulationPage.getLinker( simulation ).getDefaultRawUrl() ) ),
                new MetaElement( "og:image", "http://" + WebsiteConstants.WEB_SERVER + simulation.getSimulation().getThumbnailUrl() ),
                new MetaElement( "og:site_name", "PhET" ),
                new MetaElement( "og:description", getLocalizer().getString( simulation.getSimulation().getDescriptionKey(), this ) )
        );

        List<MetaElement> standardMetaTags = Arrays.asList(
                new MetaElement( "keywords", mkString( keywords, ", " ) )
        );

        ListView<MetaElement> metaTagList = new ListView<MetaElement>( "meta-tags", concat( openGraphTags, standardMetaTags ) ) {
            @Override protected void populateItem( ListItem<MetaElement> item ) {
                item.add( new AttributeModifier( "property", true, new Model<String>( item.getModelObject().property ) ) );
                item.add( new AttributeModifier( "content", true, new Model<String>( item.getModelObject().content ) ) );
            }
        };

        add( metaTagList );
    }

    public static class MetaElement implements Serializable {
        public final String property;
        public final String content;

        public MetaElement( String property, String content ) {
            this.property = property;
            this.content = content;
        }
    }
}