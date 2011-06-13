package edu.colorado.phet.website.oai

import javax.servlet.ServletContext
import org.dlese.dpc.xml.XMLFormatConverter

/**
 * Converts our master format simulation data to Dublin Core XML
 */
class DublinCoreConverter extends XMLFormatConverter {
  def lastModified(p1: ServletContext) = 1307600537000L

  def getFromFormat = OaiUtils.MasterFormatName

  def getToFormat = "oai_dc"

  def convertXML(masterXML: String, servletContext: ServletContext): String = {

    val record = new SimulationRecord(masterXML)

    // TODO: complete this

    // TODO: dc:subject
    // TODO: language (s)? I saw an example with multiple descriptions, later ones with xml:lang="" set
    // TODO: date? see W3CDTF format

    // type uses InteractiveResource from http://dublincore.org/documents/dcmi-type-vocabulary/, and also "Simulation" as our general type
    // languages encoded in RFC 4646

    <oai_dc:dc xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dc="http://purl.org/dc/elements/1.1/" xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd">
      <dc:identifier>{record.simPageLink}</dc:identifier>
      <dc:title>{record.englishTitle}</dc:title>
      <dc:description>{record.englishDescrption}</dc:description>
      <dc:creator>PhET Interactive Simulations</dc:creator>
      <dc:publisher>PhET Interactive Simulations</dc:publisher>
      <dc:rights>Free access / usage by everyone</dc:rights>
      <dc:rights>More licensing information available at http://phet.colorado.edu/en/about/licensing</dc:rights>
      <dc:rights>© 2011 University of Colorado</dc:rights>
      <dc:type>InteractiveResource</dc:type>
      <dc:type>Simulation</dc:type>
      {record.mimeTypes.map(mimeType => <dc:format>{mimeType}</dc:format>)}
      {record.languages.map(language => <dc:language>{language}</dc:language>)}
    </oai_dc:dc>.toString
  }
}