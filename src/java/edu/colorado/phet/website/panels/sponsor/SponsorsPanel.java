/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.panels.sponsor;

import edu.colorado.phet.website.cache.EventDependency;
import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.content.about.AboutSponsorsPanel;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.data.util.HibernateEventListener;
import edu.colorado.phet.website.data.util.IChangeListener;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

/**
 * The sponsor panel that is seen on the left side under the left navigation.
 */
public class SponsorsPanel extends PhetPanel {
    public SponsorsPanel( String id, final PageContext context ) {
        super( id, context );

        add( AboutSponsorsPanel.getLinker().getLink( "sponsors-link", context, getPhetCycle() ) );

        add( new StaticImage( "hewlett-logo", Images.LOGO_HEWLETT, "The Hewlett Logo" ) );
        add( new StaticImage( "nsf-logo", Images.LOGO_NSF, "The NSF Logo" ) );
        add( new StaticImage( "ecsme-logo", Images.LOGO_ECSME, "The King Saud (ESCME) Logo" ) );

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
