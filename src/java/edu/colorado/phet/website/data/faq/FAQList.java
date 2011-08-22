// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.website.data.faq;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.colorado.phet.website.data.util.IntId;

/**
 * This is basically a collection of FAQs and headers (in order)
 */
public class FAQList implements Serializable, IntId {

    // unique ID
    private int id;

    // unique name, used for the translation key
    private String name;

    // list of items, in order
    private List faqItems = new ArrayList();

    public FAQList() {
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
        // since we have a few strings that fit the new FAQ string format, we need to blacklist these to prevent overwriting them for the time being
        if ( name.equals( "embedding" ) || name.equals( "header" ) || name.equals( "licensing" ) || name.equals( "mobileDevices" ) || name.equals( "sourceCode" ) ) {
            throw new RuntimeException( "FAQ name reserved: " + name );
        }
        this.name = name;
    }

    public List getFaqItems() {
        return faqItems;
    }

    public void setFaqItems( List faqItems ) {
        this.faqItems = faqItems;
    }
}
