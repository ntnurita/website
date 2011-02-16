/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.Model;

import edu.colorado.phet.website.util.WebImage;

/**
 * Handles an image with src and alt attributes. wicket:id should be placed upon the img tag itself, and then
 * the existing (or nonexisting) attributes will be overwritten.
 */
public class StaticImage extends WebComponent {

    private String url = null;
    private String alt = null;
    private String dataServerPrefix = "";

    /**
     * Create an image with a standard URL for a src attribute.
     *
     * @param id  The Wicket ID for the image
     * @param src The src attribute (URL)
     * @param alt The alt text for accessibility
     */
    public StaticImage( String id, String src, String alt ) {
        super( id );
        this.url = src;
        this.alt = alt;
    }

    /**
     * Create an image with a standard URL that also includes image dimensions
     *
     * @param id    The Wicket ID for the image
     * @param image WebImage
     * @param alt   The alt text for accessibility
     */
    public StaticImage( String id, WebImage image, String alt ) {
        super( id );
        this.url = image.getSrc();
        this.alt = alt;

        // TODO: possibly use tag.put below?
        add( new AttributeModifier( "width", true, new Model<String>( String.valueOf( image.getWidth() ) ) ) );
        add( new AttributeModifier( "height", true, new Model<String>( String.valueOf( image.getHeight() ) ) ) );
    }

    public void setDataServer( String dataServer ) {
        dataServerPrefix = "http://" + dataServer;
    }

    public String getUrl() {
        return dataServerPrefix + url;
    }

    /*---------------------------------------------------------------------------*
    * implementation
    *----------------------------------------------------------------------------*/

    protected void onComponentTag( ComponentTag tag ) {
        checkComponentTag( tag, "img" );
        super.onComponentTag( tag );
        tag.put( "src", getUrl() );
        if ( alt != null ) {
            tag.put( "alt", alt );
        }
    }

    @Override
    protected boolean getStatelessHint() {
        return true;
    }
}