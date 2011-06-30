/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.constants.Linkers;
import edu.colorado.phet.website.data.util.IntId;
import edu.colorado.phet.website.util.WebImage;

public class Simulation implements Serializable, IntId {
    private int id;
    private String name;
    private Project project;

    private int kilobytes;
    private Set localizedSimulations = new HashSet();
    private Set categories = new HashSet();
    private Set contributions = new HashSet();

    private List topics = new LinkedList();
    private List keywords = new LinkedList();

    private List relatedSimulations = new LinkedList();

    private String designTeam;
    private String libraries;
    private String thanksTo;

    private boolean underConstruction;
    private boolean guidanceRecommended;
    private boolean classroomTested;
    private boolean simulationVisible;
    private boolean hasCreativeCommonsAttributionLicense;

    /**
     * What the simulation ID was on the old PHP site. Kept for redirections, etc.
     */
    private int oldId;

    private Date createTime; // when simulation was uploaded initially to the server
    private Date updateTime; // whenever something about the simulation object is updated. doesn't mean the sim itself changed, but possibly metadata

    private GradeLevel lowGradeLevel; // typical low-end grade level for usage
    private GradeLevel highGradeLevel; // typical high-end grade level for usage

    public static final String DEFAULT_DESCRIPTION = "Description coming soon";
    public static final String DEFAULT_LEARNING_GOALS = "Learning goals coming soon";

    public Simulation() {
    }

    public String getDescriptionKey() {
        return "simulation." + name + ".description";
    }

    public String getLearningGoalsKey() {
        return "simulation." + name + ".learningGoals";
    }

    /**
     * Returns the best matching localized simulation.
     * <p/>
     * Note: should be in a Hibernate session, so localizedSimulations is instantiated
     *
     * @param bestLocale
     * @return
     */
    public LocalizedSimulation getBestLocalizedSimulation( Locale bestLocale ) {
        LocalizedSimulation englishSimulation = null;
        LocalizedSimulation languageSimulation = null;
        Locale englishLocale = LocaleUtils.stringToLocale( "en" );
        Locale languageLocale = LocaleUtils.stringToLocale( bestLocale.getLanguage() );
        for ( Object localizedSimulation : localizedSimulations ) {
            LocalizedSimulation sim = (LocalizedSimulation) localizedSimulation;
            if ( sim.getLocale().equals( bestLocale ) ) {
                return sim;
            }
            else if ( sim.getLocale().equals( englishLocale ) ) {
                englishSimulation = sim;
            }
            else if ( sim.getLocale().equals( languageLocale ) ) {
                languageSimulation = sim;
            }
        }
        return languageSimulation != null ? languageSimulation : englishSimulation;
    }

    public LocalizedSimulation getEnglishSimulation() {
        return getBestLocalizedSimulation( LocaleUtils.stringToLocale( "en" ) );
    }

    /**
     * @return A relative URL for the thumbnail
     */
    public String getThumbnailUrl() {
        // NOTE: this is relied upon to be relative!
        return "/sims/" + getProject().getName() + "/" + getName() + "-thumbnail.jpg";
    }

    public String getImageUrl() {
        return "/sims/" + getProject().getName() + "/" + getName() + "-screenshot.png";
    }

    public WebImage getThumbnail() {
        return WebImage.get( getThumbnailUrl(), false );
    }

    public WebImage getImage() {
        return WebImage.get( getImageUrl(), false );
    }

    public int detectSimKilobytes( File docRoot ) {
        File projectRoot = project.getProjectRoot( docRoot );
        switch( getType() ) {
            case Project.TYPE_JAVA:
                return (int) ( new File( projectRoot, project.getName() + "_all.jar" ) ).length() / 1000;
            case Project.TYPE_FLASH:
                return (int) ( new File( projectRoot, name + "_en.jar" ) ).length() / 1024;
            default:
                throw new RuntimeException( "Simulation type not handled? type = " + getType() );
        }
    }

    public static File getAllJar( File docRoot, String project ) {
        return new File( docRoot, "sims/" + project + "/" + project + "_all.jar" );
    }

    public static File getLocalizedJar( File docRoot, String project, String sim, Locale locale ) {
        return new File( docRoot, "sims/" + project + "/" + sim + "_" + LocaleUtils.localeToString( locale ) + ".jar" );
    }

    public boolean isJava() {
        return getType() == 0;
    }

    public boolean isFlash() {
        return getType() == 1;
    }

    public GradeLevel getMinGradeLevel() {
        boolean foundGradeLevelCategory = false;
        GradeLevel result = GradeLevel.getHighestGradeLevel();
        for ( Object o : categories ) {
            Category category = (Category) o;
            if ( category.isGradeLevelCategory() ) {
                foundGradeLevelCategory = true;
                GradeLevel level = GradeLevel.getGradeLevelFromCategory( category );
                if ( GradeLevel.isLowerGradeLevel( level, result ) ) {
                    result = level;
                }
            }
        }

        // if we have no grade level information, include everything
        if ( !foundGradeLevelCategory ) {
            return GradeLevel.getLowestGradeLevel();
        }
        return result;
    }

