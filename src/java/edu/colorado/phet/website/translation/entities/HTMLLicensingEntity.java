// Copyright 2002-2013, University of Colorado

package edu.colorado.phet.website.translation.entities;

import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.translation.PhetPanelFactory;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.content.about.HTMLLicensingPanel;

public class HTMLLicensingEntity extends TranslationEntity {
    public HTMLLicensingEntity() {
        addString( "htmlLicensing.header" );
        addString( "htmlLicensing.preamble" );
        addString( "htmlLicensing.agreementI" );
        addString( "htmlLicensing.agreementII" );
        addString( "htmlLicensing.agreementIIA" );
        addString( "htmlLicensing.agreementIIB" );
        addString( "htmlLicensing.agreementIIC" );
        addString( "htmlLicensing.agreementIID" );
        addString( "htmlLicensing.agreementIIE" );
        addString( "htmlLicensing.agreementIIF" );
        addString( "htmlLicensing.agreementIII" );
        addString( "htmlLicensing.agreementIV" );
        addString( "htmlLicensing.agreementIVA" );
        addString( "htmlLicensing.agreementIVB" );
        addString( "htmlLicensing.agreementIVC" );
        addString( "htmlLicensing.agreementIVD" );
        addString( "htmlLicensing.agreementIVE" );
        addString( "htmlLicensing.agreementIVF" );
        addString( "htmlLicensing.agreementV" );
        addString( "htmlLicensing.agreementVI" );
        addString( "htmlLicensing.agreementI.header" );
        addString( "htmlLicensing.agreementI.p1" );
        addString( "htmlLicensing.agreementI.p2" );
        addString( "htmlLicensing.agreementII.header" );
        addString( "htmlLicensing.agreementIIA.header" );
        addString( "htmlLicensing.agreementIIA.p1" );
        addString( "htmlLicensing.agreementIIA.p2" );
        addString( "htmlLicensing.agreementIIA.p3" );
        addString( "htmlLicensing.agreementIIA.p4" );
        addString( "htmlLicensing.agreementIIA.p5" );
        addString( "htmlLicensing.agreementIIA.p6" );
        addString( "htmlLicensing.agreementIIA.p7" );
        addString( "htmlLicensing.agreementIIB.header" );
        addString( "htmlLicensing.agreementIIB.p1" );
        addString( "htmlLicensing.agreementIIB.p2" );
        addString( "htmlLicensing.agreementIIB.p3" );
        addString( "htmlLicensing.agreementIIB.p4" );
        addString( "htmlLicensing.agreementIIB.p5" );
        addString( "htmlLicensing.agreementIIC.header" );
        addString( "htmlLicensing.agreementIIC.p1" );
        addString( "htmlLicensing.agreementIIC.p2" );
        addString( "htmlLicensing.agreementIIC.p3" );
        addString( "htmlLicensing.agreementIIC.p4" );
        addString( "htmlLicensing.agreementIIC.p5" );
        addString( "htmlLicensing.agreementIID.header" );
        addString( "htmlLicensing.agreementIID.p1" );
        addString( "htmlLicensing.agreementIID.p2" );
        addString( "htmlLicensing.agreementIIE.header" );
        addString( "htmlLicensing.agreementIIE.p1" );
        addString( "htmlLicensing.agreementIIF.header" );
        addString( "htmlLicensing.agreementIIF.p1" );
        addString( "htmlLicensing.agreementIII.header" );
        addString( "htmlLicensing.agreementIII.p1" );
        addString( "htmlLicensing.agreementIV.header" );
        addString( "htmlLicensing.agreementIVA.header" );
        addString( "htmlLicensing.agreementIVA.p1" );
        addString( "htmlLicensing.agreementIVB.header" );
        addString( "htmlLicensing.agreementIVB.p1" );
        addString( "htmlLicensing.agreementIVB.p2" );
        addString( "htmlLicensing.agreementIVB.p3" );
        addString( "htmlLicensing.agreementIVB.p4" );
        addString( "htmlLicensing.agreementIVC.header" );
        addString( "htmlLicensing.agreementIVC.p1" );
        addString( "htmlLicensing.agreementIVD.header" );
        addString( "htmlLicensing.agreementIVD.p1" );
        addString( "htmlLicensing.agreementIVE.header" );
        addString( "htmlLicensing.agreementIVE.p1" );
        addString( "htmlLicensing.agreementIVF.header" );
        addString( "htmlLicensing.agreementIVF.p1" );
        addString( "htmlLicensing.agreementV.header" );
        addString( "htmlLicensing.agreementV.p1" );
        addString( "htmlLicensing.agreementV.p2" );
        addString( "htmlLicensing.agreementV.p3" );
        addString( "htmlLicensing.agreementVI.header" );
        addString( "htmlLicensing.agreementVI.p1" );
        addString( "htmlLicensing.disclaimer" );

        addPreview(new PhetPanelFactory() {
            public PhetPanel getNewPanel(String id, PageContext context, PhetRequestCycle requestCycle) {
                return new HTMLLicensingPanel(id, context);
            }
        }, "HTML Licensing Panel Page" );
    }

    public String getDisplayName() {
        return "HTML Licensing Panel";
    }
}
