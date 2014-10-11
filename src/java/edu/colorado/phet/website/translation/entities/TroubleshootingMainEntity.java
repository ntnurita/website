/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation.entities;

import edu.colorado.phet.website.content.troubleshooting.GeneralFAQPanel;
import edu.colorado.phet.website.content.troubleshooting.TroubleshootingMainPanel;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.translation.PhetPanelFactory;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;

public class TroubleshootingMainEntity extends TranslationEntity {

    public TroubleshootingMainEntity() {
        addString( "troubleshooting.main.title" );
        addString( "troubleshooting.main.intro" );
        addString( "troubleshooting.main.mac" );
        addString( "troubleshooting.main.windows" );
        addString( "troubleshooting.main.mobile" );
        addString( "troubleshooting.main.faqs" );
        addString( "troubleshooting.main.commonIssues" );
        addString( "troubleshooting.top" );
        addString( "troubleshooting.main.q1.title" );
        addString( "troubleshooting.main.q1.answer" );
        addString( "troubleshooting.main.q2.title" );
        addString( "troubleshooting.main.q2.answer" );
        addString( "troubleshooting.main.q4.title" );
        addString( "troubleshooting.main.q4.answer" );
        addString( "troubleshooting.main.q5.title" );
        addString( "troubleshooting.main.q5.answer" );
        addString( "troubleshooting.main.q6.title" );
        addString( "troubleshooting.main.q6.answer" );
        addString( "troubleshooting.main.q7.title" );
        addString( "troubleshooting.main.q7.answer" );
        addString( "troubleshooting.main.q8.title" );
        addString( "troubleshooting.main.q8.answer" );
        addString( "troubleshooting.main.q9.title" );
        addString( "troubleshooting.main.q9.answer" );
        addString( "troubleshooting.main.q10.title" );
        addString( "troubleshooting.main.q10.answer" );
        addString( "troubleshooting.main.q11.title" );
        addString( "troubleshooting.main.q11.answer" );
        addString( "troubleshooting.main.q12.title" );
        addString( "troubleshooting.main.q12.answer" );
        addString( "troubleshooting.main.q13.title" );
        addString( "troubleshooting.main.q13.answer" );
        addString( "troubleshooting.main.q14.title" );
        addString( "troubleshooting.main.q14.answer" );
        addString( "troubleshooting.main.q15.title" );
        addString( "troubleshooting.main.q15.answer" );
        addString( "troubleshooting.main.q16.title" );
        addString( "troubleshooting.main.q16.answer" );
        addString( "troubleshooting.main.q17.title" );
        addString( "troubleshooting.main.q17.answer" );
        addString( "troubleshooting.main.q18.title" );
        addString( "troubleshooting.main.q18.answer" );
        addString( "troubleshooting.main.q19.title" );
        addString( "troubleshooting.main.q19.answer" );
        addString( "faq.header" );
        addString( "faq.embedding.title" );
        addString( "faq.embedding.answer" );
        addString( "faq.sourceCode.title" );
        addString( "faq.sourceCode.answer" );
        addString( "faq.licensing.answer" );

        addString( "faq.mobileDevices.title" );
        addString( "faq.mobileDevices.answer" );

        addString( "troubleshooting.mac.q1.title" );
        addString( "troubleshooting.mac.q1.answer" );
        addString( "troubleshooting.mac.q2.title" );
        addString( "troubleshooting.mac.q2.answer" );
        addString( "troubleshooting.mac.q3.title" );
        addString( "troubleshooting.mac.q3.answer" );
        addString( "troubleshooting.mac.q4.title" );
        addString( "troubleshooting.mac.q4.answer" );
        addString( "troubleshooting.mac.q5.title" );
        addString( "troubleshooting.mac.q5.answer" );
        addString( "troubleshooting.mac.q6.title" );
        addString( "troubleshooting.mac.q6.answer.firefox" );
        addString( "troubleshooting.mac.q6.answer.chrome" );
        addString( "troubleshooting.mac.q6.answer.safari" );
        addString( "troubleshooting.mac.faqs" );

        addString( "troubleshooting.windows.q1.title" );
        addString( "troubleshooting.windows.q1.answer" );
        addString( "troubleshooting.windows.q2.title" );
        addString( "troubleshooting.windows.q2.answer" );
        addString( "troubleshooting.windows.q3.title" );
        addString( "troubleshooting.windows.q3.answer" );
        addString( "troubleshooting.windows.q4.title" );
        addString( "troubleshooting.windows.q4.answer" );
        addString( "troubleshooting.windows.q5.title" );
        addString( "troubleshooting.windows.q5.answer" );
        addString( "troubleshooting.windows.faqs" );

        addString( "troubleshooting.mobile.p1" );
        addString( "troubleshooting.mobile.p2" );
        addString( "troubleshooting.mobile.p3" );
        addString( "troubleshooting.mobile.p4" );
        addString( "troubleshooting.mobile.p5" );
        addString( "troubleshooting.mobile.p6" );
        addString( "troubleshooting.mobile.p7" );

        addString( "troubleshooting.mac.title" );
        addString( "troubleshooting.windows.title" );
        addString( "troubleshooting.mobile.title" );

        addString( "troubleshooting.javaSecurity.title" );
        addString( "troubleshooting.javaSecurity.monitoring" );
        addString( "troubleshooting.javaSecurity.upgradeStep" );
        addString( "troubleshooting.javaSecurity.disableStep" );
        addString( "troubleshooting.javaSecurity.downloadStep" );
        addString( "troubleshooting.javaSecurity.questions" );
        addString( "troubleshooting.javaSecurity.thankyou" );
        addString( "troubleshooting.javaSecurity.readNow" );
        addString( "troubleshooting.javaSecurity.javaAdvisory" );
        addString( "troubleshooting.javaSecurity.pleaseRead" );
        addString( "troubleshooting.javaSecurity.steps" );
        addString( "troubleshooting.javaSecurity.intro" );
        addString( "troubleshooting.javaSecurity.localInstallation" );

        addString( "nav.breadcrumb.faqs" );
        addString( "troubleshooting.main.licensingRequirements" );
        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                            return new TroubleshootingMainPanel( id, context );
                        }
                    }, "Troubleshooting (main)" );
        addPreview( new PhetPanelFactory() {
                        public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                            return new GeneralFAQPanel( id, context );
                        }
                    }, "FAQs" );
    }

    public String getDisplayName() {
        return "Troubleshooting (main)";
    }
}
