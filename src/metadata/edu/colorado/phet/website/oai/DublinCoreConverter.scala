package edu.colorado.phet.website.oai

import javax.servlet.ServletContext
import xml.Node

/**
 * Converts our master format simulation data to Dublin Core XML
 *
 * Core elements: http://dublincore.org/documents/usageguide/elements.shtml
 *
 * TODO: integrate DC parts of NSDL_DC and others into here?
 */
class DublinCoreConverter extends PhetFormatConverter {
  def getToFormat = "oai_dc"

  def convertRecord(record: SimulationRecord, servletContext: ServletContext): Node = {
    // TODO: dc:subject (for categories)

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
      <dc:publisher>PhET Interactive Simulations</dc:publisher>
      <dc:creator>PhET Interactive Simulations</dc:creator>
      {record.authors.map(author => <dc:creator>{author}</dc:creator>)}
      <dc:rights>Free access / usage by everyone</dc:rights>
      <dc:rights>More licensing information available at http://phet.colorado.edu/en/about/licensing</dc:rights>
      <dc:rights>© 2011 University of Colorado</dc:rights>
      <dc:type>InteractiveResource</dc:type>
      <dc:type>Simulation</dc:type>
      {record.mimeTypes.map(mimeType => <dc:format>{mimeType}</dc:format>)}
      {record.languages.map(language => <dc:language>{language}</dc:language>)}
      {record.translatedTitles.filter(str => str.language != "en").map(str => <dc:title xml:lang={str.language}>{str.string}</dc:title>)}
      {record.translatedDescriptions.filter(str => str.language != "en").map(str => <dc:description xml:lang={str.language}>{str.string}</dc:description>)}
      {record.translatedTerms.map(term => term.map(str => <dc:subject xml:lang={str.language}>{str.string}</dc:subject>))}
    </oai_dc:dc>
  }
}