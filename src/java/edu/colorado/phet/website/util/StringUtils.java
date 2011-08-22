/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.util;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.hibernate.Session;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.components.LocalizedLabel;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * Includes static functions (with their own transaction handling) that handle setting and getting of localization strings
 */
public class StringUtils {

    public static final String LIST_SEPARATOR_KEY = "listSeparator";

    private static final Logger logger = Logger.getLogger( StringUtils.class.getName() );

    /**
     * Returns a default (English) string from the database
     *
     * @param session Hibernate session (already open)
     * @param key     Localization key
     * @return Translated String (probably not translated though!)
     */
    public static String getDefaultStringDirect( Session session, String key ) {
        return getStringDirect( session, key, PhetWicketApplication.getDefaultLocale() );
    }

    /**
     * Returns a string from the database for a visible translation (specified by a locale)
     *
     * @param session Hibernate session (already open)
     * @param key     Localization key
     * @param locale  Locale of the string
     * @return Translated String
     */
    public static String getStringDirect( Session session, final String key, final Locale locale ) {
        final String[] ret = new String[1];
        ret[0] = null;
        HibernateUtils.wrapTransaction( session, new HibernateTask() {
            public boolean run( Session session ) {
                TranslatedString translatedString = (TranslatedString) session.createQuery( "select ts from TranslatedString as ts, Translation as t where (ts.translation = t and t.visible = true and t.locale = :locale and ts.key = :key)" )
                        .setLocale( "locale", locale ).setString( "key", key ).uniqueResult();
                if ( translatedString != null ) {
                    ret[0] = translatedString.getValue();
                }
                return true;
            }
        } );
        return ret[0];
    }

    public static String getEnglishStringDirect( Session session, final String key ) {
        return getStringDirect( session, key, PhetWicketApplication.getDefaultLocale() );
    }

    /**
     * Returns a string from the database for a visible translation (specified by a locale). Run from within the
     * transaction
     * <p/>
     * (The X denotes that this function should only be called from within the scope of a Hibernate transaction for the
     * particular session)
     *
     * @param session Hibernate session (already open)
     * @param key     Localization key
     * @param locale  Locale of the string
     * @return Translated String
     */
    public static String getStringDirectWithinTransaction( Session session, final String key, final Locale locale ) {
        TranslatedString string = getTranslatedString( session, key, locale );
        if ( string == null ) {
            return null;
        }
        else {
            return string.getValue();
        }
    }

    /**
     * Returns a translated string instance, assuming one is already inside of a transaction.
     *
     * @param session Hibernate session (already open)
     * @param key     Localization key
     * @param locale  Locale of the string
     * @return Translated String
     */
    public static TranslatedString getTranslatedString( Session session, final String key, final Locale locale ) {
        List list = session.createQuery( "select ts from TranslatedString as ts, Translation as t where (ts.translation = t and t.visible = true and t.locale = :locale and ts.key = :key)" )
                .setLocale( "locale", locale ).setString( "key", key ).list();
        if ( list.isEmpty() ) {
            return null;
        }
        else {
            if ( list.size() != 1 ) {
                logger.warn( "strings for key " + key + ", locale " + locale + " have " + list.size() + " options" );
            }
            return (TranslatedString) list.get( 0 );
        }
    }

    /**
     * Returns a string from the database for any translation (by id)
     *
     * @param session       Hibernate Session (already open)
     * @param key           Localization key
     * @param translationId Translation ID
     * @return Translated String
     */
    public static String getStringDirect( Session session, final String key, final int translationId ) {
        final String[] ret = new String[1];
        ret[0] = null;
        HibernateUtils.wrapTransaction( session, new HibernateTask() {
            public boolean run( Session session ) {
                TranslatedString translatedString = (TranslatedString) session.createQuery( "select ts from TranslatedString as ts, Translation as t where (ts.translation = t and t.id = :id and ts.key = :key)" )
                        .setInteger( "id", translationId ).setString( "key", key ).uniqueResult();
                if ( translatedString != null ) {
                    ret[0] = translatedString.getValue();
                }
                return true;
            }
        } );
        return ret[0];
    }

    /**
     * Returns a string from the database for any translation (by id). Run from within a transaction
     * <p/>
     * (The X denotes that this function should only be called from within the scope of a Hibernate transaction for the
     * particular session)
     *
     * @param session       Hibernate Session (already open)
     * @param key           Localization key
     * @param translationId Translation ID
     * @return Translated String
     */
    public static String getStringDirectWithinTransaction( Session session, final String key, final int translationId ) {
        TranslatedString string = getTranslatedString( session, key, translationId );
        if ( string == null ) {
            return null;
        }
        else {
            return string.getValue();
        }
    }

