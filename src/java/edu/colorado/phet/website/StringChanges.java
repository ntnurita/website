/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * Contains strings that have been added or modified since the last production deployment. If strings by those key names
 * don't exist they will be created.
 */
public class StringChanges {

    private static Logger logger = Logger.getLogger( StringChanges.class );

    public static void checkNewStrings() {
        Session session = HibernateUtils.getInstance().openSession();

        StringUtils.addString( session, "webglDisabled.header", "WebGL Simulation Compatibility" );
        StringUtils.addString( session, "webglDisabled.IEWindows.header", "Internet Explorer on Windows" );
        StringUtils.addString( session, "webglDisabled.IEWindows.text", "WebGL is only supported on Internet Explorer 11. If you are on Windows 8 or Windows 7, please update your IE browser to version 11. If you are on a previous version of Windows, either update your operating system or switch to a compatible browser." );
        StringUtils.addString( session, "webglDisabled.chromeWindows.header", "Google Chrome on Windows" );
        StringUtils.addString( session, "webglDisabled.chromeWindows.text", "Chrome has full support for WebGL on all platforms. If you are having issues with WebGL on Chrome, you may need to update to a more recent version of Chrome." );
        StringUtils.addString( session, "webglDisabled.firefoxWindows.header", "Firefox on Windows" );
        StringUtils.addString( session, "webglDisabled.firefoxWindows.text", "WebGL is not compatible with Firefox on certain graphics drivers. If you are having issues, please update your drivers (Intel, AMD, or NVIDIA) or switch to a compatible browser." );
        StringUtils.addString( session, "webglDisabled.safariMac.header", "Safari on Mac" );
        StringUtils.addString( session, "webglDisabled.safariMac.text", "WebGL is supported on most versions of Safari. For all versions of Safari for Mac before 10.10 Yosemite, WebGL must be enabled manually. To enable WebGL, follow the following steps:" );
        StringUtils.addString( session, "webglDisabled.safariMac.step1", "Go to Safari>Preferences" );
        StringUtils.addString( session, "webglDisabled.safariMac.step2", "Go to the Advanced tab and check the box marked \"Show Develop menu in menu bar.\"" );
        StringUtils.addString( session, "webglDisabled.safariMac.step3", "Go to Develop>Enable WebGL. This option should now be checked and WebGL should be enabled." );
        StringUtils.addString( session, "webglDisabled.safariMac.stepsUnavailable", "If these steps are not available, you may be on an incompatible version of Safari. You should update your Mac operating system or switch to a compatible browser." );
        StringUtils.addString( session, "webglDisabled.chromeMac.header", "Google Chrome for Mac" );
        StringUtils.addString( session, "webglDisabled.chromeMac.text", "Chrome has full support for WebGL on all platforms. If you are having issues with WebGL on Chrome, you may need to update to a more recent version of Chrome." );
        StringUtils.addString( session, "webglDisabled.firefoxMac.header", "Firefox on Mac" );
        StringUtils.addString( session, "webglDisabled.firefoxMac.text", "WebGL is compatible with Firefox on all versions of Mac from Snow Leopard 10.6. If you are having issues, please update to a more recent version of Mac or switch to a compatible browser." );
        StringUtils.addString( session, "webglDisabled.safariIOS.header", "Safari on iOS" );
        StringUtils.addString( session, "webglDisabled.safariIOS.text", "WebGL is only supported starting with iOS 8. If you are having issues, please update your operating system to the latest version." );
        StringUtils.addString( session, "webglDisabled.chromeAndroid.header", "Chrome on Android" );
        StringUtils.addString( session, "webglDisabled.chromeAndroid.text", "WebGL has only partial support in Android. If you are on Chrome version, 37 or later, WebGL will be automatically enabled if it is supported. If you are on an earlier version of Chrome, you may need to enable WebGL manually. To do so, follow these steps:" );
        StringUtils.addString( session, "webglDisabled.chromeAndroid.step1", "Type in \"chrome://flags\" into your address bar." );
        StringUtils.addString( session, "webglDisabled.chromeAndroid.step2", "Scroll until you find \"Enable WebGL\", and tap enable." );
        StringUtils.addString( session, "webglDisabled.chromeAndroid.note", "Note: not all Android devices support WebGL." );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