    public GradeLevel getMaxGradeLevel() {
        boolean foundGradeLevelCategory = false;
        GradeLevel result = GradeLevel.getLowestGradeLevel();
        for ( Object o : categories ) {
            Category category = (Category) o;
            if ( category.isGradeLevelCategory() ) {
                foundGradeLevelCategory = true;
                GradeLevel level = GradeLevel.getGradeLevelFromCategory( category );
                if ( GradeLevel.isLowerGradeLevel( result, level ) ) {
                    result = level;
                }
            }
        }

        // if we have no grade level information, include everything
        if ( !foundGradeLevelCategory ) {
            return GradeLevel.getHighestGradeLevel();
        }
        return result;
    }

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public int getType() {
        return project.getType();
    }

    public Project getProject() {
        return project;
    }

    public void setProject( Project project ) {
        this.project = project;
    }

    public Set getLocalizedSimulations() {
        return localizedSimulations;
    }

    public void setLocalizedSimulations( Set localizedSimulations ) {
        this.localizedSimulations = localizedSimulations;
    }

    public Set getCategories() {
        return categories;
    }

    public void setCategories( Set categories ) {
        this.categories = categories;
    }

    public List getKeywords() {
        return keywords;
    }

    public void setKeywords( List keywords ) {
        this.keywords = keywords;
    }

    public int getKilobytes() {
        return kilobytes;
    }

    public void setKilobytes( int kilobytes ) {
        this.kilobytes = kilobytes;
    }

    public List getTopics() {
        return topics;
    }

    public void setTopics( List topics ) {
        this.topics = topics;
    }

    public List getRelatedSimulations() {
        return relatedSimulations;
    }

    public void setRelatedSimulations( List relatedSimulations ) {
        this.relatedSimulations = relatedSimulations;
    }

    public String getDesignTeam() {
        return designTeam;
    }

    public void setDesignTeam( String designTeam ) {
        this.designTeam = designTeam;
    }

    public String getLibraries() {
        return libraries;
    }

    public void setLibraries( String libraries ) {
        this.libraries = libraries;
    }

    public String getThanksTo() {
        return thanksTo;
    }

    public void setThanksTo( String thanksTo ) {
        this.thanksTo = thanksTo;
    }

    public boolean isUnderConstruction() {
        return underConstruction;
    }

    public void setUnderConstruction( boolean underConstruction ) {
        this.underConstruction = underConstruction;
    }

    public boolean isGuidanceRecommended() {
        return guidanceRecommended;
    }

    public void setGuidanceRecommended( boolean guidanceRecommended ) {
        this.guidanceRecommended = guidanceRecommended;
    }

    public boolean isClassroomTested() {
        return classroomTested;
    }

    public void setClassroomTested( boolean classroomTested ) {
        this.classroomTested = classroomTested;
    }

    public boolean isSimulationVisible() {
        return simulationVisible;
    }

    public void setSimulationVisible( boolean simulationVisible ) {
        this.simulationVisible = simulationVisible;
    }

    public boolean isVisible() {
        return isSimulationVisible() && project.isVisible();
    }

    /**
     * @return Whether this simulation is covered by the CC-BY 3.0 license. See Linkers.CC_BY_3
     */
    public boolean isHasCreativeCommonsAttributionLicense() {
        return hasCreativeCommonsAttributionLicense;
    }

    public void setHasCreativeCommonsAttributionLicense( boolean hasCreativeCommonsAttributionLicense ) {
        this.hasCreativeCommonsAttributionLicense = hasCreativeCommonsAttributionLicense;
    }

    public boolean isHasGPL2License() {
        // currently all sims are licensed under GPL 2.0
        return true;
    }

    /**
     * @return List of license URLs that this simulation is covered by
     */
    public List<String> getLicenseURLs() {
        List<String> result = new ArrayList<String>();
        if ( isHasCreativeCommonsAttributionLicense() ) {
            result.add( Linkers.CC_BY_3.getDefaultRawUrl() );
        }
        if ( isHasGPL2License() ) {
            result.add( Linkers.CC_GPL_2.getDefaultRawUrl() );
        }
        return result;
    }

    public Set getContributions() {
        return contributions;
    }

    public void setContributions( Set contributions ) {
        this.contributions = contributions;
    }

    public int getOldId() {
        return oldId;
    }

    public void setOldId( int oldId ) {
        this.oldId = oldId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime( Date createTime ) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime( Date updateTime ) {
        this.updateTime = updateTime;
    }

    public GradeLevel getLowGradeLevel() {
        return lowGradeLevel == null ? GradeLevel.getLowestGradeLevel() : lowGradeLevel;
    }

    public void setLowGradeLevel( GradeLevel lowGradeLevel ) {
        this.lowGradeLevel = lowGradeLevel;
    }

    public GradeLevel getHighGradeLevel() {
        return highGradeLevel == null ? GradeLevel.getHighestGradeLevel() : highGradeLevel;
    }

    public void setHighGradeLevel( GradeLevel highGradeLevel ) {
        this.highGradeLevel = highGradeLevel;
    }
}
