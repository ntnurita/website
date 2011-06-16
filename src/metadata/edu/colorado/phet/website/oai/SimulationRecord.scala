package edu.colorado.phet.website.oai

import xml.{NodeSeq, XML}
import collection.mutable.ListBuffer

/**
 * Represents a simulation record from the master data
 */
class SimulationRecord(str: String) {
  val xml = XML.loadString(str)

  /*---------------------------------------------------------------------------*
  * titles
  *----------------------------------------------------------------------------*/
  def englishTitle: String = englishString(xml \ "title")

  def translatedTitles: Seq[LanguageString] = allStrings(xml \ "title")

  /*---------------------------------------------------------------------------*
  * descriptions
  *----------------------------------------------------------------------------*/
  def englishDescrption: String = englishString(xml \ "description")

  def translatedDescriptions: Seq[LanguageString] = allStrings(xml \ "description")

  /*---------------------------------------------------------------------------*
  * terms (keywords / topics combined)
  *----------------------------------------------------------------------------*/
  def englishTerms: Seq[String] = filteredTerms.map(node => englishString(node))

  def translatedTerms: Seq[Seq[LanguageString]] = filteredTerms.map(node => allStrings(node)) // list of keywords (each with a list of translations)

  private def filteredTerms = ( xml \\ "keyword" ).distinct // remove duplicates

  /*---------------------------------------------------------------------------*
  * URLs
  *----------------------------------------------------------------------------*/
  def simPageLink = ( xml \ "simPageLink" ).text

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

  def englishSoftwareRequirements: String = if ( isJava ) "Sun Java 1.5.0_15 or later" else "Adobe Flash 9 or later"

  def kilobytes = Integer.parseInt(( xml \ "filesize" \ "kilobytes" ).text)

  /*---------------------------------------------------------------------------*
  * dates
  *----------------------------------------------------------------------------*/

  def timeCreated = ( xml \ "createTime" ).text // W3CDTF
  def timeUpdated = ( xml \ "updateTime" ).text // W3CDTF

  /*---------------------------------------------------------------------------*
  * languages (translations)
  *----------------------------------------------------------------------------*/

  def languages: Seq[String] = ( xml \\ "language" ).map(_.text)

  /*---------------------------------------------------------------------------*
  * versioning
  *----------------------------------------------------------------------------*/

  def majorVersion = ( xml \ "version" \ "@major" ).text

  def minorVersion = ( xml \ "version" \ "@minor" ).text

  def revisionVersion = ( xml \ "version" \ "@revision" ).text

  def timestampVersion = ( xml \ "version" \ "@timestamp" ).text

  def versionString = majorVersion + "." + minorVersion + ( if ( minorVersion.length() < 2 ) "0" else "" )

  /*---------------------------------------------------------------------------*
  * implementation
  *----------------------------------------------------------------------------*/

  /**
   * Given a node with contained "string" elements, return the text within the English "string" element
   */
  def englishString(node: NodeSeq): String = ( node \ "string" ).filter(node => ( node \ "@locale" ).text == "en").text

  /**
   * Given a node with contained "string" elements, return the text within all "string" elements
   */
  def allStrings(node: NodeSeq): Seq[LanguageString] = ( node \ "string" ).map(str => new LanguageString(( str \ "@locale" ).text, str.text))
}
