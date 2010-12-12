package edu.colorado.phet.website.components;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class StringTextField extends TextField<String> {
    public StringTextField( String id, IModel<String> object ) {
        super( id, object );
        setType( String.class );
    }
}
