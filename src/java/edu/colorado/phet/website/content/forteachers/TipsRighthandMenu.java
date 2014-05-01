// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.content.forteachers;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.image.Image;

import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;

/**
 * Displays a vertical list of social bookmarking service icons
 */
public class TipsRighthandMenu extends PhetPanel {
    @Override
	protected void onRender(MarkupStream markupStream) {
		// TODO Auto-generated method stub
		super.onRender(markupStream);
	}

	public TipsRighthandMenu( String id, final PageContext context, final String bookmarkableUrl, final String bookmarkableTitle ) {
        super( id, context );

        Image img = new Image("planningToUsePhet") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                // illustrates how to prevent an image from caching by adding a random value to the src attribute
                // This is similar to the code thats in NonCachingImage and would be the preferable solution for this
                super.onComponentTag(tag);
                String src = (String) tag.getAttributes().get("style");
                src = "display:block";
                tag.getAttributes().put("style", src);
            }
        };
        img.setVisible(true);
    }
}