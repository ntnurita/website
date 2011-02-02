/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.panels;

import java.util.Random;

import edu.colorado.phet.website.constants.Images;

public class Sponsor {

    private String nameKey;
    private String url;
    private String imageUrl;
    private String imageAlt;
    private boolean nameOnHomepage;
    private boolean needsArticle;

    public Sponsor( String nameKey, String url, String imageUrl, String imageAlt, boolean nameOnHomepage, boolean needsArticle ) {
        this.nameKey = nameKey;
        this.url = url;
        this.imageUrl = imageUrl;
        this.imageAlt = imageAlt;
        this.nameOnHomepage = nameOnHomepage;
        this.needsArticle = needsArticle;
    }

    public String getNameKey() {
        return nameKey;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public boolean showNameOnHomepage() {
        return nameOnHomepage;
    }

    public boolean needsArticle() {
        return needsArticle;
    }

    public static Sponsor HEWLETT_FOUNDATION = new Sponsor(
            "sponsors.hewlett.name",
            Images.LOGO_HEWLETT,
            "Hewlett Foundation logo",
            "Hewlett Foundation Logo",
            false,
            false );

    public static Sponsor NSF = new Sponsor(
            "sponsors.nsf.name",
            "http://www.nsf.gov/",
            Images.LOGO_NSF,
            "NSF logo",
            true,
            true );

    public static Sponsor KSU = new Sponsor(
            "sponsors.ksu.name",
            "http://www.ksu.edu.sa/",
            Images.LOGO_ECSME,
            "The King Saud (ESCME) Logo",
            true,
            false );

    public static Sponsor ODONNELL_FOUNDATION = new Sponsor(
            "sponsors.odonnell.name",
            "http://www.odf.org/",
            Images.LOGO_ODONNELL_LARGE,
            "O'Donnell Foundation logo",
            true,
            true );

    public static Random random = new Random();
    public static Sponsor[] ActiveSponsors = new Sponsor[]{HEWLETT_FOUNDATION, NSF, KSU, ODONNELL_FOUNDATION};

    public static Sponsor chooseRandomActiveSponsor() {
        return ActiveSponsors[random.nextInt( ActiveSponsors.length )];
    }

}
