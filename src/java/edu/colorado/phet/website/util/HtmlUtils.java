/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.util;

public class HtmlUtils {

    /**
     * Encodes a string into an HTML-escaped version
     *
     * @param s String to encode
     * @return HTML encoded response
     */
    public static String encode( String s ) {
        if ( s == null ) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        int len = s.length();

        for ( int i = 0; i < len; i++ ) {
            char c = s.charAt( i );
            switch( c ) {
                case '&':
                    buf.append( "&amp;" );
                    break;
                case '<':
                    buf.append( "&lt;" );
                    break;
                case '>':
                    buf.append( "&gt;" );
                    break;
                case '"':
                    buf.append( "&quot;" );
                    break;
                case '\'':
                    buf.append( "&#39;" ); // apostrophe, but changed so that innerHTML in IE will properly be unescaped! see http://stackoverflow.com/questions/3611468/how-to-fix-innerhtml-on-ie-not-rendering-entities-with-html5-doctype
                    break;
                default:
                    buf.append( c );
            }
        }
        return buf.toString();
    }

    /**
     * Encodes a string into an HTML-escaped version (attribute version, skips ampersands).
     * <p/>
     * Escaping other things is just to appease the Nessus monster. This will cause jibberish for <>'s.
     * <p/>
     * NOTE: Not suitable for encoding if JS is going to be involved!
     *
     * @param s String to encode
     * @return HTML encoded response
     */
    public static String encodeForAttribute( String s ) {
        if ( s == null ) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        int len = s.length();

        for ( int i = 0; i < len; i++ ) {
            char c = s.charAt( i );
            switch( c ) {
                case '<':
                    buf.append( "&lt;" );
                    break;
                case '>':
                    buf.append( "&gt;" );
                    break;
                case '"':
                    buf.append( "&quot;" );
                    break;
                case '\'':
                    buf.append( "&apos;" );
                    break;
                default:
                    buf.append( c );
            }
        }
        return buf.toString();
    }

    public static String sanitizeId( String id ) {
        return id.replace( '.', '-' );
    }
}
