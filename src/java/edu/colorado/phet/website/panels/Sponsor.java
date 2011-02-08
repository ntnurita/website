/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.panels;

import java.util.Random;

import org.apache.wicket.Component;

import edu.colorado.phet.website.components.StaticImage;
import edu.colorado.phet.website.constants.Images;

public abstract class Sponsor {

    private String fullName;
    private String url; // null if there is no URL

    // defaults
    private boolean needsArticle = false;
    private boolean logoNeedsSubtitle = false;
    private double homeWeight = 0;
    private double simWeight = 0;

    protected abstract Component createLogoComponent( String id );

    public Sponsor( String fullName, String url ) {
        this.fullName = fullName;
        this.url = url;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUrl() {
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

    public static Sponsor ODONNELL_FOUNDATION = new LogoSponsor(
            "The O'Donnell Foundation",
            "http://www.odf.org/",
            Images.LOGO_ODONNELL_LARGE ) {{
        setSimWeight( 2 );
    }};

    public static Sponsor MORTENSON_FOUNDATION = new LogoSponsor(
            "The Mortenson Family Foundation",
            null,
            null ) {{
        setHomeWeight( 2 );
        setSimWeight( 2 );
    }};

    public static Random random = new Random();
    public static Sponsor[] ActiveSponsors = new Sponsor[]{HEWLETT_FOUNDATION, NSF, KSU, ODONNELL_FOUNDATION};

    public static Sponsor chooseRandomActiveSponsor() {
        return ActiveSponsors[random.nextInt( ActiveSponsors.length )];
    }

    public static class LogoSponsor extends Sponsor {
        private String imageUrl;

        public LogoSponsor( String fullName, String url, String imageUrl ) {
            super( fullName, url );
            this.imageUrl = imageUrl;
        }

        @Override
        protected Component createLogoComponent( String id ) {
            return new StaticImage( id, imageUrl, getFullName() + " logo" );
        }
    }
}
