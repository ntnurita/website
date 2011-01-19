/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.util;

/**
 * Used for building comma-separated value strings
 */
public class RawCSV {

    private StringBuilder str;
    private int columnsPrinted = 0;

    public RawCSV() {
        str = new StringBuilder();
    }

    public void addColumnValue( String value ) {
        if ( columnsPrinted > 0 ) {
            str.append( "," );
        }
        if ( value != null && value.length() >= 0 ) {
            // do proper CSV escaping of double-quotes. see http://en.wikipedia.org/wiki/Comma-separated_values
            str.append( "\"" ).append( value.replace( "\"", "\"\"" ) ).append( "\"" );
        }
        columnsPrinted++;
    }

    public void betweenLines() {
        if ( columnsPrinted > 0 ) {
            str.append( "\n" );
            columnsPrinted = 0;
        }
    }

    @Override
    public String toString() {
        return str.toString();
    }
}
