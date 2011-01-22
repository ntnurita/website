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

        addString( session, "home.about.otherSponsors", "Other Sponsors" );
        //overwriteString( session, "home.about.otherSponsors", "Other Sponsors", "along with our <a {0}>other sponsors</a> and educators like you." );
        overwriteString( session, "home.about.otherSponsors", "along with our <a {0}>other sponsors</a> and educators like you.", "Other Sponsors" );
        addString( session, "home.about.alongWithOurSponsors", "along with our <a {0}>other sponsors</a> and educators like you.");

        addString( session, "home.about.featuredSponsor", "Featured Sponsor" );

        addString( session, "home.meta", "Free educational simulations covering a diverse topics designed by the University of Colorado available in various languages." );

        addString( session, "contribution.title", "{0} - PhET Contribution" );

        //overwriteString( session, "home.about.sponsors", "Sponsors", "PhET is supported by" );
        overwriteString( session, "home.about.sponsors", "PhET is supported by", "Sponsors" );
        addString( session, "home.about.phetSupportedBy", "PhET is supported by..." );

        // TODO: sunset home.contribute string
        // TODO: sunset home.about.featuredSponsor

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
