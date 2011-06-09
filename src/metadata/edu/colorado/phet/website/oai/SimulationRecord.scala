package edu.colorado.phet.website.oai

import xml.XML

/**
 * Represents a simulation record from the master data
 */
class SimulationRecord(str: String) {
  val xml = XML.loadString(str)

  println("woohoo")
  println("xml: " + xml)
  println("xml \\ title: " + ( xml \ "title" ))

  // TODO: implement

  def englishTitle = "Fake Title"
}