package edu.colorado.phet.website.oai

import javax.servlet.ServletContext
import xml.Comment
import edu.colorado.phet.website.oai.OaiUtils.formatDateIso8601

/**
 * Converts our master format simulation data to NSDL_DC XML
 *
 * metadata guide for the format: http://nsdl.org/collection/metadata-guide.php
 * example: http://ns.nsdl.org/schemas/nsdl_dc/nsdl_dc1_v1.02.xml
 */
class NSDLDCConverter extends PhetFormatConverter {
  def getToFormat = "nsdl_dc"

  def convertRecord(record: SimulationRecord, servletContext: ServletContext): String = {
    // TODO: subject (for categories)
    // TODO: education level
    // TODO: type. DCMI maybe has <dc:type>InteractiveResource</dc:type>
    // TODO: verify that rights section is ok for now
    // TODO: license! (and maybe we need to specify which sims are available under which licenses?)

    <nsdl_dc:nsdl_dc schemaVersion="1.02.020"
                     xmlns:nsdl_dc="http://ns.nsdl.org/nsdl_dc_v1.02/"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xmlns:dc="http://purl.org/dc/elements/1.1/"
                     xmlns:dct="http://purl.org/dc/terms/"
                     xmlns:ieee="http://ltsc.ieee.org/xsd/LOM"
                     xsi:schemaLocation="http://ns.nsdl.org/nsdl_dc_v1.02/ http://ns.nsdl.org/schemas/nsdl_dc/nsdl_dc_v1.02.xsd">

      <!-- Unique identifier for the sim (the sim URI) -->
      <dc:identifier xsi:type="dct:URI">{record.simPageLink}</dc:identifier>

      <!-- English title -->
      {record.translatedTitles.map(str => <dc:title xml:lang={str.language}>{str.string}</dc:title>)}

      <!-- Descriptions -->
      {record.translatedDescriptions.map(str => <dc:description xml:lang={str.language}>{str.string}</dc:description>)}

      <!-- Rights -->
      <dc:rights>Free access / usage by everyone</dc:rights>
      <dc:rights>More licensing information available at http://phet.colorado.edu/en/about/licensing</dc:rights>
      <dc:rights>© 2011 University of Colorado</dc:rights>

      <!-- Access rights -->
      <dct:accessRights xsi:type="nsdl_dc:NSDLAccess">Free access</dct:accessRights>

      <!-- Credits -->
      <dc:publisher>PhET Interactive Simulations</dc:publisher>
      <dc:creator>PhET Interactive Simulations</dc:creator>
      {record.authors.map(author => <dc:creator>{author}</dc:creator>)}

      <!-- Available languages-->
      {record.languages.map(language => <dc:language>{language}</dc:language>)}

      <!-- Creation date (when the sim was first deployed to our production site) -->

      {if ( record.hasTimeCreated ) <dc:date xsi:type="dct:W3CDTF">{formatDateIso8601(record.dateCreated)}</dc:date> else new Comment("no creation date found")}
      {if ( record.hasTimeCreated ) <dct:created xsi:type="dct:W3CDTF">{formatDateIso8601(record.dateCreated)}</dct:created> else new Comment("no creation date found")}

      <!-- Modification date (includes changes of translations, actual simulation modifications, etc.) -->
      {if ( record.hasTimeUpdated ) <dct:modified xsi:type="dct:W3CDTF">{formatDateIso8601(record.dateUpdated)}</dct:modified> else new Comment("no modification date found")}

      <!-- Mime types used -->
      {record.mimeTypes.map(mimeType => <dc:format>{mimeType}</dc:format>)}

      <!-- Simulation version -->
      <dct:hasVersion>{record.versionString}</dct:hasVersion>

      <!-- Interaction information-->
      <ieee:interactivityType><ieee:source>LOMv1.0</ieee:source><ieee:source>active</ieee:source></ieee:interactivityType>
      <ieee:interactivityLevel><ieee:source>LOMv1.0</ieee:source><ieee:source>very high</ieee:source></ieee:interactivityLevel>
      <ieee:typicalLearningTime><!-- TODO Duration type, like PT1H30M or PT1M45S--></ieee:typicalLearningTime>

      <!-- Keywords -->
      {record.translatedTerms.map(term => term.map(str => <dc:subject xml:lang={str.language}>{str.string}</dc:subject>))}

      <!-- Recommended Audiences -->
      <dct:audience xsi:type="nsdl_dc:NSDLAudience">Learner</dct:audience>
      <dct:audience xsi:type="nsdl_dc:NSDLAudience">Educator</dct:audience>
      <dct:audience xsi:type="nsdl_dc:NSDLAudience">General Public</dct:audience>
    </nsdl_dc:nsdl_dc>.toString()
  }
}