    /**
     * Returns a translated string instance, assuming one is already inside of a transaction.
     *
     * @param session       Hibernate Session (already open)
     * @param key           Localization key
     * @param translationId Translation ID
     * @return Translated String
     */
    public static TranslatedString getTranslatedString( Session session, final String key, final int translationId ) {
        List list = session.createQuery( "select ts from TranslatedString as ts, Translation as t where (ts.translation = t and t.id = :id and ts.key = :key)" )
                .setInteger( "id", translationId ).setString( "key", key ).list();
        if ( list.isEmpty() ) {
            return null;
        }
        else {
            if ( list.size() != 1 ) {
                logger.warn( "strings for key " + key + ", translationId " + translationId + " have " + list.size() + " options" );
            }
            return (TranslatedString) list.get( 0 );
        }
    }

    public static final int STRING_TRANSLATED = 0;
    public static final int STRING_UNTRANSLATED = 1;
    public static final int STRING_OUT_OF_DATE = 2;

    public static int stringStatus( Session session, final String key, final int translationId ) {
        StatusTask task = new StatusTask( translationId, key );
        HibernateUtils.wrapTransaction( session, task );
        return task.status;
    }

    public static String escapeFileString( String str ) {
        char[] chars = str.toCharArray();
        StringBuffer buf = new StringBuffer();
        for ( char c : chars ) {
            if ( Character.isLetterOrDigit( c ) ) {
                buf.append( c );
            }
            else {
                buf.append( "_" );
            }
        }
        return buf.toString();
    }

    /**
     * Similar to makeUrlAbsolute, but just uses HTTP and phet.colorado.edu. Detects with a leading slash (/)
     *
     * @param url Relative url
     * @return Absolute URL
     */
    public static String makeUrlAbsoluteProduction( String url ) {
        if ( url.startsWith( "/" ) ) {
            return "http://phet.colorado.edu" + url;
        }
        else {
            return url;
        }
    }

    /**
     * This will turn URLs relative to the PhET server root into absolute urls, preserving the server name and protocol.
     *
     * @param url Will not turn truly relative URLs (ones without a leading /) to absolute URLs.
     * @return Absolute URL if the above is met
     */
    public static String makeUrlAbsolute( String url ) {
        String server = PhetRequestCycle.get().getServerName();
        String scheme = PhetRequestCycle.get().getScheme(); // protocol
        if ( url.startsWith( "/" ) ) {
            return scheme + "://" + server + url;
        }
        else {
            return url;
        }
    }

    /**
     * Looks up an English string from a translation key and a session, and unescapes common characters
     *
     * @param session Session
     * @param key     Translation key
     * @return English default string
     */
    public static String lookup( Session session, String key ) {
        return PhetLocalizer.get().getDefaultString( session, key, "", true ).replace( "&#039;", "'" ).replace( "&quot;", "\"" ).replace( "&amp;", "&" ).replace( "&lt;", "<" ).replace( "&gt;", ">" );
    }

    /*---------------------------------------------------------------------------*
    * string changes
    *----------------------------------------------------------------------------*/

    /**
     * Add a string to our database
     *
     * @param session      Hibernate session
     * @param key          Translation key
     * @param englishValue Initial English value
     */
    public static void addString( Session session, String key, String englishValue ) {
        String result = getStringDirect( session, key, PhetWicketApplication.getDefaultLocale() );
        if ( result == null ) {
            logger.warn( "Auto-setting English string with key=" + key + " value=" + englishValue );
            setEnglishString( session, key, englishValue );
        }
    }

    /**
     * Overwrite an English string in the database, but only if we know the previous value.
     * This extra check allows us to specify string changes in code (see StringChanges), so that if we decide to change the value
     * to something else and then run the old overwriteString call, it will NOT overwrite the new (different) value.
     *
     * @param session         Hibernate session
     * @param key             Translation key
     * @param oldEnglishValue Hopefully current English value. If not, we will do nothing
     * @param newEnglishValue New English value
     */
    public static void overwriteString( Session session, String key, String oldEnglishValue, String newEnglishValue ) {
        String result = getStringDirect( session, key, PhetWicketApplication.getDefaultLocale() );
        if ( result == null ) {
            logger.warn( "Auto-setting English string with key=" + key + " value=" + newEnglishValue );
            setEnglishString( session, key, newEnglishValue );
        }
        else {
            if ( result.equals( oldEnglishValue ) ) {
                logger.warn( "Auto-setting English string with key=" + key + " value=" + newEnglishValue + " over old value " + oldEnglishValue );
                setEnglishString( session, key, newEnglishValue );
            }
        }
    }

