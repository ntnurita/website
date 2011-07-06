package edu.colorado.phet.website.tools

import xml.XML
import java.util.Properties
import java.io.{FileWriter, File}

/**
 * Converts a vdex-formatted vocabulary into properties of the format id=english
 *
 * Usage: args(0) is the input vdex file
 *        args(1) is the output file
 *
 * Example:
 *     (run it with) /home/jon/phet/git/website/assets/LRE-LRE-0001.xml /home/jon/phet/git/website/data/website/LRE-0001.properties
 */
object VdexPropertyConverter {
  def main(args: Array[String]) {
    val doc = XML.loadFile(new File(args(0)))
    val properties = new Properties()
    for ( term <- ( doc \ "term" );
          englishNode = ( term \ "caption" \ "langstring" ).find(str => ( str \ "@language" ).text == "en")
          if englishNode.isDefined ) {
      val id = ( term \ "termIdentifier" ).text
      val englishString = englishNode.get.text
      properties.setProperty(id, englishString)
    }
    properties.store(new FileWriter(new File(args(1))), "formatted as id=english")
  }
}