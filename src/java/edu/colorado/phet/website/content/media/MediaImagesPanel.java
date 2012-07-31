// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.content.media;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import edu.colorado.phet.website.components.RawLink;
import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.attributes.ClassAppender;
import edu.colorado.phet.website.util.ImageHandle;
import edu.colorado.phet.website.util.PageContext;

/**
 * Images and logos for use by the media
 */
public class MediaImagesPanel extends PhetPanel {

    private static final List<LogoImage> LOGO_IMAGES = new ArrayList<LogoImage>() {{
        add( new LogoImage( "PhET-logo.jpg", "PhET logo (high-res)" ) );
        add( new LogoImage( "PhET-team-9-9-2011.jpg", "PhET team photo (high-res)" ) );

        add( new LogoImage( "Acid-Base_Solutions.jpg", "Acid-Base Solutions" ) );
        add( new LogoImage( "AlphaDecay.jpg", "Alpha Decay" ) );
        add( new LogoImage( "Balancing_Chemical_Equations.jpg", "Balancing Chemical Equations" ) );
        add( new LogoImage( "Bending_Light.jpg", "Bending Light" ) );
        add( new LogoImage( "Build_a_Molecule.jpg", "Build a Molecule" ) );
        add( new LogoImage( "Build_an_Atom.jpg", "Build an Atom" ) );
        add( new LogoImage( "Buoyancy.jpg", "Buoyancy" ) );
        add( new LogoImage( "Capacitor_Lab.jpg", "Capacitor Lab" ) );
        add( new LogoImage( "CircuitConstructionKit.jpg", "Circuit Construction Kit" ) );
        add( new LogoImage( "EnergySkatePark.jpg", "Energy Skate Park" ) );
        add( new LogoImage( "Forces_and_Motion.jpg", "Forces and Motion" ) );
        add( new LogoImage( "GasProperties.jpg", "Gas Properties" ) );
        add( new LogoImage( "Generator.jpg", "Generator" ) );
        add( new LogoImage( "Gravity_and_Orbits.jpg", "Gravity and Orbits" ) );
        add( new LogoImage( "Isotopes_and_Atomic_Mass.jpg", "Isotopes and Atomic Mass" ) );
        add( new LogoImage( "MassesAndSprings.jpg", "Masses and Springs" ) );
        add( new LogoImage( "Membrane_Channels.jpg", "Membrane Channels" ) );
        add( new LogoImage( "Molecules_and_Light.jpg", "Molecules and Light" ) );
        add( new LogoImage( "Neuron.jpg", "Neuron" ) );
        add( new LogoImage( "OpticalTweezers.jpg", "Optical Tweezers" ) );
        add( new LogoImage( "ProjectileMotion.jpg", "Projectile Motion" ) );
        add( new LogoImage( "Reactants_Products_and_Leftovers.jpg", "Reactants, Products and Leftovers" ) );
        add( new LogoImage( "SaltsAndSolubility.jpg", "Salts and Solubility" ) );
    }};

    public MediaImagesPanel( String id, PageContext context ) {
        super( id, context );

        add( new ListView<LogoImage>( "images", LOGO_IMAGES ) {
            @Override protected void populateItem( ListItem<LogoImage> item ) {
                final LogoImage image = item.getModelObject();
                item.add( new RawLink( "link", image.fullImageUrl ) {{
                    add( new StaticImage( "image", image.thumbnail, image.description ) );
                }} );
                item.add( new Label( "description", image.description ) );
                if ( LOGO_IMAGES.indexOf( image ) % 2 == 0 ) {
                    item.add( new ClassAppender( "image-logo-clear" ) );
                }
            }
        } );
    }

    private static class LogoImage {
        public final ImageHandle thumbnail;
        public final String fullImageUrl;
        public final String description;

        private LogoImage( String filename, String description ) {
            thumbnail = new ImageHandle( "/files/media-images/thumbs/" + filename, false );
            fullImageUrl = "/files/media-images/" + filename;
            this.description = description;
        }
    }
}