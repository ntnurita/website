/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation.entities;

public class SocialEntity extends TranslationEntity {
    public SocialEntity() {
        addString( "social.facebook.tooltip" );
        addString( "social.twitter.tooltip" );
        addString( "social.stumbleupon.tooltip" );
        addString( "social.reddit.tooltip" );
        addString( "social.youtube.tooltip" );
        addString( "social.blog.tooltip" );
        addString( "social.newsletter.tooltip" );
        addString( "social.pinterest.tooltip" );

        addString( "social.homepage.facebook.tooltip" );
        addString( "social.homepage.twitter.tooltip" );
        addString( "social.homepage.youtube.tooltip" );
        addString( "social.homepage.blog.tooltip" );
        addString( "social.homepage.newsletter.tooltip" );
        addString( "social.homepage.pinterest.tooltip" );
    }

    public String getDisplayName() {
        return "Social";
    }
}
