package edu.colorado.phet.website.oai

import javax.servlet.ServletContext
import org.dlese.dpc.xml.XMLFormatConverter

/**
 * Converts our master format simulation data to Dublin Core XML
 */
class DublinCoreConverter extends XMLFormatConverter {
  def lastModified(p1: ServletContext) = 1306376433000L

  def convertXML(masterXML: String, servletContext: ServletContext): String = {

    val record = new SimulationRecord(masterXML)

    // TODO: complete this

    <oai_dc:dc
      xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
      xmlns:dc="http://purl.org/dc/elements/1.1/"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd">
      <dc:title>{record.englishTitle}</dc:title>
    </oai_dc:dc>.toString
  }

  def getToFormat = "oai_dc"

  def getFromFormat = OaiUtils.MasterFormatName
}