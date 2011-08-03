package edu.colorado.phet.website.oai

import javax.servlet.ServletContext
import edu.colorado.phet.website.oai.OaiUtils.formatDateIso8601
import xml.{Node, Comment}
import edu.colorado.phet.website.data.GradeLevel

/**
 * Converts our master format simulation data to NSDL_DC XML
 *
 * metadata guide for the format: http://nsdl.org/collection/metadata-guide.php
 * example: http://ns.nsdl.org/schemas/nsdl_dc/nsdl_dc1_v1.02.xml
 */
class NSDLDCConverter extends PhetFormatConverter {
  def getToFormat = "nsdl_dc"

  def convertRecord(record: SimulationRecord, servletContext: ServletContext): Node = {
    // TODO: verify that rights section is ok for now
    // TODO: license! (and maybe we need to specify which sims are available under which licenses?)

    // map grade levels to NSDL specific vocabulary. see http://nsdl.org/collection/educationLevel.php
    def gradeLevelMap(level: GradeLevel): String = level match {
      case GradeLevel.ELEMENTARY_SCHOOL => "Elementary School"
      case GradeLevel.MIDDLE_SCHOOL => "Middle School"
      case GradeLevel.HIGH_SCHOOL => "High School"
      case GradeLevel.UNIVERSITY => "Higher Education"
    }

    <nsdl_dc:nsdl_dc schemaVersion="1.02.020"
                     xmlns:nsdl_dc="http://ns.nsdl.org/nsdl_dc_v1.02/"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xmlns:dc="http://purl.org/dc/elements/1.1/"
                     xmlns:dct="http://purl.org/dc/terms/"
                     xmlns:ieee="http://www.ieee.org/xsd/LOMv1p0"
                     xsi:schemaLocation="http://ns.nsdl.org/nsdl_dc_v1.02/ http://ns.nsdl.org/schemas/nsdl_dc/nsdl_dc_v1.02.xsd">

      <!-- Unique identifier for the sim (the sim URI) -->
      <dc:identifier xsi:type="dct:URI">{record.simPageLink}</dc:identifier>

      <!-- Titles -->
      <dc:title xml:lang="en">{record.englishTitle}</dc:title>

      <!-- Descriptions -->
      <dc:description xml:lang="en">{record.englishDescrption}</dc:description>

      <!-- Types -->
      <dc:type xsi:type="dct:DCMIType">InteractiveResource</dc:type>
      <dc:type xsi:type="nsdl_dc:NSDLType">Instructional Material</dc:type>
      <dc:type xsi:type="nsdl_dc:NSDLType">Simulation</dc:type>
      <dc:type xsi:type="nsdl_dc:NSDLType">Activity</dc:type> <!-- activities are available on the simulation web page -->

      <!-- Rights -->
      <dc:rights>{record.englishRights}</dc:rights>

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
      <ieee:interactivityType>active</ieee:interactivityType>
      <ieee:interactivityLevel>very high</ieee:interactivityLevel>

      <!-- Educational Level -->
      {record.gradeLevels.map(level => <dct:educationLevel xsi:type="nsdl_dc:NSDLEdLevel">{gradeLevelMap(level)}</dct:educationLevel>)}

      <!-- Keywords -->
      {record.translatedTerms.map(term => <dc:subject xml:lang="en">{OaiUtils.englishString(term)}</dc:subject>)}

      <!-- subjects from our "categories" -->
      {record.translatedCategories.map(term => <dc:subject xml:lang="en">{OaiUtils.englishString(term)}</dc:subject>)}

      <!-- subjects from our LRE vocabulary -->
      {record.lreTerms.map(term => <dc:subject xml:lang="en">{term._2}</dc:subject>)}

      <!-- NSDL Science Literacy Maps information -->
      {record.NSDLScienceLiteracyMapKeys.map(key => <dct:conformsTo>{key}</dct:conformsTo>)}

      <!-- Recommended Audiences -->
      <dct:audience xsi:type="nsdl_dc:NSDLAudience">Learner</dct:audience>
      <dct:audience xsi:type="nsdl_dc:NSDLAudience">Educator</dct:audience>
      <dct:audience xsi:type="nsdl_dc:NSDLAudience">General Public</dct:audience>
    </nsdl_dc:nsdl_dc>
  }
}