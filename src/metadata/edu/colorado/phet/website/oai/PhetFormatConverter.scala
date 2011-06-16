package edu.colorado.phet.website.oai

import org.dlese.dpc.xml.XMLFormatConverter
import javax.servlet.ServletContext

/**
 * Handles things that are common between our format converters from the master format
 */
abstract class PhetFormatConverter extends XMLFormatConverter {
  def lastModified(servletContext: ServletContext) = OaiUtils.commonTimestamp

  def getFromFormat = OaiUtils.MasterFormatName

  def convertRecord(record: SimulationRecord, servletContext: ServletContext): String

  def convertXML(masterXML: String, servletContext: ServletContext): String = {
    try {
      convertRecord(new SimulationRecord(masterXML), servletContext)
    }
    catch {
      case e: Exception => {
        // since jOAI doesn't print the stacktrace, we'll do it here instead
        e.printStackTrace()
        println("Error in conversion for: " + ( new SimulationRecord(masterXML).englishTitle ))
        throw e
      }
    }
  }
}