    /**
     * Delete all translatable strings with a specific key
     *
     * @param session Hibernate session
     * @param key     Translation key
     * @return Success
     */
    public static boolean deleteString( Session session, final String key ) {
        // TODO: consider using session.getTransaction().isActive() to detect whether we are in a transaction or not
        return HibernateUtils.wrapTransaction( session, new HibernateTask() {
            public boolean run( Session session ) {
                deleteStringWithinTransaction( session, key );
                return true;
            }
        } );
    }

    /**
     * Delete all translatable strings with a specific key, inside of a transaction
     *
     * @param session Hibernate session
     * @param key     Translation key
     */
    public static void deleteStringWithinTransaction( Session session, final String key ) {
        String result = getStringDirectWithinTransaction( session, key, PhetWicketApplication.getDefaultLocale() );
        if ( result != null ) {
            logger.warn( "Deleting strings with key=" + key + ". English value=" + result );
            List strings = session.createQuery( "select ts from TranslatedString as ts where ts.key = :key" ).setString( "key", key ).list();
            for ( Object string : strings ) {
                TranslatedString str = (TranslatedString) string;
                str.getTranslation().removeString( str );
                session.delete( str );
            }
        }
    }

    private static class StatusTask implements HibernateTask {
        public int status;
        private final int translationId;
        private final String key;

        public StatusTask( int translationId, String key ) {
            this.translationId = translationId;
            this.key = key;
            status = STRING_UNTRANSLATED;
        }

        public boolean run( Session session ) {
            // should hit an error and return false if it doesn't exist
            TranslatedString string = (TranslatedString) session.createQuery(
                    "select ts from TranslatedString as ts where ts.translation.id = :id and ts.key = :key" )
                    .setInteger( "id", translationId ).setString( "key", key ).uniqueResult();
            if ( string != null ) {
                status = STRING_TRANSLATED;
            }
            else {
                return true;
            }
            TranslatedString englishString = (TranslatedString) session.createQuery(
                    "select ts from TranslatedString as ts where ts.translation.visible = true and ts.translation.locale = :locale and ts.key = :key" )
                    .setLocale( "locale", PhetWicketApplication.getDefaultLocale() ).setString( "key", key ).uniqueResult();
            if ( string.getUpdatedAt().compareTo( englishString.getUpdatedAt() ) < 0 ) {
                status = STRING_OUT_OF_DATE;
            }
            return true;
        }
    }

    public static boolean isStringSet( Session session, final String key, final int translationId ) {
        return HibernateUtils.wrapTransaction( session, new HibernateTask() {
            public boolean run( Session session ) {
                // should hit an error and return false if it doesn't exist
                TranslatedString string = (TranslatedString) session.createQuery(
                        "select ts from TranslatedString as ts where ts.translation.id = :id and ts.key = :key" )
                        .setInteger( "id", translationId ).setString( "key", key ).uniqueResult();
                return string != null;
            }
        } );
    }

    public static boolean setEnglishString( Session session, String key, String value ) {
        return setString( session, key, value, PhetWicketApplication.getDefaultLocale() );
    }

    /**
     * Turn newlines into line breaks, so that people can type in line-breaks and have it
     * appear visually in that style. We need to store them in this modified format since
     * using wicket's string lookup inserts the strings as-is.
     *
     * @param str The string to convert
     * @return The string with newlines replaced with XHTML line-breaks
     */
    public static String mapStringForStorage( String str ) {
        // Tested on Mac, Safari 4.0.5, Firefox 3.6.3
        str = str.replaceAll( "\r", "" );
        str = str.replaceAll( "\n", "<br/>" );
        return str;
    }

    /**
     * Turn line-breaks into newlines, so that we can take stored translated string text and
     * make it fit nicely within a multiline text editing box
     *
     * @param str String to convert
     * @return The string with XHTML line-breaks replaced with newlines
     */
    public static String mapStringForEditing( String str ) {
        return str.replaceAll( "<br/>", "\n" );
    }

    public static void setEnglishStringWithinTransaction( Session session, String key, String value ) {
        setStringWithinTransaction( session, HibernateUtils.getTranslationWithinTransaction( session, PhetWicketApplication.getDefaultLocale() ), key, value );
    }

