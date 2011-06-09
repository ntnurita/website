package edu.colorado.phet.website.oai

import xml.{NodeSeq, XML}

/**
 * Represents a simulation record from the master data
 */
class SimulationRecord(str: String) {
  val xml = XML.loadString(str)

  println("filtered: " + ( ( xml \ "title" ) \ "string" ).filter(node => ( node \ "@locale" ).text == "en"))
  println("english title: " + englishString(xml \ "title"))

  // TODO: implement

  def englishTitle = "Fake Title"

  def englishString(node: NodeSeq): String = ( node \ "string" ).filter(node => ( node \ "@locale" ).text == "en").text
}