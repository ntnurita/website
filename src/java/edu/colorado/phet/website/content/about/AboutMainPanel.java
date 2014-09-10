/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content.about;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.hibernate.Session;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.content.IndexPage;
import edu.colorado.phet.website.content.ResearchPanel;
import edu.colorado.phet.website.content.media.TechAwardPage;
import edu.colorado.phet.website.content.troubleshooting.TroubleshootingFlashPanel;
import edu.colorado.phet.website.content.troubleshooting.TroubleshootingJavaPanel;
import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class AboutMainPanel extends PhetPanel {
    public AboutMainPanel( String id, PageContext context ) {
        super( id, context );

        // add linkers
        add( TechAwardPage.getLinker().getLink( "tech-award-link", context, getPhetCycle() ) );
    }
    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        ((PhetMenuPage) this.getPage()).hideSocialBookmarkButtons();
        ((PhetMenuPage) this.getPage()).setContentWidth(1120);
    }
    public static String getKey() {
        return "about";
    }

    public static String getUrl() {
        return "about";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            @Override
            public String getRawUrl( PageContext context, PhetRequestCycle cycle ) {
                if ( DistributionHandler.redirectPageClassToProduction( cycle, AboutMainPanel.class ) ) {
                    return "http://phet.colorado.edu/about/index.php";
                }
                else {
                    return super.getRawUrl( context, cycle );
                }
            }

            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }
}
