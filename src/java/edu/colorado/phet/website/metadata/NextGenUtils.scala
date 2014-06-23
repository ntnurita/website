// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.website.metadata

import java.io.File
import xml.{NodeSeq, XML}
import edu.colorado.phet.common.phetcommon.util.FileUtils
import collection.mutable

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

    val conceptMap = new mutable.HashMap[String,String]()

    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/1", "Forces and Interactions: Pushes and Pulls" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/2", "Interdependent Relationships in Ecosystems: Animals, Plants, and Their Environment" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/3", "Weather and Climate" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/4", "Waves: Light and Sound" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/5", "Structure, Function, and Information Processing" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/6", "Space Systems: Patterns and Cycles" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/7", "Structure and Properties of Matter" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/8", "Interdependent Relationships in Ecosystems" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/9", "Earth's Systems: Processes that Shape the Earth" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/10", "Engineering Design" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/11", "Forces and Interactions" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/12", "Inheritance and Variation of Traits: Life Cycles and Traits" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/13", "Energy" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/14", "Waves: Waves and Information" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/15", "Matter and Energy in Organisms and Ecosystems" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/16", "Earth's Systems" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/17", "Space Systems: Stars and the Solar System" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/18", "Chemical Reactions" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/19", "Waves and Electromagnetic Radiation" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/20", "Growth, Development, and Reproduction of Organisms" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/21", "Natural Selection and Adaptations" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/22", "Space Systems" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/23", "History of Earth" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/24", "Human Impacts" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/25", "Structure and Function" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/26", "Inheritance and Variation of Traits" )
    conceptMap.put( "http://purl.org/ASN/scheme/NGSSTopic/27", "Natural Selection and Evolution" )

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
            "Is Child Of\t" +
            "See Also\t" +
            "Concept Term"
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
        ).mkString(","),
        ( node \ "seeAlso" ).map(
          n => {
            ( n \ ( "@{" + rdfNamespace + "}resource" ) ).text
          }
        ).mkString(","),
        ( node \ "conceptTerm" ).map(
          n => {
            conceptMap.get( ( n \ ( "@{" + rdfNamespace + "}resource" ) ).text ).get
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
