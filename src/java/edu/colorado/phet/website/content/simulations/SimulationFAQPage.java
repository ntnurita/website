// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.content.simulations;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.hibernate.Session;

import edu.colorado.phet.website.content.NotFoundPage;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.data.faq.FAQList;
import edu.colorado.phet.website.menu.NavLocation;
import edu.colorado.phet.website.panels.faq.FAQPanel;
import edu.colorado.phet.website.templates.PhetMenuPage;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetUrlMapper;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.Result;
import edu.colorado.phet.website.util.hibernate.Task;
import edu.colorado.phet.website.util.hibernate.TaskException;
import edu.colorado.phet.website.util.links.AbstractLinker;

/**
 * Displays the FAQ for a simulation
 */
public class SimulationFAQPage extends PhetMenuPage {

    private static final Logger logger = Logger.getLogger( SimulationFAQPage.class.getName() );

    private String simTitle;

    public SimulationFAQPage( PageParameters parameters ) {
        super( parameters );

        final String simName = parameters.getString( "simulation" );

        Result<FAQList> faqListResult = HibernateUtils.resultCatchTransaction( getHibernateSession(), new Task<FAQList>() {
            public FAQList run( Session session ) {
                LocalizedSimulation lsim = HibernateUtils.getBestSimulation( session, getMyLocale(), simName );
                simTitle = lsim.getTitle();
                Simulation simulation = lsim.getSimulation();
                if ( !simulation.isFaqVisible() || simulation.getFaqList() == null ) {
                    throw new TaskException( "Simulation does not have a FAQ visible" );
                }
                return simulation.getFaqList();
            }
        } );

        if ( !faqListResult.success ) {
            throw new RestartResponseAtInterceptPageException( NotFoundPage.class );
        }

        // TODO: better way of handling look-ups for message-formatted strings (with locales). consolidate into PhetLocalizer
        String title = StringUtils.messageFormat( getPhetLocalizer().getString( "simulation.faq.title", this ), new Object[] {
                simTitle
        }, getMyLocale() );
        setTitle( title );

        add( new Label( "faq-header", title ) );

        // TODO: meta description!
//        setMetaDescription( ogDescription );

        add( new FAQPanel( "faq-panel", faqListResult.value.getName(), getPageContext() ) );

        initializeLocationWithSet( new ArrayList<NavLocation>() );

    }

    public static void addToMapper( PhetUrlMapper mapper ) {
        mapper.addMap( "^simulation/([^/]+)/faq$", SimulationFAQPage.class, new String[] { "simulation" } );
    }

    public static AbstractLinker getLinker( final String projectName, final String simulationName ) {
        // WARNING: don't change without also changing the old URL redirection
        return new AbstractLinker() {
            @Override
            public String getSubUrl( PageContext context ) {
                return "simulation/" + simulationName + "/faq";
            }
        };
    }

    public static AbstractLinker getLinker( final Simulation simulation ) {
        return getLinker( simulation.getProject().getName(), simulation.getName() );
    }

    public static AbstractLinker getLinker( final LocalizedSimulation localizedSimulation ) {
        return getLinker( localizedSimulation.getSimulation() );
    }
}