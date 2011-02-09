/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.components;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.Link;

import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.links.Linkable;

/**
 * Wraps a link around an image
 */
public abstract class LinkImageWrapper extends PhetPanel {
    private Link link;

    public abstract Component createChild( String id );

    public LinkImageWrapper( String id, PageContext context, Linkable linker ) {
        super( id, context );

        link = linker.getLink( "link", context, getPhetCycle() );
        add( link );
        Component child = createChild( "child" );
        assert ( child.getId().equals( "child" ) );
        link.add( child );
    }

    public Link getLink() {
        return link;
    }

}
