package edu.colorado.phet.website.util;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import edu.colorado.phet.website.PhetWicketApplication;

/**
 * Sorts string forms of objects with locale-specific information
 */

public abstract class StringComparator<T> implements Comparator<T> {

    private final Comparator<Object> collator;

    protected StringComparator() {
        this( PhetWicketApplication.getDefaultLocale() );
    }

    protected StringComparator( Locale locale ) {
        collator = Collator.getInstance( locale );
    }

    public abstract String toString( T ob );

    public int compare( T a, T b ) {
        return collator.compare( toString( a ), toString( b ) );
    }
}
