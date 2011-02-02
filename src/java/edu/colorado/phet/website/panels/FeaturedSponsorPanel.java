/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.panels;

import edu.colorado.phet.website.cache.EventDependency;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.components.RawLink;
import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.data.util.HibernateEventListener;
import edu.colorado.phet.website.data.util.IChangeListener;
import edu.colorado.phet.website.util.PageContext;

public class FeaturedSponsorPanel extends PhetPanel {
    public FeaturedSponsorPanel( String id, final PageContext context ) {
        super( id, context );

        final Sponsor sponsor = Sponsor.chooseRandomActiveSponsor();
        if ( sponsor.showNameOnHomepage() ) {
            add( new LocalizedText( "featured-sponsor-name", sponsor.getNameKey() ) );
        }
        else {
            add( new InvisibleComponent( "featured-sponsor-name" ) );
        }
        add( new RawLink( "featured-sponsor-logo-link", sponsor.getUrl() ) {{
            add( new StaticImage( "logo", sponsor.getImageUrl(), sponsor.getImageAlt() ) );
        }} );

        addDependency( new EventDependency() {

            private IChangeListener stringListener;

            @Override
            protected void addListeners() {
                stringListener = createTranslationChangeInvalidator( context.getLocale() );
                HibernateEventListener.addListener( TranslatedString.class, stringListener );
            }

            @Override
            protected void removeListeners() {
                HibernateEventListener.removeListener( TranslatedString.class, stringListener );
            }
        } );
    }
}
