// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.oai

import javax.servlet.ServletContext

object LearningRegistryUtils {

  def wrapWithEnvelope(record: SimulationRecord, formatConverter: PhetFormatConverter,
                       servletContext: ServletContext): String = {

    import net.liftweb.json.JsonDSL._
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
               ( "keys" -> record.englishTerms ) ~
               ( "payload_placement" -> "inline" ) ~
               ( "payload_schema" -> List(
                 formatConverter.getLRSchemaTypes
               ) ) ~
               ( "payload_schema_locator" -> formatConverter.getSchemaURI ) ~
               ( "resource_data" -> formatConverter.convertRecord(record, servletContext).toString )

    compact(render(json))
  }

}
