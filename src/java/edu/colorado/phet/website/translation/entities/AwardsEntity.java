// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.website.translation.entities;

public class AwardsEntity extends TranslationEntity {
    public AwardsEntity() {
        addString( "award.techAward2011.homeTitle" );
        addString( "award.techAward2011.homeSubtitle" );
        addString( "award.techAward2011.title" );
    }

    @Override public String getDisplayName() {
        return "Awards";
    }
}
