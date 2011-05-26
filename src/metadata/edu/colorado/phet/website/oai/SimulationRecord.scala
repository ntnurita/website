package edu.colorado.phet.website.oai

import xml.XML

/**
 * Represents a simulation record from the master data
 */
class SimulationRecord(str: String) {
  val xml = XML.loadString(str)

  // TODO: implement

  def englishTitle = "Fake Title"
}