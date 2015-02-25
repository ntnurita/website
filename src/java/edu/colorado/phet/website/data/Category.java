/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.data.util.IntId;
import edu.colorado.phet.website.menu.NavLocation;
import edu.colorado.phet.website.menu.NavMenu;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * A simulation category. 'All Sims' and 'Translated Sims' are not stored in these categories.
 * <p/>
 * An undisplayed 'root' category is the parent of all others, and is important for its childrens' ordering.
 */
public class Category implements Serializable, IntId {
    private int id;

    /**
     * Used for the nav location key AND URL
     */
    private String name;

    /**
     * Whether the category should include all simulations from children (true), or just simulations assigned to the
     * exact category (false). It is helpful for base categories to be set to auto, so if a sim is added to
     * 'Light &amp; Radiation', it would be auto-added to 'Physics';
     */
    private boolean auto;

    /**
     * Whether this category should override the ordering and alphabetize the listed simulations.
     */
    private boolean alphabetize;

    /**
     * Whether this is the root category. Consider it immutable! BAD THINGS will happen if this goes haywire, and checks
     * require there is only 1 root category.
     */
    private boolean root;

    /**
     * Ordered list of subcategories
     */
    private List subcategories = new LinkedList();

    /**
     * Ordered list of simulations
     */
    private List simulations = new LinkedList();

    /**
     * Parent category (root category will have this be null)
     */
    private Category parent;

    public static final String BY_LEVEL_CATEGORY_NAME = "by-level";
    public static final String BY_DEVICE_NAME = "by-device";
    public static final String NEW_CATEGORY_NAME = "new";

    /*---------------------------------------------------------------------------*
    * grade-level category names
    *----------------------------------------------------------------------------*/

    public static final String ELEMENTARY_SCHOOL = "elementary-school";
    public static final String MIDDLE_SCHOOL = "middle-school";
    public static final String HIGH_SCHOOL = "high-school";
    public static final String UNIVERSITY = "university";

    /*---------------------------------------------------------------------------*
    * device category names
    *----------------------------------------------------------------------------*/

    public static final String IPAD_TABLET = "ipad-tablet";
    public static final String CHROMEBOOK = "chromebook";

    private static final Set<String> DEVICE_CATEGORY_NAMES = new HashSet<String>() {{
        add( IPAD_TABLET );
        add( CHROMEBOOK );
    }};

    private static final Set<String> GRADE_LEVEL_CATEGORY_NAMES = new HashSet<String>() {{
        add( ELEMENTARY_SCHOOL );
        add( MIDDLE_SCHOOL );
        add( HIGH_SCHOOL );
        add( UNIVERSITY );
    }};

    private static final Logger logger = Logger.getLogger( Category.class.getName() );

    public Category() {
    }

    public Category( Category parent, String name ) {
        this.parent = parent;
        this.name = name;
        if ( parent == null ) {
            auto = true;
            root = true;
        }
        else if ( parent.isRoot() ) {
            auto = true;
            root = false;
        }
        else {
            auto = false;
            root = false;
        }

        if ( !root ) {
            parent.subcategories.add( this );
        }
    }

    public static String getDefaultCategoryKey() {
        return "new";
    }

    public void addSimulationToEnd( Simulation simulation ) {
        simulation.getCategories().add( this );
        getSimulations().add( simulation );
    }

    public void addSimulationToStart( Simulation simulation ) {
        simulation.getCategories().add( this );
        getSimulations().add( 0, simulation );
    }

    public void removeSimulation( Simulation simulation ) {
        simulation.getCategories().remove( this );
        getSimulations().remove( simulation );
    }

    /**
     * Add the new HTML5 sim in the simulations list next to the legacy version
     * @param legacySimulation
     * @param newSimulation
     */
    public void addNewSimVersion( Simulation legacySimulation, Simulation newSimulation ) {
        if ( !getSimulations().contains( newSimulation ) ) {
            int index = getSimulations().indexOf( legacySimulation );
            getSimulations().add( index, newSimulation );
        }
    }

