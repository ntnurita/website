/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SqlResultTask {
    public boolean process( ResultSet result ) throws SQLException;
}
