package edu.colorado.phet.website.oai

import xml.{NodeSeq, XML}
import collection.mutable.ListBuffer

/**
 * Represents a simulation record from the master data
 */
class SimulationRecord(str: String) {
  val xml = XML.loadString(str)

  def englishTitle = englishString(xml \ "title")

  def englishDescrption = englishString(xml \ "description")

  def simPageLink = ( xml \ "simPageLink" ).text

  def englishString(node: NodeSeq): String = ( node \ "string" ).filter(node => ( node \ "@locale" ).text == "en").text

  /*---------------------------------------------------------------------------*
  * technology
  *----------------------------------------------------------------------------*/
  def technology = ( xml \ "technology" \ "@type" ).text

  def isJava = technology == "0"

  def isFlash = technology == "1"

  def mimeTypes: Seq[String] = {
    val result = new ListBuffer[String]
    if ( isJava ) {
      result += "application/x-java-jnlp-file" // JNLP method of delivery
      result += "application/java-archive" // JAR method of delivery
    }
    if ( isFlash ) {
      result += "application/x-shockwave-flash" // SWF method of delivery
    }
    result += "text/html" // the main sim page with resources / etc
  }

  def languages: Seq[String] = ( xml \\ "language" ).map(_.text)
}
