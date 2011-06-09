package edu.colorado.phet.website.oai

import xml.{NodeSeq, XML}

/**
 * Represents a simulation record from the master data
 */
class SimulationRecord(str: String) {
  val xml = XML.loadString(str)

  def englishTitle = englishString(xml \ "title")

  def englishDescrption = englishString(xml \ "description")

  def simPageLink = ( xml \ "simPageLink" ).text

  def englishString(node: NodeSeq): String = ( node \ "string" ).filter(node => ( node \ "@locale" ).text == "en").text
}