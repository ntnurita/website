/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.constants;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import edu.colorado.phet.website.util.ImageHandle;
import edu.colorado.phet.website.util.links.RawLinker;

/**
 * Model of a social bookmarking service (like sharing for Facebook, tweeting links, or the more classic delicious /
 * digg models). They have two main things: an icon and a way to get a link to bookmark a specific URL.
 */
public abstract class SocialBookmarkService implements Serializable {
    private static int ICON_SIZE = 32;

    /**
     * @return Path to image icon. Relative from server root. Starts with slash
     */
    public abstract String getIconPath();

    public String getSpritePath() {
        return "/images/icons/social-sprite.png";
    }

    public abstract int getSpriteOffset();

    public String getFooterLabel() {
        return getName();
    }

    /**
     * Get the URL to use for bookmarking.
     *
     * @param relativeUrl Relative to server root, starts with slash
     * @param title       The default title for the bookmarking
     * @return Absolute URL
     * @throws UnsupportedEncodingException
     */
    public abstract String getShareUrl( String relativeUrl, String title ) throws UnsupportedEncodingException;

    public abstract String getName();

    public String getTooltipLocalizationKey() {
        return "social." + getName() + ".tooltip";
    }

    public String getHomePageTooltipLocalizationKey() {
        return "social.homepage." + getName() + ".tooltip";
    }

    public RawLinker getLinker( String relativeUrl, String title ) {
        try {
            return new RawLinker( getShareUrl( relativeUrl, title ) );
        }
        catch ( UnsupportedEncodingException e ) {
            throw new RuntimeException( e );
        }
    }

    public ImageHandle getIconHandle() {
        return new ImageHandle( getIconPath(), true );
    }

    public static String doubleEncode( String str ) throws UnsupportedEncodingException {
        return URLEncoder.encode( URLEncoder.encode( str, "UTF-8" ), "UTF-8" );
    }

    public static final SocialBookmarkService BLOG = new SocialBookmarkService() {
        @Override
        public String getIconPath() {
            return "/images/icons/social/new/blog-icon.svg";
        }

        @Override
        public int getSpriteOffset() {
            return ICON_SIZE * 0;
        }

        @Override
        public String getShareUrl( String relativeUrl, String title ) throws UnsupportedEncodingException {
            return "/blog";
        }

        @Override
        public String getName() {
            return "blog";
        }

        @Override
        public String getFooterLabel() {
            return "home.blogText";
        }
    };

    public static final SocialBookmarkService NEWSLETTER = new SocialBookmarkService() {
        @Override
        public String getIconPath() {
            return "/images/icons/social/new/mail-icon.svg";
        }

        @Override
        public int getSpriteOffset() {
            return ICON_SIZE * 1;
        }

        @Override
        public String getShareUrl( String relativeUrl, String title ) throws UnsupportedEncodingException {
            return ""; // this is is not a normal share URL, but a linker, so it is handled in SocialBookmarkPanel
        }

        @Override
        public String getName() {
            return "newsletter";
        }

        @Override
        public String getFooterLabel() {
            return "home.newsletterText";
        }
    };

    public static final SocialBookmarkService FACEBOOK = new SocialBookmarkService() {
        @Override
        public String getIconPath() {
            return "/images/icons/social/new/facebook.svg";
        }

        @Override
        public int getSpriteOffset() {
            return ICON_SIZE * 2;
        }

        @Override
        public String getShareUrl( String relativeUrl, String title ) throws UnsupportedEncodingException {
            if ( title == "home.title" ) {
                return "http://www.facebook.com/pages/PhET-Interactive-Simulations/161503243888932?v=wall";
            }
            return "http://www.facebook.com/sharer.php?u=http%3A%2F%2Fphet.colorado.edu" + URLEncoder.encode( relativeUrl, "UTF-8" ) + "&t=" + URLEncoder.encode( title, "UTF-8" );
        }

        @Override
        public String getName() {
            return "facebook";
        }

        @Override
        public String getFooterLabel() {
            return "home.facebookText";
        }
    };

