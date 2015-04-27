/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;

import edu.colorado.phet.common.phetcommon.util.PhetLocales;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

/**
 * Presents the user with a drop-down choice of locales. Should almost always be used from within a form, otherwise the
 * locale will not be updated properly (does not use AJAX)
 */
public class LocaleDropDownChoice extends PhetPanel {

    private Model selectedLocaleModel = null;

    public LocaleDropDownChoice( String id, PageContext context ) {
        this( id, context, WebsiteConstants.ENGLISH );
    }

    public LocaleDropDownChoice( String id, PageContext context, Locale defaultLocale ) {
        super( id, context );

        PhetLocales phetLocales = ( (PhetWicketApplication) getApplication() ).getSupportedLocales();

        List<LocaleItem> localeItems = new LinkedList<LocaleItem>();
        Model englishLocaleModel = null;

        for ( String name : phetLocales.getSortedNames() ) {
            Locale locale = phetLocales.getLocale( name );
            LocaleItem localeItem = new LocaleItem( locale, name );
            localeItems.add( localeItem );
            if ( locale.equals( defaultLocale ) ) {
                selectedLocaleModel = new Model( localeItem );
            }
            if ( locale.equals( WebsiteConstants.ENGLISH ) ) {
                englishLocaleModel = new Model( localeItem );
            }
        }

        // this will probably never happen, but just in case default to English if defaultLocale doesn't match a phetLocale
        if ( selectedLocaleModel == null ) {
            selectedLocaleModel = englishLocaleModel;
        }

        DropDownChoice localeChoice = new DropDownChoice( "locales", selectedLocaleModel, localeItems, new IChoiceRenderer() {
            public Object getDisplayValue( Object object ) {
                if ( object instanceof LocaleItem ) {
                    LocaleItem localeItem = (LocaleItem) object;
                    return localeItem.getDisplayValue() + " ( " + localeItem.getLocale().toLanguageTag() + " )";
                }
                else {
                    throw new RuntimeException( "Not an LocaleItem" );
                }
            }

            public String getIdValue( Object object, int index ) {
                if ( object instanceof LocaleItem ) {
                    return ( (LocaleItem) object ).getDisplayValue();
                }
                else {
                    throw new RuntimeException( "Not an LocaleItem" );
                }
            }
        } );
        add( localeChoice );

    }

    /**
     * Get the user-selected locale
     *
     * @return The user-selected locale
     */
    public Locale getLocale() {
        return ( (LocaleItem) selectedLocaleModel.getObject() ).getLocale();
    }

    private static class LocaleItem implements Serializable {

        private Locale locale;
        private String name;

        private LocaleItem( Locale locale, String name ) {
            this.locale = locale;
            this.name = name;
        }

        public Locale getLocale() {
            return locale;
        }

        public String getDisplayValue() {
            return name;
        }

        public Component getDisplayComponent( String id ) {
            return new Label( id, getDisplayValue() );
        }
    }
}
