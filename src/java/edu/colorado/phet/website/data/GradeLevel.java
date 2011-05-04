/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.data;

/**
 * An abstract "grade level". Currenly best modeled by a larger category, and not by individual year-long grades
 */
public enum GradeLevel {

    // NOTE: keep the same order! The natural ordering is used in grade-level comparison
    ELEMENTARY_SCHOOL,
    MIDDLE_SCHOOL,
    HIGH_SCHOOL,
    UNIVERSITY;

    public static GradeLevel getLowestGradeLevel() {
        return ELEMENTARY_SCHOOL;
    }

    public static GradeLevel getHighestGradeLevel() {
        return UNIVERSITY;
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
