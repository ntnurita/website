package edu.colorado.phet.website.oai

import java.text.SimpleDateFormat
import java.util.Date
import xml.{NodeSeq, Unparsed, Node}

/**
 * Metadata related utilities
 */
object OaiUtils {
  val MasterFormatName = "phet-simulation"

  // TODO: date cleanup
  def dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss") // NOTE: also used in MetadataUtils

  def iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

  def formatDateIso8601(date: Date) = iso8601DateFormat.format(date)

  /**
   * Turn a name into a VCard format entry
   */
  def vCardFromName(name: String): Node = Unparsed("<![CDATA[BEGIN:VCARD\nFN:" + name + "\nVERSION:3.0\nEND:VCARD]]>")

  /**
   * Turns a sequence of translated strings into an IEEE string (same meaning, but in different languages) that can be included in XML
   */
  def convertLangString(lang: Seq[LanguageString]): NodeSeq = lang.map(str => <string language={str.language}>{str.string}</string>)

  /**
   * Find and return the English translation from a list of LanguageStrings
   */
  def englishString(lang: Seq[LanguageString]): String = lang.find(_.language == "en").get.string

  val commonTimestamp = 1312240182000L; // used so we can update all converters at once
  //  val commonTimestamp = System.currentTimeMillis(); // TODO: remove this, since it always regenerates
}