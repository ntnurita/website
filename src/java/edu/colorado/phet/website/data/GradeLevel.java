/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.data;

/**
 * An abstract "grade level". Currenly best modeled by a larger category, and not by individual year-long grades
 */
public enum GradeLevel {
    ELEMENTARY_SCHOOL( 1, 5, 5, 11 ),
    MIDDLE_SCHOOL( 6, 8, 11, 14 ),
    HIGH_SCHOOL( 8, 12, 14, 18 ),
    UNIVERSITY( 13, 16, 18, 23 );

    private final int lowGradeNumber; // lowest grade level in GradeLevel. see http://nsdl.org/collection/educationLevel.php
    private final int highGradeNumber; // highest grade level in GradeLevel. see http://nsdl.org/collection/educationLevel.php
    private final int lowAge; // lowest developmental age for this GradeLevel. see https://secure.wikimedia.org/wikipedia/en/wiki/Education_in_the_United_States#School_grades
    private final int highAge; // highest developmental age for this GradeLevel. see https://secure.wikimedia.org/wikipedia/en/wiki/Education_in_the_United_States#School_grades

    GradeLevel( int lowGradeNumber, int highGradeNumber, int lowAge, int highAge ) {
        this.lowGradeNumber = lowGradeNumber;
        this.highGradeNumber = highGradeNumber;
        this.lowAge = lowAge;
        this.highAge = highAge;
    }

    /*---------------------------------------------------------------------------*
    * instance methods
    *----------------------------------------------------------------------------*/

    public int getLowGradeNumber() {
        return lowGradeNumber;
    }

    public int getHighGradeNumber() {
        return highGradeNumber;
    }

    public int getLowAge() {
        return lowAge;
    }

    public int getHighAge() {
        return highAge;
    }

    /*---------------------------------------------------------------------------*
    * static methods
    *----------------------------------------------------------------------------*/

    public static GradeLevel getLowestGradeLevel() {
        return ELEMENTARY_SCHOOL;
    }

    public static GradeLevel getHighestGradeLevel() {
        return UNIVERSITY;
    }

    /**
     * @param a Grade level A
     * @param b Grade level B
     * @return Whether A is a lower (younger) grade level
     */
    public static boolean isLowerGradeLevel( GradeLevel a, GradeLevel b ) {
        return a.lowGradeNumber < b.lowGradeNumber;
    }

    /**
     * Grade level from a category. If not a grade-level category, this will throw an exception
     *
     * @param category Category (Grade-level)
     * @return GradeLevel
     */
    public static GradeLevel getGradeLevelFromCategory( Category category ) {
        // TODO: make these names constants in Category!
        if ( category.getName().equals( Category.ELEMENTARY_SCHOOL ) ) {
            return ELEMENTARY_SCHOOL;
        }
        else if ( category.getName().equals( Category.MIDDLE_SCHOOL ) ) {
            return MIDDLE_SCHOOL;
        }
        else if ( category.getName().equals( Category.HIGH_SCHOOL ) ) {
            return HIGH_SCHOOL;
        }
        else if ( category.getName().equals( Category.UNIVERSITY ) ) {
            return UNIVERSITY;
        }
        else {
            throw new RuntimeException( "unknown grade level for category: " + category.toString() );
        }
    }
}
