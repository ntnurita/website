// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.metadata

import java.io.File
import xml.{NodeSeq, XML}
import edu.colorado.phet.common.phetcommon.util.FileUtils

/**
 * For parsing the common core standards from the ASN
 */
object CommonCoreUtils {

  // produce CSV-related data for help with filling out initial standards conformance with simulations
  def main(args: Array[String]) {
    val file: File = new File("C:\\Users\\jon\\phet\\git\\website\\root\\standards\\CommonCoreMath.xml")

    val xml = XML.loadString(FileUtils.loadFileAsString(file))

    val statements = xml \\ "Statement"

    val rdfNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    val asnNamespace = "http://purl.org/ASN/schema/core/"
    val dctermsNamespace = "http://purl.org/dc/terms/"

    /*

      Example Record:

      <asn:Statement rdf:about="http://asn.jesandco.org/resources/S1143417">
        <asn:authorityStatus rdf:resource="http://purl.org/ASN/scheme/ASNAuthorityStatus/Original" />
        <asn:indexingStatus rdf:resource="http://purl.org/ASN/scheme/ASNIndexingStatus/Yes" />
        <asn:statementNotation>K.CC.1</asn:statementNotation>

        <dcterms:isPartOf rdf:resource="http://asn.jesandco.org/resources/D10003FB" />

        <dcterms:educationLevel rdf:resource="http://purl.org/ASN/scheme/ASNEducationLevel/K" />
        <dcterms:subject rdf:resource="http://purl.org/ASN/scheme/ASNTopic/math" />

        <dcterms:description xml:lang="en-US">1. Count to 100 by ones and by tens.</dcterms:description>

        <dcterms:language rdf:resource="http://id.loc.gov/vocabulary/iso639-2/eng" />
        <asn:identifier rdf:resource="http://purl.org/ASN/resources/S1143417" />
      </asn:Statement>
     */

    // header row for the CSV
    println("identifier\t" +
            "statement notation\t" +
            "education levels\t" +
            "description\t" +
            "comment\t" +
            "simulations")

    // print a row for
    println(statements.filter(node => !( node \ "identifier" ).isEmpty).map(node => List(
      // identifier, e.g. http://purl.org/ASN/resources/S1143417
      ( node \ "identifier" \ ( "@{" + rdfNamespace + "}resource" ) ).text,

      // statement notation, e.g. K.CC.1
      ( node \ "statementNotation" ).text,

      // education level, e.g. K,1,2,3
      ( node \ "educationLevel" ).map(
        n => {
          val uri = ( n \ ( "@{" + rdfNamespace + "}resource" ) ).text
          uri.split('/').last
        }
      ).mkString(","),

      // standard description
      ( node \ "description" ).text.replace('\n',' '),

      // additional comments
      ( node \ "comment" ).text.replace('\n',' ')
    ).mkString("\t")).mkString("\n"))
  }
}
