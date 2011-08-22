/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.util.hibernate;

import org.hibernate.Session;

/**
 * A general Hibernate task
 */
public interface HibernateTask {

    /**
     * @param session Hibernate session that can be used to make queries.
     * @return Success
     */
    public boolean run( Session session );
}
