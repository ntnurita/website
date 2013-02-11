/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.constants;

import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;

/**
 * Global website constants
 */
public class WebsiteConstants {

    public static final SimpleDateFormat ISO_8601 = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );

    // General help email address
    public static final String HELP_EMAIL = "phethelp@colorado.edu";

    // Address from which many emails are sent.
    public static final String PHET_NO_REPLY_EMAIL_ADDRESS = "phetnoreply@phet.colorado.edu";

    // email footer with legal address. NOTE that for newsletters, this is not used (instead, the text in the newsletter.html is used)
    public static final String EMAIL_ADDRESS_FOOTER = "PhET Interactive Simulations | University of Colorado 390 UCB | Boulder, CO 80309-0390";

    // Web server hostname
    public static final String WEB_SERVER = "phet.colorado.edu";

    public static final Locale ENGLISH = LocaleUtils.stringToLocale( "en" );
}
