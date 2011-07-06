package edu.colorado.phet.website.oai

import xml.{NodeSeq, XML}
import collection.mutable.ListBuffer
import java.util.Date
import edu.colorado.phet.website.data.GradeLevel

/**
 * Represents a simulation record from the master data
 */
class SimulationRecord(str: String) {
  val xml = XML.loadString(str)

  def projectName: String = ( xml \ "project" \ "@name" ).text

  def simulationName: String = ( xml \ "simulation" \ "@name" ).text

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

  def translatedCategories: Seq[Seq[LanguageString]] = ( xml \ "categories" \ "category" ).map(node => allStrings(node))

  /*---------------------------------------------------------------------------*
  * NSDL Science Literacy Maps (keys like SMS-BMK-0141 or SMS-MAP-1357)
  *----------------------------------------------------------------------------*/

  def NSDLScienceLiteracyMapKeys: Seq[String] = ( xml \ "scienceLiteracyMaps" \ "mapKey" ).map(_.text)

  /*---------------------------------------------------------------------------*
  * LRE-0001 classification terms
  *----------------------------------------------------------------------------*/

  /**
   * returns a list of LRE-0001 terms, in the (id, english) variant
   */
  def lreTerms: Seq[(String, String)] = ( xml \ "classification" \ "lre0001" \ "term" ).map(term => (( term \ "@id" ).text, ( term \ "@english" ).text))

  /*---------------------------------------------------------------------------*
  * URLs
  *----------------------------------------------------------------------------*/
  def simPageLink = ( xml \ "simPageLink" ).text

  def thumbnailLink = "http://phet.colorado.edu/sims/" + projectName + "/" + simulationName + "-thumbnail.jpg"

  def screenshotLink = "http://phet.colorado.edu/sims/" + projectName + "/" + simulationName + "-screenshot.png"

  def runNowUrl(language: String): String = {
    if ( isJava ) {
      simulationBase + "_" + language + ".jnlp"
    }
    else {
      simulationBase + "_" + language + ".html"
    }
  }

  def downloadUrl(language: String): String = simulationBase + "_" + language + ".jar"

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

  def kilobytes = Integer.parseInt(( xml \ "filesize" \ "@kilobytes" ).text)

  /*---------------------------------------------------------------------------*
  * dates
  *----------------------------------------------------------------------------*/

  def hasTimeCreated = ( xml \ "createTime" ).text.length() > 0

  def hasTimeUpdated = ( xml \ "updateTime" ).text.length() > 0

  def dateCreated: Date = OaiUtils.dateFormat.parse(( xml \ "createTime" ).text)

  def dateUpdated: Date = OaiUtils.dateFormat.parse(( xml \ "updateTime" ).text)

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
  * credits
  *----------------------------------------------------------------------------*/

  /**
   * Strips off things from the designTeam that aren't parts of names
   */
  def authors: Seq[String] = ( xml \ "credits" \ "@designTeam" ).text.split('|').map(str => {
    val AuthorRegex(prefix, name, role) = str
    name
  }).distinct

  private val AuthorRegex = """^(.*: *)?([^:\(]+)( +\(.*)?$""".r

  def licenseURLs: Seq[String] = ( xml \ "licenses" \ "license" ).map(elem => ( elem \ "url" ).text)

  /*---------------------------------------------------------------------------*
  * grade levels
  *----------------------------------------------------------------------------*/

  def minGradeLevel = GradeLevel.valueOf(( xml \ "minGradeLevel" ).text)

  def maxGradeLevel = GradeLevel.valueOf(( xml \ "maxGradeLevel" ).text)

  def gradeLevels = GradeLevel.values().filter(level => {
    !GradeLevel.isLowerGradeLevel(level, minGradeLevel) && // enforce "no lower than minimum grade level"
    !GradeLevel.isLowerGradeLevel(maxGradeLevel, level) // enforce "no higher than maximum grade level"
  })

  /*---------------------------------------------------------------------------*
  * implementation
  *----------------------------------------------------------------------------*/

  def simulationBase = "http://phet.colorado.edu/sims/" + projectName + "/" + simulationName

  /**
   * Given a node with contained "string" elements, return the text within the English "string" element
   */
  def englishString(node: NodeSeq): String = ( node \ "string" ).filter(node => ( node \ "@locale" ).text == "en").text

  /**
   * Given a node with contained "string" elements, return the text within all "string" elements
   */
  def allStrings(node: NodeSeq): Seq[LanguageString] = ( node \ "string" ).map(str => new LanguageString(( str \ "@locale" ).text, str.text))
}
