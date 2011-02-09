/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.panels;

import java.io.Serializable;
import java.util.Random;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.model.Model;

import edu.colorado.phet.website.components.LinkImageWrapper;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.util.HtmlUtils;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.links.RawLinker;

public abstract class Sponsor implements Serializable {

    private String fullName;
    private String url; // null if there is no URL

    // defaults
    private boolean needsArticle = false;
    private boolean logoNeedsSubtitle = false;
    private double homeWeight = 0;
    private double simWeight = 0;

    protected abstract Component createLogoComponent( String id, String style );

    public Sponsor( String fullName, String url ) {
        this.fullName = fullName;
        this.url = url;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUrl() {
        // TODO: add linker!
        return url;
    }

    public double getHomeWeight() {
        return homeWeight;
    }

    public void setHomeWeight( double homeWeight ) {
        this.homeWeight = homeWeight;
    }

    public double getSimWeight() {
        return simWeight;
    }

    public void setSimWeight( double simWeight ) {
        this.simWeight = simWeight;
    }

    public boolean getNeedsArticle() {
        return needsArticle;
    }

    public boolean getLogoNeedsSubtitle() {
        return logoNeedsSubtitle;
    }

    public void setNeedsArticle( boolean needsArticle ) {
        this.needsArticle = needsArticle;
    }

    public void setLogoNeedsSubtitle( boolean logoNeedsSubtitle ) {
        this.logoNeedsSubtitle = logoNeedsSubtitle;
    }

    public static Sponsor HEWLETT_FOUNDATION = new LogoSponsor(
            "The William and Flora Hewlett Foundation",
            "http://www.hewlett.org/",
            Images.LOGO_HEWLETT ) {{
        setSimWeight( 2 );
    }};

    public static Sponsor NSF = new LogoSponsor(
            "The National Science Foundation",
            "http://www.nsf.gov/",
            Images.LOGO_NSF ) {{
        setSimWeight( 2 );
        setLogoNeedsSubtitle( true );
    }};

    public static Sponsor KSU = new LogoSponsor(
            "ERCSME at King Saud University",
            "http://www.ksu.edu.sa/",
            Images.LOGO_ECSME ) {{
        setSimWeight( 2 );
        setLogoNeedsSubtitle( true );
    }};

    public static Sponsor ODONNELL_FOUNDATION = new TextSponsor(
            "The O'Donnell Foundation",
            "http://www.odf.org/",
            "The O'Donnell Foundation", "font-size: 14px; padding: 0.3em 0; color: #000;" ) {{
        setSimWeight( 2 );
    }};

    public static Sponsor MORTENSON_FOUNDATION = new TextSponsor(
            "The Mortenson Family Foundation",
            null,
            "The Mortenson Family Foundation", "font-size: 14px; padding: 0.3em 0; color: #000;" ) {{
        setHomeWeight( 2 );
        setSimWeight( 2 );
    }};

    public static Random random = new Random();
    public static Sponsor[] ActiveSponsors = new Sponsor[]{
            HEWLETT_FOUNDATION,
            NSF,
            KSU,
            ODONNELL_FOUNDATION,
            MORTENSON_FOUNDATION
    };

    /**
     * @return Pick a random home sponsor by weight
     */
    public static Sponsor chooseRandomHomeSponsor() {
        double total = 0;
        for ( Sponsor sponsor : ActiveSponsors ) {
            total += sponsor.getHomeWeight();
        }
        double r = random.nextDouble() * total;
        for ( Sponsor sponsor : ActiveSponsors ) {
            r -= sponsor.getHomeWeight();
            if ( r <= 0 ) {
                return sponsor;
            }
        }
        throw new RuntimeException( "Failure with randomly picking a home sponsor" );
    }

    /**
     * @return Pick a random sim sponsor by weight
     */
    public static Sponsor chooseRandomSimSponsor() {
        double total = 0;
        for ( Sponsor sponsor : ActiveSponsors ) {
            total += sponsor.getSimWeight();
        }
        double r = random.nextDouble() * total; // r < total
        for ( Sponsor sponsor : ActiveSponsors ) {
            r -= sponsor.getSimWeight();
            if ( r <= 0 ) {
                return sponsor;
            }
        }
        throw new RuntimeException( "Failure with randomly picking a home sponsor" );
    }

    /**
     * Used for sponsors who have a logo
     */
    public static class LogoSponsor extends Sponsor {
        private String imageUrl;

        public LogoSponsor( String fullName, String url, String imageUrl ) {
            super( fullName, url );
            this.imageUrl = imageUrl;
        }

        @Override
        protected Component createLogoComponent( String id, String style ) {
            StaticImage image = new StaticImage( id, imageUrl, getFullName() + " logo" );
            image.add( new AttributeModifier( "style", true, new Model<String>( style ) ) );
            return image;
        }
    }

    /**
     * Used for sponsors who do not have a logo. Only text.
     */
    public static class TextSponsor extends Sponsor {
        private String text;
        private String textStyle;

        public TextSponsor( String fullName, String url, String text, String textStyle ) {
            super( fullName, url );
            this.text = text;
            this.textStyle = textStyle;
        }

        @Override
        protected Component createLogoComponent( String id, String style ) {
            RawLabel label = new RawLabel( id, "<span>" + HtmlUtils.encode( text ) + "</span>" );
            String combinedStyle = style + ( style.endsWith( ";" ) ? "" : ";" ) + textStyle;
            label.add( new AttributeModifier( "style", true, new Model<String>( combinedStyle ) ) );
            return label;
        }
    }

    public static Component createSponsorLogoPanel( String id, final Sponsor sponsor, final PageContext context, final String style ) {
        if ( sponsor.getUrl() != null ) {
            return new LinkImageWrapper( id, context, new RawLinker( sponsor.getUrl() ) ) {
                {
                    getLink().add( new AttributeModifier( "target", true, new Model<String>( "_blank" ) ) );
                }

                @Override
                public Component createChild( String id ) {
                    return sponsor.createLogoComponent( id, style );
                }
            };
        }
        else {
            return sponsor.createLogoComponent( id, style );
        }
    }
}
