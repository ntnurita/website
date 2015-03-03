/*
 * Copyright 2015, University of Colorado
 */

package edu.colorado.phet.website.translation.entities;

import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.translation.PhetPanelFactory;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.webgl.WebglDisabledPanel;

public class WebglDisabledEntity extends TranslationEntity {
    public WebglDisabledEntity() {
        addString( "webglDisabled.header" );
        addString( "webglDisabled.IEWindows.header" );
        addString( "webglDisabled.IEWindows.text" );
        addString( "webglDisabled.chromeWindows.header" );
        addString( "webglDisabled.chromeWindows.text" );
        addString( "webglDisabled.firefoxWindows.header" );
        addString( "webglDisabled.firefoxWindows.text" );
        addString( "webglDisabled.safariMac.header" );
        addString( "webglDisabled.safariMac.text" );
        addString( "webglDisabled.safariMac.step1" );
        addString( "webglDisabled.safariMac.step2" );
        addString( "webglDisabled.safariMac.step3" );
        addString( "webglDisabled.safariMac.stepsUnavailable" );
        addString( "webglDisabled.chromeMac.header" );
        addString( "webglDisabled.chromeMac.text" );
        addString( "webglDisabled.firefoxMac.header" );
        addString( "webglDisabled.firefoxMac.text" );
        addString( "webglDisabled.safariIOS.header" );
        addString( "webglDisabled.safariIOS.text" );
        addString( "webglDisabled.chromeAndroid.header" );
        addString( "webglDisabled.chromeAndroid.text" );
        addString( "webglDisabled.chromeAndroid.step1" );
        addString( "webglDisabled.chromeAndroid.step2" );
        addString( "webglDisabled.chromeAndroid.note" );

        addPreview( new PhetPanelFactory() {
            public PhetPanel getNewPanel( String id, PageContext context, PhetRequestCycle requestCycle ) {
                return new WebglDisabledPanel( id, context );
            }
        }, "WebGL Disabled Page" );
    }

    public String getDisplayName() {
        return "WebGL Disabled";
    }
}
