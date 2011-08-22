// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.panels;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.ResourceModel;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.event.PostCollectionUpdateEvent;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostUpdateEvent;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.borders.SmallOrangeButtonBorder;
import edu.colorado.phet.website.cache.EventDependency;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.components.RawBodyLabel;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.components.RawLink;
import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.content.DonatePanel;
import edu.colorado.phet.website.content.about.AboutLegendPanel;
import edu.colorado.phet.website.content.contribution.ContributionCreatePage;
import edu.colorado.phet.website.content.simulations.SimsByKeywordPage;
import edu.colorado.phet.website.content.simulations.SimulationChangelogPage;
import edu.colorado.phet.website.content.simulations.TranslatedSimsPage;
import edu.colorado.phet.website.data.Keyword;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Project;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.data.TeachersGuide;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.data.contribution.Contribution;
import edu.colorado.phet.website.data.util.AbstractChangeListener;
import edu.colorado.phet.website.data.util.HibernateEventListener;
import edu.colorado.phet.website.data.util.IChangeListener;
import edu.colorado.phet.website.panels.contribution.ContributionBrowsePanel;
import edu.colorado.phet.website.panels.simulation.SimulationDisplayPanel;
import edu.colorado.phet.website.panels.sponsor.SimSponsorPanel;
import edu.colorado.phet.website.panels.sponsor.Sponsor;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.util.ClassAppender;
import edu.colorado.phet.website.util.HtmlUtils;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;
import edu.colorado.phet.website.util.wicket.WicketUtils;

import static edu.colorado.phet.website.util.HtmlUtils.encode;

public class FAQPanel extends PhetPanel {

    private static final Logger logger = Logger.getLogger( FAQPanel.class.getName() );

    public FAQPanel( String id, final String faqName, final PageContext context ) {
        super( id, context );

    }

}