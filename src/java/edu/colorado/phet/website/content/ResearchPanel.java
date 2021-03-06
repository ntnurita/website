/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.content;

import edu.colorado.phet.website.DistributionHandler;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.constants.Linkers;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.links.AbstractLinker;
import edu.colorado.phet.website.util.links.RawLinkable;

public class ResearchPanel extends PhetPanel {
    public ResearchPanel( String id, PageContext context ) {
        super( id, context );

        add( new LocalizedText( "research-top", "research.top" ) );

        add( new LocalizedText( "research-intro", "research.intro", new Object[] {
                "rel=\"external\"" + " " + Linkers.PHET_DESIGN_PROCESS_PDF.getHref( context, getPhetCycle() ),
                "rel=\"external\"" + " " + Linkers.PHET_LOOK_AND_FEEL_PDF.getHref( context, getPhetCycle() ),
                "href=\"#pub_1\""
        } ) );

        add( new LocalizedText( "research-question-1-answer", "research.question.1.answer" ) );
        add( new LocalizedText( "research-question-2-answer", "research.question.2.answer" ) );
        add( new LocalizedText( "research-question-3-answer", "research.question.3.answer" ) );

        add( new LocalizedText( "research-interest-analogy", "research.interest.analogy" ) );
        add( new LocalizedText( "research-interest-norms", "research.interest.norms" ) );
        add( new LocalizedText( "research-interest-exploration", "research.interest.exploration" ) );
        add( new LocalizedText( "research-interest-homework", "research.interest.homework" ) );
        add( new LocalizedText( "research-interest-chemistry", "research.interest.chemistry" ) );
    }

    public static String getKey() {
        return "research";
    }

    public static String getUrl() {
        return "research";
    }

    public static RawLinkable getLinker() {
        return new AbstractLinker() {
            @Override
            public String getRawUrl( PageContext context, PhetRequestCycle cycle ) {
                if ( DistributionHandler.redirectPageClassToProduction( cycle, ResearchPanel.class ) ) {
                    return "http://phet.colorado.edu/research/index.php";
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