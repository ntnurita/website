/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.panels;

import java.util.Random;

import edu.colorado.phet.website.constants.Images;

public class Sponsor {

    private String fullName;
    private String url; // null if there is no URL
    private String imageUrl; // null if there is no logo
    private String imageAlt;
    private boolean nameOnHomepage;
    private boolean needsArticle;

    public Sponsor( String fullName, String url, String imageUrl, String imageAlt, boolean nameOnHomepage, boolean needsArticle, double homeWeight, double simWeight ) {
        this.fullName = fullName;
        this.url = url;
        this.imageUrl = imageUrl;
        this.imageAlt = imageAlt;
        this.nameOnHomepage = nameOnHomepage;
        this.needsArticle = needsArticle;
    }

    public String getFullName() {
        return fullName;
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
            "The William and Flora Hewlett Foundation",
            "http://www.hewlett.org/",
            Images.LOGO_HEWLETT,
            "Hewlett Foundation Logo",
            false,
            false, 0, 2 );

    public static Sponsor NSF = new Sponsor(
            "The National Science Foundation",
            "http://www.nsf.gov/",
            Images.LOGO_NSF,
            "NSF logo",
            true,
            true, 0, 2 );

    public static Sponsor KSU = new Sponsor(
            "ERCSME at King Saud University",
            "http://www.ksu.edu.sa/",
            Images.LOGO_ECSME,
            "The King Saud (ESCME) Logo",
            true,
            false, 0, 2 );

    public static Sponsor ODONNELL_FOUNDATION = new Sponsor(
            "The O'Donnell Foundation",
            "http://www.odf.org/",
            Images.LOGO_ODONNELL_LARGE,
            "O'Donnell Foundation logo",
            true,
            true, 0, 2 );

    public static Sponsor MORTENSON_FOUNDATION = new Sponsor(
            "The Mortenson Family Foundation",
            null,
            null,
            null,
            true,
            false, 2, 2 );

    public static Random random = new Random();
    public static Sponsor[] ActiveSponsors = new Sponsor[]{HEWLETT_FOUNDATION, NSF, KSU, ODONNELL_FOUNDATION};

    public static Sponsor chooseRandomActiveSponsor() {
        return ActiveSponsors[random.nextInt( ActiveSponsors.length )];
    }

}
