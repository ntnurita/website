// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.panels;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

import edu.colorado.phet.website.content.troubleshooting.JavaSecurity;
import edu.colorado.phet.website.util.PageContext;

public class JavaSecurityBanner extends PhetPanel {

    private static final Logger logger = Logger.getLogger( JavaSecurityBanner.class.getName() );

    public JavaSecurityBanner( String id, final PageContext context ) {
        super( id, context );
        
        // params: value, onclick="window.open('https://npo1.networkforgood.org/Donate/Donate.aspx?npoSubscriptionId=1006125','_blank');"
        add( new WebMarkupContainer( "security-button" ){{
            add( new AttributeModifier( "onclick", true, new Model<String>( "window.open('" + JavaSecurity.getLinker().getRawUrl( context, getPhetCycle() ) + "','_blank');" ) ) );
        }} );
    }
}