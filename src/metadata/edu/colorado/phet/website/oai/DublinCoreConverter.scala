package edu.colorado.phet.website.oai

import javax.servlet.ServletContext
import xml.Node

/**
 * Converts our master format simulation data to Dublin Core XML
 *
 * Core elements: http://dublincore.org/documents/usageguide/elements.shtml
 */
class DublinCoreConverter extends PhetFormatConverter {
  def getToFormat = "oai_dc"

  def getSchemaURI = Some("http://www.openarchives.org/OAI/2.0/oai_dc/")

  def getLRSchemaTypes = List("oai_dc", "DC 1.1")

  def convertRecord(record: SimulationRecord): Node = {

    // note: no dc:date element is used currently, as there would be no unambiguous meaning to this
    // type uses InteractiveResource from http://dublincore.org/documents/dcmi-type-vocabulary/, and also "Simulation" as our general type
    // languages encoded in RFC 4646

    <oai_dc:dc xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xmlns:dc="http://purl.org/dc/elements/1.1/"
               xmlns:dcterms="http://purl.org/dc/terms/"
               xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd">
      <dc:identifier>{record.simPageLink}</dc:identifier>
      <dc:title xml:lang="en">{record.englishTitle}</dc:title>
      <dc:description xml:lang="en">{record.englishDescrption}</dc:description>

      <!-- credits -->
      <dc:publisher>PhET Interactive Simulations</dc:publisher>
      <dc:creator>PhET Interactive Simulations</dc:creator>
      {record.authors.map(author => <dc:creator>{author}</dc:creator>)}

      <!-- rights -->
      <dc:rights>{record.englishRights}</dc:rights>

      <!-- types -->
      <dc:type>InteractiveResource</dc:type>
      <dc:type>Simulation</dc:type>

      <!-- mime types of content -->
      {record.mimeTypes.map(mimeType => <dc:format>{mimeType}</dc:format>)}

      <!-- available simulation languages -->
      {record.languages.map(language => <dc:language>{language}</dc:language>)}

      <!-- other titles -->
      {record.translatedTitles.filter(str => str.language != "en").map(str => <dc:title xml:lang={str.language}>{str.string}</dc:title>)}

      <!-- other descriptions -->
      {record.translatedDescriptions.filter(str => str.language != "en").map(str => <dc:description xml:lang={str.language}>{str.string}</dc:description>)}

      <!-- keywords -->
      {record.translatedTerms.map(term => term.map(str => <dc:subject xml:lang={str.language}>{str.string}</dc:subject>))}

      <!-- subjects from our "categories" -->
      {record.translatedCategories.map(term => term.map(str => <dc:subject xml:lang={str.language}>{str.string}</dc:subject>))}
    </oai_dc:dc>
  }
}