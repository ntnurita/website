/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.panels.sponsor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.model.Model;

import edu.colorado.phet.website.components.LinkImageWrapper;
import edu.colorado.phet.website.components.LinkWrapper;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;
import edu.colorado.phet.website.util.attributes.ClassAppender;
import edu.colorado.phet.website.util.HtmlUtils;
import edu.colorado.phet.website.util.ImageHandle;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.WebImage;
import edu.colorado.phet.website.util.links.RawLinker;

/**
 * Represents one of our sponsors, and has a way of creating a logo
 */
public abstract class Sponsor implements Serializable {

    private String fullName;
    private String url; // null if there is no URL

    // defaults
    private boolean needsArticle = false;
    private boolean logoNeedsSubtitle = false;
    private double homeWeight = 0;
    private double simWeight = 0;

    protected abstract Component createLogoComponent( String id, String style, PageContext context, SponsorContext sponsorContext );

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
            "The O'Donnell Foundation", "font-size: 14px; padding: 0.3em 0; color: #000; text-decoration: none;" ) {{
        setSimWeight( 2 );
    }};

    public static Sponsor MORTENSON_FOUNDATION = new TextSponsor(
            "The Mortenson Family Foundation",
            null,
            "The Mortenson Family Foundation", "font-size: 14px; padding: 0.3em 0; color: #000; text-decoration: none;" ) {{
        setHomeWeight( 2 );
        setSimWeight( 2 );
    }};

    public static Sponsor JILA = new LogoSponsor(
            "JILA",
            "http://jila.colorado.edu/",
            Images.LOGO_JILA, "padding: 4px;" ) {{ // we need extra padding for the JILA logo
        setHomeWeight( 3 );
        setSimWeight( 2 );
    }};

    public static Sponsor TECH_SMITH = new LogoSponsor(
            "TechSmith",
            "http://www.techsmith.com/",
            Images.LOGO_TECHSMITH ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor BITROCK = new LogoSponsor(
            "BitRock",
            "http://bitrock.com/",
            Images.LOGO_BITROCK ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor JET_BRAINS = new LogoSponsor(
            "JetBrains",
            "http://www.jetbrains.com/",
            Images.LOGO_JETBRAINS ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor EJ_TECHNOLOGIES = new LogoSponsor(
            "ej-technologies",
            "http://www.ej-technologies.com/",
            Images.LOGO_EJTECHNOLOGIES, "padding: 4px;" ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor IDAHO_DIGITAL_LEARNING = new LogoSponsor(
            "Idaho Digital Learning",
            "http://www.idahodigitallearning.org/",
            Images.LOGO_IDAHO_DIGITAL_LEARNING, "padding: 8px;" ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor COMPASS_LEARNING = new LogoSponsor(
            "CompassLearning",
            "http://www.compasslearning.com/",
            Images.LOGO_COMPASS_LEARNING, "padding: 5px;" ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor LEARNING_COM = new LogoSponsor(
            "Learning.com",
            "http://www.learning.com/",
            Images.LOGO_LEARNING_COM, "padding: 5px;" ) {{
        setHomeWeight( 2 );
        setSimWeight( 2 );
    }};

    public static Sponsor WIRELESS_GENERATION = new LogoSponsor(
            "Wireless Generation",
            "http://www.wirelessgeneration.com/",
            Images.LOGO_WIRELESS_GENERATION, "padding: 3px;" ) {{
        setHomeWeight( 2 );
        setSimWeight( 2 );
    }};

    public static Sponsor OREGON_DEPARTMENT_OF_EDUCATION = new LogoSponsor(
            "Oregon Department of Education",
            "http://www.ode.state.or.us",
            Images.LOGO_OREGON_DOE, "padding: 5px;" ) {{
        setHomeWeight( 3 );
        setSimWeight( 3 );
    }};

    public static Sponsor JEFFERSON_COUNTY_VLA = new LogoSponsor(
            "Jefferson County Virtual Learning Academy",
            "http://www.jcesc.k12.oh.us/",
            Images.LOGO_JEFFERSON_COUNTY_VLA, "padding: 5px;" ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor CAROLINA_CORP = new LogoSponsor(
            "Carolina Biological Supply Company",
            "http://www.carolina.com",
            Images.LOGO_CAROLINA_CORP, "padding: 0px;" ) {{
        setHomeWeight( 2 );
        setSimWeight( 2 );
    }};

    public static Sponsor KANG_HSUAN = new LogoSponsor(
            "Kang Hsuan Educational Publishing Group",
            "http://www.knsh.com.tw/",
            Images.LOGO_KANG_HSUAN, "padding: 2px;" ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor PEARSON = new LogoSponsor(
            "Pearson",
            "http://www.pearsoned.com/",
            Images.LOGO_PEARSON, "padding: 0px;" ) {{
        setHomeWeight( 5 );
        setSimWeight( 5 );
    }};

    public static Sponsor PAN_AMERICA = new LogoSponsor(
            "Pan America Construction",
            "http://www.pancopty.com/",
            Images.LOGO_PAN_AMERICA, "padding: 0px;" ) {{
        setHomeWeight( 5 );
        setSimWeight( 5 );
    }};

    public static Sponsor TEXAS_INSTRUMENTS = new LogoSponsor(
            "Texas Instruments",
            "http://education.ti.com/",
            Images.LOGO_TEXAS_INSTRUMENTS, "padding: 2px;" ) {{
        setHomeWeight( 5 );
        setSimWeight( 5 );
    }};

    public static Sponsor MCGRAW_HILL = new LogoSponsor(
            "McGraw-Hill Education",
            "http://www.mheonline.com/",
            Images.LOGO_MCGRAW_HILL, "padding: 2px;" ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor HARK = new LogoSponsor(
            "Hark",
            "http://www.hark.com/",
            Images.LOGO_HARK, "padding: 5px;" ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor ZANICHELLI = new LogoSponsor(
            "Zanichelli",
            "http://www.zanichelli.it",
            Images.LOGO_ZANICHELLI, "padding: 2px;" ) {{
        setHomeWeight( 2 );
        setSimWeight( 2 );
    }};

    public static Sponsor CYBERNETYX = new LogoSponsor(
            "Cybernetyx",
            "http://www.cybernetyx.com/",
            Images.LOGO_CYBERNETYX, "padding: 5px;" ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor GIANCOLI = new LogoSponsor(
            "Giancoli Answers",
            "http://www.giancolianswers.com/",
            Images.LOGO_GIANCOLI, "padding: 5px;" ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor CENGAGE = new LogoSponsor(
            "Cengage Learning",
            "http://www.cengage.com",
            Images.LOGO_CENGAGE, "padding: 5px;" ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor CNEC = new LogoSponsor(
            "Campanha Nacional de Escolas da Comunidade",
            "http://www.noas.com.br/",
            Images.LOGO_CNEC, "padding: 5px;" ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor WHRO = new LogoSponsor(
            "WHRO Education",
            "http://education.whro.org/",
            Images.LOGO_WHRO, "padding: 5px;" ) {{
        setHomeWeight( 1 );
        setSimWeight( 1 );
    }};

    public static Sponsor NDLA = new LogoSponsor(
            "Norwegian Digital Learning Arena",
            "http://ndla.no/en",
            Images.LOGO_NDLA, "padding: 0px;" ) {{
        setHomeWeight( 5 );
        setSimWeight( 5 );
    }};

    public static Sponsor YOU = new YouSponsor(
            "you",
            null ) {{
        setHomeWeight( 3 );
        setSimWeight( 3 );
    }};

    public static Random random = new Random();

    // NOTE: on adding a sponsor, do we need to update the installer to rip the image?
    public static Sponsor[] ActiveSponsors = new Sponsor[] {
            HEWLETT_FOUNDATION,
            NSF,
            KSU,
            ODONNELL_FOUNDATION,
//            MORTENSON_FOUNDATION,
            JILA,
            TECH_SMITH,
            BITROCK,
            JET_BRAINS,
            EJ_TECHNOLOGIES,
            IDAHO_DIGITAL_LEARNING,
            COMPASS_LEARNING,
            LEARNING_COM,
            WIRELESS_GENERATION,
            OREGON_DEPARTMENT_OF_EDUCATION,
            JEFFERSON_COUNTY_VLA,
            CAROLINA_CORP,
            KANG_HSUAN,
            PEARSON,
            PAN_AMERICA,
            TEXAS_INSTRUMENTS,
            MCGRAW_HILL,
            HARK,
            ZANICHELLI,
            CYBERNETYX,
//            GIANCOLI, // removed at request Nov 5 2012, with donation refund?
            CENGAGE,
            CNEC,
            WHRO,
            NDLA,
            YOU
    };

    /**
     * @return All sponsors with non-zero home weight
     */
    public static List<Sponsor> getHomeSponsors() {
        List<Sponsor> ret = new LinkedList<Sponsor>();
        for ( Sponsor sponsor : ActiveSponsors ) {
            if ( sponsor.getHomeWeight() > 0 ) {
                ret.add( sponsor );
            }
        }
        return ret;
    }

    /**
     * @return All sponsors with non-zero sim weight
     */
    public static List<Sponsor> getSimSponsors() {
        List<Sponsor> ret = new LinkedList<Sponsor>();
        for ( Sponsor sponsor : ActiveSponsors ) {
            if ( sponsor.getSimWeight() > 0 ) {
                ret.add( sponsor );
            }
        }
        return ret;
    }

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
        private ImageHandle imageHandle;
        private String imageStyle = "";

        public LogoSponsor( String fullName, String url, ImageHandle imageHandle ) {
            super( fullName, url );
            this.imageHandle = imageHandle;
        }

        public LogoSponsor( String fullName, String url, ImageHandle imageHandle, String style ) {
            this( fullName, url, imageHandle );
            this.imageStyle = style;
        }

        @Override
        protected Component createLogoComponent( String id, String style, PageContext context, SponsorContext sponsorContext ) {
            StaticImage image = new StaticImage( id, WebImage.get( imageHandle ), getFullName() + " logo" );
            String combinedStyle = style + ( style.endsWith( ";" ) ? "" : ";" ) + imageStyle;
            image.add( new AttributeModifier( "style", true, new Model<String>( combinedStyle ) ) );
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
        protected Component createLogoComponent( String id, String style, PageContext context, SponsorContext sponsorContext ) {
            RawLabel label = new RawLabel( id, "<span>" + HtmlUtils.encode( text ) + "</span>" );
            String combinedStyle = style + ( style.endsWith( ";" ) ? "" : ";" ) + textStyle;
            label.add( new AttributeModifier( "style", true, new Model<String>( combinedStyle ) ) );
            return label;
        }
    }

    public static class YouSponsor extends Sponsor {
        public YouSponsor( String fullName, String url ) {
            super( fullName, url );
            // NOTE: change the url manually on the sponsor panel!
        }

        @Override
        protected Component createLogoComponent( String id, String style, PageContext context, SponsorContext sponsorContext ) {
            YouSponsorPanel panel = new YouSponsorPanel( id, context, sponsorContext );
            String combinedStyle = style + ( style.endsWith( ";" ) ? "" : ";" ) + "border: none;background-color: transparent;";
            panel.add( new AttributeModifier( "style", true, new Model<String>( combinedStyle ) ) );
            return panel;
        }
    }

    public static Component createSponsorLogoPanel( String id, final Sponsor sponsor, final PageContext context, final String style, final SponsorContext sponsorContext ) {
        if ( sponsor.getUrl() != null ) {
            if ( sponsor instanceof LogoSponsor ) {
                return new LinkImageWrapper( id, context, new RawLinker( sponsor.getUrl() ) ) {
                    {
                        getLink().add( new ClassAppender( "external" ) )
                                .add( new AttributeModifier( "rel", true, new Model<String>( "nofollow" ) ) );
                    }

                    @Override
                    public Component createChild( String id ) {
                        return sponsor.createLogoComponent( id, style, context, sponsorContext );
                    }
                };
            }
            else {
                return new LinkWrapper( id, context, new RawLinker( sponsor.getUrl() ) ) {
                    {
                        getLink().add( new ClassAppender( "external" ) )
                                .add( new AttributeModifier( "rel", true, new Model<String>( "nofollow" ) ) );
                    }

                    @Override
                    public Component createChild( String id ) {
                        return sponsor.createLogoComponent( id, style, context, sponsorContext );
                    }
                };
            }
        }
        else {
            return sponsor.createLogoComponent( id, style, context, sponsorContext );
        }
    }

    public static enum SponsorContext {
        HOME,
        SIM
    }
}
