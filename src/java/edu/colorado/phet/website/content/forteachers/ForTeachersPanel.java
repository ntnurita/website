// Copyright 2002-2013, University of Colorado
package edu.colorado.phet.website.content.forteachers;

import org.apache.wicket.Component;

import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.wicket.IComponentFactory;
import edu.colorado.phet.website.util.wicket.WicketUtils;

/**
 * Superclass for for-teachers panels
 */
public abstract class ForTeachersPanel extends PhetPanel {

    public static final int CONTENT_WIDTH = 1120;

    protected PageContext pageContext;
    protected boolean addedTips = false;

    public ForTeachersPanel( String id, PageContext context ) {
        super( id, context );
        pageContext = context;
    }

    public void addRighthandMenu( final String key ) {
        if ( !addedTips ) {
            add( WicketUtils.componentIf( true, "righthand-menu-panel", new IComponentFactory<Component>() {
                public Component create( String id ) {
                    return new TipsRighthandMenu( "righthand-menu-panel", pageContext, key );
                }
            } ) );
            addedTips = true;
        }
    }
}
