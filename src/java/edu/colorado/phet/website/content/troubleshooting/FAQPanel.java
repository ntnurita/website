/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.content.troubleshooting;

import edu.colorado.phet.website.cache.InstallerCache;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.content.ForTranslatorsPanel;
import edu.colorado.phet.website.content.about.AboutLicensingPanel;
import edu.colorado.phet.website.content.about.AboutSourceCodePanel;
import edu.colorado.phet.website.content.getphet.FullInstallPanel;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

/**
 * General troubleshooting panel
 */
public class FAQPanel extends PhetPanel {
    public FAQPanel( String id, PageContext context ) {
        super( id, context );

        add( TroubleshootingMainPanel.getLinker().getLink( "troubleshooting-link", context, getPhetCycle() ) );

        add( new LocalizedText( "troubleshooting-main-q2-answer", "troubleshooting.main.q2.answer", new Object[]{
                FullInstallPanel.getLinker().getHref( context, getPhetCycle() ),
                InstallerCache.getWinSize() / 1000000,
                InstallerCache.getMacSize() / 1000000,
                InstallerCache.getLinuxSize() / 1000000
        } ) );

        add( new LocalizedText( "troubleshooting-main-q6-answer", "troubleshooting.main.q6.answer" ) );

        add( new LocalizedText( "troubleshooting-main-q10-answer", "troubleshooting.main.q10.answer", new Object[]{
                ForTranslatorsPanel.getLinker().getHref( context, getPhetCycle() )
        } ) );

        add( new LocalizedText( "troubleshooting-main-q15-answer", "troubleshooting.main.q15.answer" ) );

        add( new LocalizedText( "troubleshooting-main-q16-answer", "troubleshooting.main.q16.answer" ) );

        add( new LocalizedText( "troubleshooting-main-q17-answer", "troubleshooting.main.q17.answer" ) );

        add( new LocalizedText( "faq-embedding-answer", "faq.embedding.answer" ) );

        add( new LocalizedText( "faq-sourceCode-answer", "faq.sourceCode.answer", new Object[]{
                AboutSourceCodePanel.getLinker().getHref( context, getPhetCycle() )
        } ) );

        add( AboutLicensingPanel.getLinker().getLink( "licensing-link", context, getPhetCycle() ) );

    }

    public static String getKey() {
        return "faqs";
    }

    public static String getUrl() {
        return "faqs";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            public String getSubUrl( PageContext context ) {
                return getUrl();
            }
        };
    }
}