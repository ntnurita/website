package edu.colorado.phet.website.oai

import org.dlese.dpc.xml.XMLFormatConverter
import javax.servlet.ServletContext
import xml.Node

/**
 * Handles things that are common between our format converters from the master format
 */
abstract class PhetFormatConverter extends XMLFormatConverter {
  def lastModified(servletContext: ServletContext) = OaiUtils.commonTimestamp

  def getFromFormat = OaiUtils.MasterFormatName

  // main schema URI
  def getSchemaURI: Option[String]

  // schema types that are sent in the Learning Registry envelope
  def getLRSchemaTypes: List[String]

  /**
   * Actually convert the SimulationRecord into XML. We return NodeSeq so that we can embed output from one converter into
   * that of another.
   */
  def convertRecord(record: SimulationRecord): Node

  def convertXML(masterXML: String, servletContext: ServletContext): String = {
    try {
      // TODO: strip out comments in production version. Utility.toXML( node, true ) should work
      convertRecord(new SimulationRecord(masterXML)).toString()
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