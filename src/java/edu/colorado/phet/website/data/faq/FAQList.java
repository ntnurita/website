// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.website.data.faq;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.xhtmlrenderer.util.XRRuntimeException;

import edu.colorado.phet.common.phetcommon.util.FileUtils;
import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.common.phetcommon.util.function.Function1;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.data.util.IntId;
import edu.colorado.phet.website.panels.faq.FAQPanel;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.util.PDFUtils;
import edu.colorado.phet.website.util.links.RawLinkable;
import edu.colorado.phet.website.util.links.RawLinker;

import com.lowagie.text.DocumentException;

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

    private static final Logger logger = Logger.getLogger( FAQList.class.getName() );

    private String getBasePDFFileLocation( Locale locale ) {
        return "faq/" + name + "/faq-" + name + "_" + LocaleUtils.localeToString( locale ) + ".pdf";
    }

    public String getLocalPDFUrl( Locale locale ) {
        return "/files/" + getBasePDFFileLocation( locale );
    }

    public RawLinkable getPDFLinker( Locale locale ) {
        return new RawLinker( getLocalPDFUrl( locale ) );
    }

    public File getPDFFile( Locale locale ) {
        File filesDir = PhetWicketApplication.get().getWebsiteProperties().getPhetDownloadRoot();
        return new File( filesDir, getBasePDFFileLocation( locale ) );
    }

    public boolean updatePDFs( Session session ) {
        boolean englishSuccess = false;
        for ( Locale locale : PhetWicketApplication.get().getAllVisibleTranslationLocales() ) {
            boolean success = writeFaq( session, locale );
            if ( locale.equals( WebsiteConstants.ENGLISH ) ) {
                englishSuccess = success;
            }
        }
        return englishSuccess;
    }

    public boolean writeFaq( Session session, Locale locale ) {
        return writeFaqToFile( getPDFFile( locale ), session, locale );
    }

    public boolean writeFaqToFile( File outFile, final Session session, final Locale locale ) {
        logger.info( "Attempting to write PDF FAQ for " + getName() + " in " + LocaleUtils.localeToString( locale ) );
        // body text of the HTML ;)
        StringBuilder bodyBuilder = new StringBuilder();
        final PhetLocalizer localizer = PhetLocalizer.get();

        // add all of the FAQ item XHTML to the body
        for ( Object o : faqItems ) {
            FAQItem item = (FAQItem) o;
            bodyBuilder.append( item.getXHTMLText( new Function1<String, String>() {
                public String apply( String key ) {
                    return localizer.getBestString( session, key, locale );
                }
            } ) );
        }

        try {
            outFile.getParentFile().mkdirs();

            // store to a temporary file so that if things go badly, we leave the regular output file in place!
            File tmpFile = File.createTempFile( "faq-tmp", "pdf" );
            try {
                // large chance this will throw the DocumentException if people didn't form perfect XHTML
                PDFUtils.writeHTMLToPDF( PDFUtils.constructPreferredHTML( bodyBuilder.toString(), FAQPanel.getFAQCSS() ), tmpFile );

                // success if we have reached here! delete and make the copy
                if ( outFile.exists() ) {
                    outFile.delete();
                }
                FileUtils.copyTo( tmpFile, outFile );
            }
            finally {
                tmpFile.delete();
            }
        }
        catch ( IOException e ) {
            // probably a file handling error.
            logger.error( "writing FAQ to file error", e );
            return false;
        }
        catch ( XRRuntimeException e ) {
            // bad xml!
            logger.info( "invalid XML for PDF FAQ " + getName() + " in " + LocaleUtils.localeToString( locale ) );
            return false;
        }
        catch ( DocumentException e ) {
            // bad document!
            logger.info( "Bad XHTML for PDF FAQ " + getName() + " in " + LocaleUtils.localeToString( locale ) );
            return false;
        }
        return true;
    }

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
