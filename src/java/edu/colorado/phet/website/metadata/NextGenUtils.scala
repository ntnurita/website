// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.metadata

import java.io.File
import xml.{NodeSeq, XML}
import edu.colorado.phet.common.phetcommon.util.FileUtils

/**
 * For parsing the next gen standards from the ASN
 */
object NextGenUtils {

  // produce CSV-related data for help with filling out initial standards conformance with simulations
  def main(args: Array[String]) {
    val file: File = new File("C:\\Users\\jon\\phet\\git\\website\\root\\standards\\NextGenScience.xml")

    val xml = XML.loadString(FileUtils.loadFileAsString(file))

    val statements = xml \\ "Statement"

    val rdfNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    val asnNamespace = "http://purl.org/ASN/schema/core/"
    val dctermsNamespace = "http://purl.org/dc/terms/"

    /*

      Old Example Record (Common Core math, not for this):
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

      Example Record:

      <asn:Statement rdf:about="http://asn.jesandco.org/resources/S2454557">
        <asn:authorityStatus rdf:resource="http://purl.org/ASN/scheme/ASNAuthorityStatus/Original"/>
        <asn:indexingStatus rdf:resource="http://purl.org/ASN/scheme/ASNIndexingStatus/Yes"/>
        <asn:statementNotation>HS-PS4-2</asn:statementNotation>
        <dcterms:isPartOf rdf:resource="http://asn.jesandco.org/resources/D2454348"/>
        <dcterms:educationLevel rdf:resource="http://purl.org/ASN/scheme/ASNEducationLevel/9"/>
        <dcterms:educationLevel rdf:resource="http://purl.org/ASN/scheme/ASNEducationLevel/10"/>
        <dcterms:educationLevel rdf:resource="http://purl.org/ASN/scheme/ASNEducationLevel/11"/>
        <dcterms:educationLevel rdf:resource="http://purl.org/ASN/scheme/ASNEducationLevel/12"/>
        <dcterms:subject rdf:resource="http://purl.org/ASN/scheme/ASNTopic/science"/>
        <asn:statementLabel xml:lang="en-US">Performance Expectation</asn:statementLabel>
        <dcterms:description xml:lang="en-US">
          Evaluate questions about the advantages of using a digital transmission and storage of information.
        </dcterms:description>
        <asn:comment xml:lang="en-US">
          Clarification Statement: Examples of advantages could include that digital information is stable because it can be stored reliably in computer memory, transferred easily, and copied and shared rapidly. Disadvantages could include issues of easy deletion, security, and theft.
        </asn:comment>
        <dcterms:language rdf:resource="http://id.loc.gov/vocabulary/iso639-2/eng"/>
        <asn:conceptTerm rdf:resource="http://purl.org/ASN/scheme/NGSSTopic/19"/>
        <asn:comprisedOf rdf:resource="http://asn.jesandco.org/resources/S2471780"/>
        <asn:comprisedOf rdf:resource="http://asn.jesandco.org/resources/S2471795"/>
        <asn:comprisedOf rdf:resource="http://asn.jesandco.org/resources/S2471810"/>
        <asn:comprisedOf rdf:resource="http://asn.jesandco.org/resources/S2471812"/>
        <asn:comprisedOf rdf:resource="http://asn.jesandco.org/resources/S2471813"/>
        <asn:alignTo rdf:resource="http://corestandards.org/ELA-Literacy/RST/11-12/1"/>
        <asn:alignTo rdf:resource="http://corestandards.org/ELA-Literacy/RST/11-12/8"/>
        <gemq:isChildOf rdf:resource="http://asn.jesandco.org/resources/S2467915"/>
       </asn:Statement>
     */

    // header row for the CSV
    println("URI\t" +
            "Authority Status\t" +
            "Indexing Status\t" +
            "Statement Notation\t" +
            "Is Part Of\t" +
            "Subject\t" +
            "Education Levels\t" +
            "Statement Label\t" +
            "Description\t" +
            "Comment\t" +
            "Language\t" +
            "Concept Term\t" +
            "Comprised Of\t" +
            "Align To\t" +
            "Is Child Of"
    )

      println( statements.map( node => List(
        ( node \ ( "@{" + rdfNamespace + "}about" ) ).text,
        ( node \ "authorityStatus" \ ( "@{" + rdfNamespace + "}resource" ) ).text.split('/').last,
        ( node \ "indexingStatus" \ ( "@{" + rdfNamespace + "}resource" ) ).text.split('/').last,
        ( node \ "statementNotation" ).text,
        ( node \ "isPartOf" ).map(
          n => {
            ( n \ ( "@{" + rdfNamespace + "}resource" ) ).text
          }
        ).mkString(","),
        ( node \ "subject" ).map(
          n => {
            ( n \ ( "@{" + rdfNamespace + "}resource" ) ).text
          }
        ).mkString(","),
        ( node \ "educationLevel" ).map(
          n => {
            val uri = ( n \ ( "@{" + rdfNamespace + "}resource" ) ).text
            uri.split('/').last
          }
        ).mkString(","),
        ( node \ "statementLabel" ).text,
        ( node \ "description" ).text.replace('\n',' '),
        ( node \ "comment" ).text.replace('\n',' '),
        ( node \ "language" ).text.replace('\n',' '),
        ( node \ "conceptTerm" ).map(
          n => {
            ( n \ ( "@{" + rdfNamespace + "}resource" ) ).text
          }
        ).mkString(","),
        ( node \ "comprisedOf" ).map(
          n => {
            ( n \ ( "@{" + rdfNamespace + "}resource" ) ).text
          }
        ).mkString(","),
        ( node \ "alignTo" ).map(
          n => {
            ( n \ ( "@{" + rdfNamespace + "}resource" ) ).text
          }
        ).mkString(","),
        ( node \ "isChildOf" ).map(
          n => {
            ( n \ ( "@{" + rdfNamespace + "}resource" ) ).text
          }
        ).mkString(",")
      ).mkString( "\t" ) ).mkString( "\n" ) )
    // print a row for
//    println(statements.filter(node => !( node \ "identifier" ).isEmpty).map(node => List(
//      // identifier, e.g. http://purl.org/ASN/resources/S1143417
//      ( node \ "identifier" \ ( "@{" + rdfNamespace + "}resource" ) ).text,
//
//      // statement notation, e.g. K.CC.1
//      ( node \ "statementNotation" ).text,
//
//      // education level, e.g. K,1,2,3
//      ( node \ "educationLevel" ).map(
//        n => {
//          val uri = ( n \ ( "@{" + rdfNamespace + "}resource" ) ).text
//          uri.split('/').last
//        }
//      ).mkString(","),
//
//      // standard description
//      ( node \ "description" ).text.replace('\n',' '),
//
//      // additional comments
//      ( node \ "comment" ).text.replace('\n',' ')
//    ).mkString("\t")).mkString("\n"))
  }
}
