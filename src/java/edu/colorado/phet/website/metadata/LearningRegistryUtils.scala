// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.metadata

import formats.NSDLDCConverter
import java.io.File
import edu.colorado.phet.common.phetcommon.util.FileUtils
import com.navnorth.learningregistry._
import org.apache.log4j.Logger
import edu.colorado.phet.website.{PhetWicketApplication, WebsiteProperties}
import scala.collection.JavaConversions._

// Envelope creation and publishing for data destined for a Learning Registry node
object LearningRegistryUtils {
  private[this] val logger: Logger = Logger.getLogger(getClass.getName)

  // common keywords that we want to be included in every simulation
  private val LRTerms = List("phet", "simulation")

  // returns success
  def submitEnvelope(record: SimulationRecord, formatConverter: PhetMetadataConverter) {
    // extract the necessary strings and information that is not stored inside the website WAR or codebase
    val websiteProperties: WebsiteProperties = PhetWicketApplication.get().getWebsiteProperties
    val publicKeyLocation = websiteProperties.getGPGPublicKeyLocation
    val privateKey = FileUtils.loadFileAsString(websiteProperties.getGPGPrivateKeyFile)
    val passphrase = websiteProperties.getGPGPassphrase
    val nodeHost = websiteProperties.getLearningRegistryNodeHost
    val nodeProtocol = websiteProperties.getLearningRegistryProtocol

    // initialize the signer and exporter
    val batchsize = 1
    val signer = new LRSigner(publicKeyLocation, privateKey, passphrase)
    val exporter = new LRExporter(batchsize, nodeHost, nodeProtocol == "https")
    exporter.configure()

    val keywords = ( LRTerms ++ record.translatedTerms.flatten.map(_.string).distinct )

    // build the envelope
    val envelope = new LREnvelope {
      protected def getResourceData: AnyRef = formatConverter.convertRecord(record).toString()

      resourceDataType = "metadata"
      resourceLocator = record.simPageLink
      // no curator
      owner = "PhET Interactive Simulations"
      tags = keywords.toArray // keywords
      payloadPlacement = "inline"
      payloadSchemaLocator = formatConverter.getSchemaURI match {
        case Some(x) => x
        case None => null
      }
      payloadSchema = formatConverter.getLRSchemaTypes.toArray
      submitter = "PhET Interactive Simulations"
      submitterType = "agent"
      submissionTOS = "http://www.learningregistry.org/tos/cc0/v0-5/"
      // no submission attribution
      signer = "PhET Interactive Simulations"
    }

    // sign it with our keys
    val signedEnvelope = signer.sign(envelope)

    // upload the signed document
    exporter.addDocument(signedEnvelope)
    val responses = exporter.sendData()

    // and receive responses
    responses.foreach(response => {
      val hasError = !response.getResourceFailure.isEmpty || response.getResourceSuccess.isEmpty

      if ( hasError ) {
        logger.error("Error publishing " + record.simulationName + " with format " + formatConverter.toString + " to Learning Registry")
        logger.info("Batch Results")
        logger.info("Status Code: " + response.getStatusCode)
        logger.info("Status Reason: " + response.getStatusReason)
        logger.info("Batch Success: " + response.getBatchSuccess)
        logger.info("Batch Response: " + response.getBatchResponse)

        logger.info("Published Resource(s)")
        for ( id: String <- response.getResourceSuccess ) {
          logger.info("ID: " + id)
          logger.info("URI: http://" + nodeHost + "/harvest/getrecord?by_doc_ID=T&request_ID=" + id)
        }

        if ( !response.getResourceFailure.isEmpty ) {
          logger.warn("Publish Errors")

          for ( message <- response.getResourceFailure ) {
            logger.error("Error: " + message)
          }
        }
      }
      else {
        for ( id <- response.getResourceSuccess ) {
          logger.info("Published " + record.simulationName + " with format " + formatConverter.toString + " to Learning Registry. See http://" + nodeHost + "/harvest/getrecord?by_doc_ID=T&request_ID=" + id)
        }
      }
    })
  }


  // manually wrap with an envelope and put it in JSON format
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
}
