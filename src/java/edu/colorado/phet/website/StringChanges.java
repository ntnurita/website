/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * Contains strings that have been addedor modified since the last production deployment. If strings by those key names
 * don't exist they will be created.
 */
public class StringChanges {

    private static Logger logger = Logger.getLogger( StringChanges.class );

    public static void checkNewStrings() {
        Session session = HibernateUtils.getInstance().openSession();

        addString( session, "sponsors.techsmith.desc", "For providing multiple licenses of Camtasia Studio " );

        overwriteString( session, "sponsors.techsmith.desc", "For providing multiple licenses of Camtasia Studio ", "For providing licenses of Camtasia Studio " );

        overwriteString( session, "home.browseActivities", "Activities contributed by Teachers", "Browse Activities" );
        overwriteString( session, "home.submitActivity", "Provide ideas you've used in class", "Contribute Activities" );
        overwriteString( session, "home.workshops", "Workshops", "Workshops and Materials" );
        overwriteString( session, "home.workshops", "Workshops and Materials", "Workshops / Materials" );

//        addString( session, "home.about.otherSponsors", "Other Sponsors" );
        //overwriteString( session, "home.about.otherSponsors", "Other Sponsors", "along with our <a {0}>other sponsors</a> and educators like you." );
//        overwriteString( session, "home.about.otherSponsors", "along with our <a {0}>other sponsors</a> and educators like you.", "Other Sponsors" );
        addString( session, "home.about.alongWithOurSponsors", "along with our <a {0}>other sponsors</a> and educators like you." );
        overwriteString( session, "home.about.alongWithOurSponsors", "along with our <a {0}>other sponsors</a> and educators like you.", "as well as our <a {0}>other sponsors</a> and educators like you." );
        overwriteString( session, "home.about.alongWithOurSponsors", "as well as our <a {0}>other sponsors</a> and educators like you.", "and our <a {0}>other sponsors</a>, including educators like you." );

        addString( session, "home.meta", "Free educational simulations covering a diverse topics designed by the University of Colorado available in various languages." );

        addString( session, "contribution.title", "{0} - PhET Contribution" );

        //overwriteString( session, "home.about.sponsors", "Sponsors", "PhET is supported by" );
        overwriteString( session, "home.about.sponsors", "PhET is supported by", "Sponsors" );
        addString( session, "home.about.phetSupportedBy", "PhET is supported by..." );
//        overwriteString( session, "home.about.phetSupportedBy", "PhET is supported by...", "PhET is supported by" );
        overwriteString( session, "home.about.phetSupportedBy", "PhET is supported by", "PhET is supported by..." );

        addString( session, "simulationMainPanel.seeBelow.tipsForTeachers", "Tips for Teachers >>" );
        addString( session, "simulationMainPanel.seeBelow.relatedSimulations", "Related Sims >>" );
        addString( session, "simulationMainPanel.relatedSimulations", "Related Simulations" );

//        addString( session, "sponsors.hewlett.name", "The William and Flora Hewlett Foundation" );
//        addString( session, "sponsors.nsf.name", "The National Science Foundation" );
//        addString( session, "sponsors.ksu.name", "ERCSME at King Saud University" );
//        addString( session, "sponsors.odonnell.name", "The O'Donnell Foundation" );

        addString( session, "home.troubleshooting", "Troubleshooting" );

        addString( session, "simulationMainPanel.teachingResources", "Teaching Resources" );

        addString( session, "home.donate", "Donate" );

        addString( session, "search.autocomplete.simulation", "{0} (simulation)" );

        addString( session, "changePasswordSuccess.message", "Your password has been successfully changed." );

//        addString( session, "sponsors.mortenson.name", "The Mortenson Family Foundation" );

        deleteString( session, "home.about.featuredSponsor" );
        deleteString( session, "home.about.otherSponsor" );
        deleteString( session, "sponsors.mortenson.name" );
        deleteString( session, "sponsors.odonnell.name" );
        deleteString( session, "sponsors.ksu.name" );
        deleteString( session, "sponsors.nsf.name" );
        deleteString( session, "sponsors.hewlett.name" );
        deleteString( session, "home.about.otherSponsors" );
        deleteString( session, "home.contribute" );

        addString( session, "sponsors.sim.supportedBy", "PhET is supported by" );
        addString( session, "sponsors.sim.supportedByThe", "PhET is supported by the" );
        addString( session, "sponsors.sim.andEducators", "and educators like you." );
        addString( session, "sponsors.sim.thanks", "Thanks!" );

        addString( session, "sponsors.silver", "Silver Level Contributors ($5k - $10k)" );

        addString( session, "troubleshooting.main.q17.title", "How can I maximize PhET simulations in a browser?" );
        addString( session, "troubleshooting.main.q17.answer", "<p>Most browsers have an option to switch into a fullscreen mode. Below are instructions for the most common browsers:</p> <ul> <li>Internet Explorer (all versions): Press F11</li> <li>Firefox: Press F11 (Windows, Linux), Shift-Command-F (Mac)</li> <li>Google Chrome: Press F11 (Windows, Linux), Shift-Command-F (Mac)</li> <li>Safari: Fullscreen support with 3rd-party plugins (Mac, Windows)</li> <li>Opera: Press F11 (Windows, Linux)</li> </ul>" );

        addString( session, "troubleshooting.main.q18.title", "Why do some foreign language characters show up as empty boxes?" );
        addString( session, "troubleshooting.main.q18.answer", "<p>Some characters from non-English languages will show up as empty squares when the proper fonts are not installed. Below are instructions to install additional language support for various operating systems:</p> <ul> <li>Windows 2000 / XP: <a href=\"http://support.microsoft.com/kb/177561\">http://support.microsoft.com/kb/177561</a></li> <li>Windows Vista: <a href=\"http://windows.microsoft.com/en-US/windows-vista/How-do-I-get-additional-language-files\">http://windows.microsoft.com/en-US/windows-vista/How-do-I-get-additional-language-files</a></li> <li>Windows 7: <a href=\"http://support.microsoft.com/kb/972813\">http://support.microsoft.com/kb/972813</a></li> <li>OS X: <a href=\"http://support.apple.com/kb/DL1123?viewlocale=en_US\">http://support.apple.com/kb/DL1123?viewlocale=en_US</a></li> </ul>" );

        addString( session, "simulationMainPanel.embed", "Embed" );
        addString( session, "embed.clickToLaunch", "Click Here to Launch" );
        overwriteString( session, "embed.clickToLaunch", "Click Here to Launch", "Click to Run" );

        addString( session, "embed.direct.title", "Embed a running copy of this simulation" );
        addString( session, "embed.direct.instructions", "Use this HTML to embed a running copy of this simulation. You can change the width and height of the embedded simulation by changing the \"width\" and \"height\" attributes in the HTML." );
        addString( session, "embed.indirect.title", "Embed an image that will launch the simulation when clicked" );
        addString( session, "embed.indirect.instructions", "Use this HTML code to display a screenshot with the words \"Click to Run\"." );
        addString( session, "embed.close", "close" );

        deleteString( session, "nav.faq" );
        addString( session, "nav.faqs", "Frequently Asked Questions" );
        addString( session, "faqs.title", "Frequently Asked Questions about PhET simulations" );
        addString( session, "troubleshooting.main.commonIssues", "Common Troubleshooting Issues" );
        addString( session, "home.faqs", "Frequently Asked Questions" );
        overwriteString( session, "home.faqs", "Frequently Asked Questions", "FAQs" );
        overwriteString( session, "nav.faqs", "Frequently Asked Questions", "FAQs" );

        addString( session, "faq.embedding.title", "How do I embed PhET simulations?" );
        addString( session, "faq.embedding.answer", "<p>Click the 'Embed' button under the simulation you would like to embed. This will bring up a text box of html code that you can copy and paste in order to embed the sim. Flash sims may be directly embedded or linked via an image to launch the sim. Java sims may only be linked via an image to launch the sim.</p>" );

        addString( session, "sponsors.youQuestion", "You?" );
        addString( session, "sponsors.youSubtitle", "(support PhET today and help STEM education worldwide.)" );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    *----------------------------------------------------------------------------*/

    public static void addString( Session session, String key, String value ) {
        String result = StringUtils.getStringDirect( session, key, PhetWicketApplication.getDefaultLocale() );
        if ( result == null ) {
            logger.warn( "Auto-setting English string with key=" + key + " value=" + value );
            StringUtils.setEnglishString( session, key, value );
        }
    }

    private static void overwriteString( Session session, String key, String oldValue, String newValue ) {
        String result = StringUtils.getStringDirect( session, key, PhetWicketApplication.getDefaultLocale() );
        if ( result == null ) {
            logger.warn( "Auto-setting English string with key=" + key + " value=" + newValue );
            StringUtils.setEnglishString( session, key, newValue );
        }
        else {
            if ( result.equals( oldValue ) ) {
                logger.warn( "Auto-setting English string with key=" + key + " value=" + newValue + " over old value " + oldValue );
                StringUtils.setEnglishString( session, key, newValue );
            }
        }
    }

    private static void deleteString( Session session, final String key ) {
        String result = StringUtils.getStringDirect( session, key, PhetWicketApplication.getDefaultLocale() );
        if ( result != null ) {
            logger.warn( "Deleting English string with key=" + key + " value=" + result );
            HibernateUtils.wrapTransaction( session, new HibernateTask() {
                public boolean run( Session session ) {
                    TranslatedString translatedString = (TranslatedString) session.createQuery( "select ts from TranslatedString as ts, Translation as t where (ts.translation = t and t.visible = true and t.locale = :locale and ts.key = :key)" )
                            .setLocale( "locale", PhetWicketApplication.getDefaultLocale() ).setString( "key", key ).uniqueResult();
                    if ( translatedString != null ) {
                        translatedString.getTranslation().removeString( translatedString );
                        session.delete( translatedString );
                    }
                    return true;
                }
            } );
        }
    }
}
