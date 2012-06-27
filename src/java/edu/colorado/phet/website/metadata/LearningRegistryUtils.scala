// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.metadata

import formats.NSDLDCConverter
import java.io.File
import edu.colorado.phet.common.phetcommon.util.FileUtils

// Envelope creation and publishing for data destined for a Learning Registry node
object LearningRegistryUtils {

  // common keywords that we want to be included in every simulation
  private val LRTerms = List("phet", "simulation")

  def wrapWithEnvelope(record: SimulationRecord, formatConverter: PhetMetadataConverter): String = {

    val keywords: Seq[String] = LRTerms ++ record.translatedTerms.flatten.map(_.string).distinct

    import net.liftweb.json.JsonDSL._
    import net.liftweb.json.Printer.compact
    import net.liftweb.json.JsonAST.render
    val json = ( "doc_type" -> "resource_data" ) ~
               ( "doc_version" -> "0.23.0" ) ~
               ( "resource_data_type" -> "metadata" ) ~
               ( "active" -> true ) ~
               ( "identity" -> (
                               ( "submitter_type" -> "agent" ) ~
                               ( "submitter" -> "PhET Interactive Simulations" ) ~
                               // curator not needed
                               ( "owner" -> "PhET Interactive Simulations" ) ~
                               ( "signer" -> "PhET Interactive Simulations" )
                               ) ) ~
               ( "TOS" -> (
                          ( "submission_TOS" -> "http://www.learningregistry.org/tos/cc0/v0-5/" )
                          ) ) ~
               ( "resource_locator" -> record.simPageLink ) ~
               ( "keys" -> keywords ) ~
               ( "payload_placement" -> "inline" ) ~
               ( "payload_schema" -> List(
                 formatConverter.getLRSchemaTypes
               ) ) ~
               ( "payload_schema_locator" -> formatConverter.getSchemaURI ) ~
               ( "resource_data" -> formatConverter.convertRecord(record).toString )

    compact(render(json))
  }

  def publishMetadata(record: SimulationRecord, formatConverter: PhetMetadataConverter) {
    println(wrapWithEnvelope(record, formatConverter))
  }

  def main(args: Array[String]) {

    // TODO: do this on application initialization (if added in with other code
    //    Security.addProvider( new BouncyCastleProvider )

    // TODO: do with each metadata format
    new File("/Users/olsonsjc/phet/tmp/metadata").listFiles().foreach((file: File) => publishMetadata(
      new SimulationRecord(FileUtils.loadFileAsString(file)),
      new NSDLDCConverter{}
    ))
  }


}