    public static final SocialBookmarkService TWITTER = new SocialBookmarkService() {
        @Override
        public String getIconPath() {
            return "/images/icons/social/new/twitter.png";
        }

        @Override
        public int getSpriteOffset() {
            return ICON_SIZE * 4;
        }

        @Override
        public String getShareUrl( String relativeUrl, String title ) throws UnsupportedEncodingException {
            if ( title == "home.title" ) {
                return "http://twitter.com/PhETSims";
            }
            return "https://twitter.com/share?url=http%3A%2F%2Fphet.colorado.edu" + URLEncoder.encode( relativeUrl, "UTF-8" ) + "&text=" + URLEncoder.encode( title, "UTF-8" );
        }

        @Override
        public String getName() {
            return "twitter";
        }

        @Override
        public String getFooterLabel() {
            return "home.twitterText";
        }
    };

    public static final SocialBookmarkService STUMBLE_UPON = new SocialBookmarkService() {
        @Override
        public String getIconPath() {
            return "/images/icons/social/new/stumble-upon.png";
        }

        @Override
        public int getSpriteOffset() {
            return 32;
        }

        @Override
        public String getShareUrl( String relativeUrl, String title ) throws UnsupportedEncodingException {
            return "http://www.stumbleupon.com/submit?url=http%3A%2F%2Fphet.colorado.edu" + URLEncoder.encode( relativeUrl, "UTF-8" ) + "&title=" + URLEncoder.encode( title, "UTF-8" );
        }

        @Override
        public String getName() {
            return "stumbleupon";
        }

        @Override
        public String getFooterLabel() {
            return "stumble upon us";
        }
    };

    public static final SocialBookmarkService REDDIT = new SocialBookmarkService() {
        @Override
        public String getIconPath() {
            return "/images/icons/social/new/reddit.png";
        }

        @Override
        public int getSpriteOffset() {
            return 64;
        }

        @Override
        public String getShareUrl( String relativeUrl, String title ) throws UnsupportedEncodingException {
            return "http://www.reddit.com/login?dest=%2Fsubmit%3Furl%3Dhttp%3A%2F%2Fhttp%253A%252F%252Fphet.colorado.edu" + doubleEncode( relativeUrl ) + "%26title%3D" + doubleEncode( title );
        }

        @Override
        public String getName() {
            return "reddit";
        }
    };

    public static final SocialBookmarkService YOUTUBE = new SocialBookmarkService() {
        @Override
        public String getIconPath() {
            return "/images/icons/social/new/you-tube-circle.png";
        }

        @Override
        public int getSpriteOffset() {
            return ICON_SIZE * 3;
        }

        @Override
        public String getShareUrl( String relativeUrl, String title ) throws UnsupportedEncodingException {
            return "https://www.youtube.com/channel/UCMRZ0-ci4ifGBF1bJvrcDRQ";
        }

        @Override
        public String getName() {
            return "youtube";
        }

        @Override
        public String getFooterLabel() {
            return "home.youtubeText";
        }
    };

    public static final SocialBookmarkService PINTEREST = new SocialBookmarkService() {
        @Override
        public String getIconPath() {
            return "/images/icons/social/new/pinterest.png";
        }

        @Override
        public int getSpriteOffset() {
            return ICON_SIZE * 4;
        }

        @Override
        public String getShareUrl( String relativeUrl, String title ) throws UnsupportedEncodingException {
            return "http://www.pinterest.com/PhETSims";
        }

        @Override
        public String getName() {
            return "pinterest";
        }

        @Override
        public String getFooterLabel() {
            return "home.pinterestText";
        }
    };

    public static final List<SocialBookmarkService> HOMEPAGE_SERVICES = new LinkedList<SocialBookmarkService>();
    public static final List<SocialBookmarkService> SERVICES = new LinkedList<SocialBookmarkService>();

    static {
        HOMEPAGE_SERVICES.add( BLOG );
        HOMEPAGE_SERVICES.add( NEWSLETTER );
        HOMEPAGE_SERVICES.add( FACEBOOK );
        HOMEPAGE_SERVICES.add( YOUTUBE );
        HOMEPAGE_SERVICES.add( TWITTER );
        HOMEPAGE_SERVICES.add( PINTEREST );

        SERVICES.add( FACEBOOK );
        SERVICES.add( YOUTUBE );
        SERVICES.add( TWITTER );
        SERVICES.add( PINTEREST );
        SERVICES.add( STUMBLE_UPON );
        SERVICES.add( REDDIT );
    }
}
