// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.website.util.hibernate;

import org.hibernate.Session;

public interface SimpleTask {
    public void run( Session session );
}
