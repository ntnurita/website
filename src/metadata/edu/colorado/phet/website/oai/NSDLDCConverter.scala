package edu.colorado.phet.website.oai

import javax.servlet.ServletContext
import org.dlese.dpc.xml.XMLFormatConverter

/**
 * Converts our master format simulation data to NSDL_DC XML
 *
 * metadata guide for the format: http://nsdl.org/collection/metadata-guide.php
 * example: http://ns.nsdl.org/schemas/nsdl_dc/nsdl_dc1_v1.02.xml
 */
class NSDLDCConverter extends XMLFormatConverter {
  def lastModified(p1: ServletContext) = 1308030299000L

  def getFromFormat = OaiUtils.MasterFormatName

  def getToFormat = "nsdl_dc"

  def convertXML(masterXML: String, servletContext: ServletContext): String = {

    val record = new SimulationRecord(masterXML)

    // TODO: subject (one each, repeat as needed)
    // TODO: education level
    // TODO: audience
    // TODO: type. DCMI maybe has <dc:type>InteractiveResource</dc:type>
    // TODO: verify that rights section is ok for now
    // TODO: access rights (NSDL specific)
    // TODO: license! (and maybe we need to specify which sims are available under which licenses?)

    // TODO: ieee things and below. start with IEEE stuff

    // TODO: consider Instructional Method parameter
    // TODO: consider Abstract

    <nsdl_dc:nsdl_dc schemaVersion="1.02.020"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xmlns:dc="http://purl.org/dc/elements/1.1/"
                     xmlns:dct="http://purl.org/dc/terms/"
                     xsi:schemaLocation="http://ns.nsdl.org/nsdl_dc_v1.02/ http://ns.nsdl.org/schemas/nsdl_dc/nsdl_dc_v1.02.xsd">
      <dc:identifier xsi:type="dct:URI">{record.simPageLink}</dc:identifier>
      <dc:title xml:lang="en">{record.englishTitle}</dc:title>
      {record.translatedTitles.filter(str=>str.language != "en").map(str => <dc:title xml:lang={str.language}>{str.string}</dc:title>)}
      <dc:description xml:lang="en">{record.englishDescrption}</dc:description>
      {record.translatedDescriptions.filter(str=>str.language != "en").map(str => <dc:description xml:lang={str.language}>{str.string}</dc:description>)}
      <dc:rights>Free access / usage by everyone</dc:rights>
      <dc:rights>More licensing information available at http://phet.colorado.edu/en/about/licensing</dc:rights>
      <dc:rights>© 2011 University of Colorado</dc:rights>
      <dc:creator>PhET Interactive Simulations</dc:creator>
      <dc:publisher>PhET Interactive Simulations</dc:publisher>
      {record.languages.map(language => <dc:language>{language}</dc:language>)}
      <dc:date xsi:type="dct:W3CDTF">{record.timeCreated}</dc:date>
      <dct:created xsi:type="dct:W3CDTF">{record.timeCreated}</dct:created>
      <dct:modified xsi:type="dct:W3CDTF">{record.timeUpdated}</dct:modified>
      {record.mimeTypes.map(mimeType => <dc:format>{mimeType}</dc:format>)}
      <dct:hasVersion>{record.versionString}</dct:hasVersion>
    </nsdl_dc:nsdl_dc>.toString
  }
}