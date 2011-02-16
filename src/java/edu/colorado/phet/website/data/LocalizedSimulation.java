/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.data;

import java.io.Serializable;
import java.util.Locale;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.components.RawLink;
import edu.colorado.phet.website.data.util.IntId;
import edu.colorado.phet.website.util.HtmlUtils;
import edu.colorado.phet.website.util.WebImage;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

public class LocalizedSimulation implements Serializable, IntId {
    private int id;
    private Locale locale;
    private String title;
    private Simulation simulation;

    public LocalizedSimulation() {
    }

    public RawLink getRunLink( String id ) {
        return new RawLink( id, getRunUrl() );
    }

    public RawLink getDownloadLink( String id ) {
        return new RawLink( id, getDownloadUrl() );
    }

    public String getRunUrl() {
        Simulation sim = getSimulation();
        Project project = sim.getProject();
        String str = "/sims/" + project.getName() + "/" + sim.getName() + "_" + getLocaleString();
        if ( sim.isJava() ) {
            str += ".jnlp";
        }
        else if ( sim.isFlash() ) {
            str += ".html";
        }
        else {
            throw new RuntimeException( "Handle more than java and flash" );
        }
        return str;
    }

    public String getDownloadUrl() {
        Simulation sim = getSimulation();
        Project project = sim.getProject();
        String str = "/sims/" + project.getName() + "/" + sim.getName() + "_" + getLocaleString();
        if ( sim.isJava() || sim.isFlash() ) {
            str += ".jar";
        }
        else {
            throw new RuntimeException( "Handle more than java and flash" );
        }
        return str;
    }

    public String getClickToLaunchSnippet( String launchText ) {
        WebImage screenshot = getSimulation().getImage();
        int width = screenshot.getWidth(); // probably 300
        int height = screenshot.getHeight();
        int subWidth = screenshot.getWidth() * 2 / 3; // probably 200
        int subHeight = 80;
        int subTop = ( height - subHeight ) / 2;
        int subLeft = ( width - subWidth ) / 2;
        return "<div style=\"position: relative; width: " + width + "px; height: " + height + "px;\"><a href=\"http://phet.colorado.edu"
               + getRunUrl() + "\" style=\"text-decoration: none;\"><img src=\"http://phet.colorado.edu" + screenshot.getSrc()
               + "\" alt=\"" + getTitle() + "\" style=\"border: none;\" width=\"" + width + "\" height=\"" + height
               + "\"/><div style=\"position: absolute; width: " + subWidth + "px; height: " + subHeight + "px; left: "
               + subLeft + "px; top: " + subTop + "px; background-color: #FFF; opacity: 0.6; filter: alpha(opacity = 60);\"></div><table style=\"position: absolute; width: "
               + subWidth + "px; height: " + subHeight + "px; left: " + subLeft + "px; top: " + subTop + "px;\"><tr><td style=\"text-align: center; color: #000; font-size: 24px; font-family: Arial,sans-serif;\">" + launchText + "</td></tr></table></a></div>";
    }

    public String getDirectEmbeddingSnippet() {
        if ( getSimulation().getProject().isFlash() ) {
            return "<iframe src=\"http://phet.colorado.edu" + getRunUrl() + "\" width=\"600\" height=\"450\"></iframe>";
        }
        else {
            return null;
        }
    }

    public String getLocaleString() {
        return LocaleUtils.localeToString( getLocale() );
    }

    public String getSortableTitle() {
        String ret = getTitle();
        for ( String ignoreWord : HibernateUtils.SIM_TITLE_IGNORE_WORDS ) {
            if ( ret.startsWith( ignoreWord + " " ) ) {
                ret = ret.substring( ignoreWord.length() + 1 );
            }
        }
        return ret;
    }

    public String getEncodedTitle() {
        return HtmlUtils.encode( title );
    }

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale( Locale locale ) {
        this.locale = locale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void setSimulation( Simulation simulation ) {
        this.simulation = simulation;
    }
}