    /**
     * NOTE: must be in session transaction!
     *
     * @param session
     * @param categoriesString
     * @return
     */
    public static Category getCategoryFromPath( Session session, String categoriesString ) {
        Category category;
        logger.debug( "categoriesString = " + categoriesString );

        // strip off the trailing slash if it exists
        String strippedCategoriesString = ( categoriesString.endsWith( "/" ) ? categoriesString.substring( 0, categoriesString.length() - 1 ) : categoriesString );

        String[] categories = strippedCategoriesString.split( "/" );
        int categoryIndex = categories.length - 1;
        if ( categories[categoryIndex].equals( "" ) ) {
            categoryIndex--;
        }
        String categoryName = categories[categoryIndex];
        category = HibernateUtils.getCategoryByName( session, categoryName );
        if ( category == null ) {
            logger.warn( "WARNING: attempt to access category " + strippedCategoriesString + " resulted in failure" );
            return category;
        }

        logger.debug( "category path: " + category.getCategoryPath() );

        if ( !category.getCategoryPath().equals( strippedCategoriesString ) ) {
            logger.warn( "category path doesn't match category strings: " + category.getCategoryPath() + " != " + strippedCategoriesString );
            return null;
        }
        return category;
    }

    public static Category getRootCategory( Session session ) {
        // NOTE: must be in session transaction
        return (Category) session.createQuery( "select c from Category as c where c.root = true" ).uniqueResult();
    }

    public NavLocation getNavLocation( NavMenu menu ) {
        // TODO: check and see whether we ever use a different menu!

        // TODO: check this usage and compare it for getLocalizationKey usages
        return menu.getLocationByKey( getName() );
    }

    public String getLocalizationKey() {
        return getNavLocation( PhetWicketApplication.get().getMenu() ).getLocalizationKey();
    }

    public String getBreadcrumbLocalizationKey() {
        return getNavLocation( PhetWicketApplication.get().getMenu() ).getBreadcrumbLocalizationKey();
    }

    /**
     * Note: parent should be loaded
     *
     * @return
     */
    public String getCategoryPath() {
        if ( isRoot() ) {
            return "";
        }
        else if ( getParent().isRoot() ) {
            return getName();
        }
        else {
            return getParent().getCategoryPath() + "/" + getName();
        }
    }

    public int getDepth() {
        if ( isRoot() ) {
            return 0;
        }
        else {
            return getParent().getDepth() + 1;
        }
    }

    public String getBaseName() {
        if ( isRoot() ) {
            return null;
        }
        if ( getParent().isRoot() ) {
            logger.warn( "My parent is root, I am " + getName() );
            return getName();
        }
        else {
            return getParent().getBaseName();
        }
    }

    /**
     * Use for filtering in metadata (is this a "subject" category?)
     *
     * @return Whether this category represents what type of educational content is available (not age level)
     */
    public boolean isContentCategory() {
        return !isGradeLevelCategory() && !getName().equals( Category.BY_LEVEL_CATEGORY_NAME ) && !getName().equals( Category.NEW_CATEGORY_NAME );
    }

    /**
     * @return Whether this category also represents a grade-level category (Elementary school, etc.)
     */
    public boolean isGradeLevelCategory() {
        return GRADE_LEVEL_CATEGORY_NAMES.contains( getName() );
    }

    /**
     * @return Whether this category also represents a by-device category (iPad, etc.)
     */
    public boolean isDeviceCategory() {
        return DEVICE_CATEGORY_NAMES.contains( getName() );
    }

    @Override
    public String toString() {
        return "Cat: " + getName();
    }

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

    public boolean isAuto() {
        return auto;
    }

    public void setAuto( boolean auto ) {
        this.auto = auto;
    }

    public boolean isAlphabetize() {
        return alphabetize;
    }

    public void setAlphabetize( boolean alphabetize ) {
        this.alphabetize = alphabetize;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot( boolean root ) {
        this.root = root;
    }

    public List getSubcategories() {
        return subcategories;
    }

    public void setSubcategories( List subcategories ) {
        this.subcategories = subcategories;
    }

    public List getSimulations() {
        return simulations;
    }

    public void setSimulations( List simulations ) {
        this.simulations = simulations;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent( Category parent ) {
        this.parent = parent;
    }
}
