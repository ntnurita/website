// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.website.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.xhtmlrenderer.pdf.ITextRenderer;

import edu.colorado.phet.common.phetcommon.util.FileUtils;

import com.lowagie.text.DocumentException;

/**
 * Utilities related to PDF handling and generation
 */
public class PDFUtils {
    public static void writeHTMLToPDF( String html, File outputFile ) throws IOException, DocumentException {
        File htmlFile = File.createTempFile( "html-to-pdf", "html" );
        FileUtils.writeString( htmlFile, html );

        OutputStream os = new FileOutputStream( outputFile );
        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument( htmlFile );
            renderer.layout();
            renderer.createPDF( os );
        }
        finally {
            os.close();
            htmlFile.delete();
        }
    }

    public static String constructPreferredHTML( String body, String css ) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
               "        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
               "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
               "<head>\n" +
               "    <title>No Title</title>\n" +
               "    <style type=\"text/css\">" +
               css +
               "    </style>\n" +
               "</head>\n" +
               "<body>" +
               body +
               "</body>\n" +
               "</html>";
    }
}
