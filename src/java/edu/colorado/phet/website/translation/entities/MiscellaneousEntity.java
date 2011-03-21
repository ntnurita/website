/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation.entities;

import edu.colorado.phet.website.translation.PhetLocalizer;

public class MiscellaneousEntity extends TranslationEntity {
    public MiscellaneousEntity() {
        addString( "listSeparator", "Used to separate items in lists. Used in places like lists of keywords." );
        addString( "form.submit", "Generic submit form button label" );
        addString( "page.SkipToMainContent", "Text shown to accessibility devices to jump down to the main page content" );
        addString( "changelog.title" );
        addString( "changelog.header" );
        addString( "changelog.raw" );
        addString( PhetLocalizer.DROP_DOWN_CHOICE_NULL_KEY, "Text shown on drop-down boxes when one choice needs to be selected" );
    }

    public String getDisplayName() {
        return "Misc";
    }
}