    public static void setStringWithinTransaction( Session session, Translation translation, String key, String value ) {
        value = mapStringForStorage( value );

        TranslatedString tString = null;

        // look to see if we have an existing translated string
        // TODO: inefficient. do a better lookup, since we probably index on string key!
        for ( Object o : translation.getTranslatedStrings() ) {
            TranslatedString ts = (TranslatedString) o;
            if ( ts.getKey().equals( key ) ) {
                tString = ts;
                break;
            }
        }

        // if there is no existing string, create a new one
        if ( tString == null ) {
            tString = new TranslatedString();
            tString.initializeNewString( translation, key, value );
            session.save( tString );
            session.update( translation );
        }
        else {
            // otherwise, update the existing string
            tString.setValue( value );
            tString.setUpdatedAt( new Date() );

            session.update( tString );
        }

        // if it's cached, change the cache entries so it doesn't fail
        // TODO: fix: on transaction failure, we will have deposited the wrong value in the cache!!!
        ( (PhetLocalizer) PhetWicketApplication.get().getResourceSettings().getLocalizer() ).updateCachedString( translation, key, value );
    }

    public static boolean setString( Session session, final String key, final String value, final Locale locale ) {
        return HibernateUtils.wrapTransaction( session, new HibernateTask() {
            public boolean run( Session session ) {
                Translation translation = HibernateUtils.getTranslationWithinTransaction( session, locale );
                setStringWithinTransaction( session, translation, key, value );
                return true;
            }
        } );
    }

    public static boolean setString( Session session, final String key, final String value, final int translationId ) {
        logger.info( "Request to set string with key=" + key + " and value=" + value );
        if ( value == null ) {
            return false;
        }
        return HibernateUtils.wrapTransaction( session, new HibernateTask() {
            public boolean run( Session session ) {
                Translation translation = HibernateUtils.getTranslationWithinTransaction( session, translationId );
                setStringWithinTransaction( session, translation, key, value );
                return true;
            }
        } );
    }

    private static String messageFormatFilter( String str ) {
        StringBuilder ret = new StringBuilder();
        for ( int i = 0; i < str.length(); i++ ) {
            ret.append( str.charAt( i ) );
            if ( str.charAt( i ) == '\'' ) {
                boolean escapeBrace = (
                        i + 2 < str.length()
                        && (
                                str.charAt( i + 1 ) == '{' || str.charAt( i + 1 ) == '}'
                        ) && str.charAt( i + 2 ) == '\''
                );
                if ( escapeBrace ) {
                    ret.append( str.charAt( i + 1 ) );
                    ret.append( '\'' );
                    i += 2;
                }
                else {
                    ret.append( '\'' );
                }
            }
        }
        return ret.toString();
    }

    public static String messageFormat( String str, String obj ) {
        return MessageFormat.format( messageFormatFilter( str ), obj );
    }

    public static String messageFormat( String str, Object[] objs ) {
        return MessageFormat.format( messageFormatFilter( str ), objs );
    }

    public static String messageFormat( String str, Object[] objs, Locale locale ) {
        MessageFormat format = new MessageFormat( messageFormatFilter( str ), locale );
        return format.format( objs, new StringBuffer(), null ).toString();
    }

    /**
     * A string representing the name of one locale translated to another locale
     *
     * @param locale       The locale that is the subject we want to display
     * @param targetLocale The locale that the subject should be translated into if possible
     * @param localizer    A reference to the localizer. getPhetLocalizer() should work in most places
     * @return
     */
    public static String getLocaleTitle( Locale locale, Locale targetLocale, PhetLocalizer localizer ) {
        // TODO: let's make this efficient
        String defaultLanguageName = locale.getDisplayName( targetLocale );
        String languageName = localizer.getString( "language.names." + LocaleUtils.localeToString( locale ), new LocalizedLabel( "toss", targetLocale, "toss" ), null, defaultLanguageName, false );
        return languageName;
    }

    public static String getEnglishLocaleTitle( Session session, Locale locale ) {
        String defaultLanguageName = locale.getDisplayName( locale );
        return PhetLocalizer.get().getDefaultString( session, "language.names." + LocaleUtils.localeToString( locale ), defaultLanguageName, false );
    }

    public static String combineStringsIntoList( Component component, List<String> strings, String separator ) {
        String ret = "";
        boolean started = false;
        for ( String str : strings ) {
            if ( started ) {
                ret += separator;
            }
            started = true;
            ret += str;
        }
        return ret;
    }

    public static String getSeparator( Component component ) {
        return component.getLocalizer().getString( StringUtils.LIST_SEPARATOR_KEY, component ) + " ";
    }
}
