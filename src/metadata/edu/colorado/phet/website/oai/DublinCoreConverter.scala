package edu.colorado.phet.website.oai

import javax.servlet.ServletContext
import org.dlese.dpc.xml.XMLFormatConverter

/**
 * Converts our master format simulation data to Dublin Core XML
 */
class DublinCoreConverter extends XMLFormatConverter {
  def lastModified(p1: ServletContext) = 1307588299000L

  def getFromFormat = OaiUtils.MasterFormatName

  def getToFormat = "oai_dc"

  def convertXML(masterXML: String, servletContext: ServletContext): String = {

    val record = new SimulationRecord(masterXML)

    // TODO: complete this

    <oai_dc:dc xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dc="http://purl.org/dc/elements/1.1/" xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd">
      <dc:identifier>fix-identifier</dc:identifier>
      <dc:title>{record.englishTitle}</dc:title>
      <dc:description>This page includes the encyclopedia entries for elliptic integrals both complete and incomplete.</dc:description>
      <dc:publisher>Wolfram Research, Inc.</dc:publisher>
      <dc:rights>Copyright © 1998-2009 Wolfram Research, Inc.</dc:rights>
      <dc:identifier>hdl:2200/20090818165922890T</dc:identifier>
      <dc:type>Image/Image Set</dc:type>
      <dc:type>Audio/Visual</dc:type>
      <dc:type>Text</dc:type>
      <dc:type>Reference Material</dc:type>
      <dc:type>Glossary/Index</dc:type>
      <dc:type>Nonfiction Reference</dc:type>
      <dc:type>Movie/Animation</dc:type>
      <dc:type>Graph</dc:type>
      <dc:format>text</dc:format>
      <dc:rights>Free access</dc:rights>
      <dc:subject>Mathematics</dc:subject>
      <dc:language>English</dc:language>
    </oai_dc:dc>.toString
  }
}