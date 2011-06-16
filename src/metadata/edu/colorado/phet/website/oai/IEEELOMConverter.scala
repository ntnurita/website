package edu.colorado.phet.website.oai

import javax.servlet.ServletContext
import org.dlese.dpc.xml.XMLFormatConverter
import xml.NodeSeq

/**
 * Converts our master format simulation data to IEEE LOM
 *
 * metadata format spec: http://ltsc.ieee.org/wg12/files/LOM_1484_12_1_v1_Final_Draft.pdf
 * XML binding spec: http://ltsc.ieee.org/wg12/files/IEEE_1484_12_03_d8_submitted.pdf
 */
class IEEELOMConverter extends XMLFormatConverter {
  def lastModified(p1: ServletContext) = 1308030299000L

  def getFromFormat = OaiUtils.MasterFormatName

  def getToFormat = "lom"

  /**
   * Turns a sequence of translated strings into an IEEE string (same meaning, but in different languages) that can be included in XML
   */
  def convertLangString(lang: Seq[LanguageString]): NodeSeq = lang.map(str => <ieee:string xml:lang={str.language}>{str.string}</ieee:string>)

  def convertXML(masterXML: String, servletContext: ServletContext): String = {

    val record = new SimulationRecord(masterXML)

    /*
begin:vcard
fn:Jonathan Olson
n:Olson;Jonathan
email;internet:olsonsjc@gmail.com
version:2.1
end:vcard
     */
    // TODO: LOM 3 (meta-metadata)

    // TODO: investigate lack of date / time information here?

    <ieee:lom xmlns:ieee="http://ltsc.ieee.org/xsd/LOM"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://ltsc.ieee.org/xsd/LOM http://ltsc.ieee.org/xsd/lomv1.0/lom.xsd">
      <ieee:general>
        <ieee:identifier>
          <ieee:catalog>URI</ieee:catalog>
          <ieee:entry>{record.simPageLink}</ieee:entry>
        </ieee:identifier>
        <ieee:title>{convertLangString(record.translatedTitles)}</ieee:title>
        {record.languages.map(language => <ieee:language>{language}</ieee:language>)}
        <ieee:description>{convertLangString(record.translatedDescriptions)}</ieee:description>
        <!-- TODO: LOM 1.5 keywords, each is LangString -->
        <ieee:structure>
          <ieee:source>LOMv1.0</ieee:source>
          <ieee:value>atomic</ieee:value>
        </ieee:structure>
        <ieee:aggregationLevel>
          <ieee:source>LOMv1.0</ieee:source>
          <ieee:value>1</ieee:value>
        </ieee:aggregationLevel>
      </ieee:general>
      <ieee:lifeCycle>
        <ieee:version><ieee:string xml:lang="en">{record.versionString}</ieee:string></ieee:version>
        <ieee:status>
          <ieee:source>LOMv1.0</ieee:source>
          <ieee:value>final</ieee:value>
        </ieee:status>
        <ieee:contribute>
          <ieee:role>
            <ieee:source>LOMv1.0</ieee:source>
            <ieee:value>publisher</ieee:value>
          </ieee:role>
          <ieee:entity><![CDATA[BEGIN:VCARD
FN:PhET Interactive Simulations
N:;;PhET Interactive Simulations
ORG:PhET Interactive Simulations
VERSION:3.0
END:VCARD]]></ieee:entity>
        </ieee:contribute>
        <!-- TODO: author / library / thanks contributions -->
      </ieee:lifeCycle>
      <ieee:technical>
        {record.mimeTypes.map(mimeType => <ieee:format>{mimeType}</ieee:format>)}
        <ieee:size>{( record.kilobytes * 1000 ).toString}</ieee:size>
        <ieee:location xsi:type="URI">{record.simPageLink}</ieee:location>
        <ieee:installationRemarks>
          <ieee:string xml:lang="en">Press either "Run Now!" to run the simulation, or "Download" to download it to your computer to run later</ieee:string>
        </ieee:installationRemarks>
        <ieee:otherPlatformRequirements>
          <ieee:string xml:lang="en">{record.englishSoftwareRequirements}</ieee:string>
        </ieee:otherPlatformRequirements>
      </ieee:technical>
      <ieee:educational>
        <ieee:interactivityType><ieee:source>LOMv1.0</ieee:source><ieee:source>active</ieee:source></ieee:interactivityType>
        <ieee:learningResourceType><ieee:source>LOMv1.0</ieee:source><ieee:source>simulation</ieee:source></ieee:learningResourceType>
        <ieee:learningResourceType><ieee:source>DCMIType</ieee:source><ieee:value>InteractiveResource</ieee:value></ieee:learningResourceType>
        <ieee:interactivityLevel><ieee:source>LOMv1.0</ieee:source><ieee:source>very high</ieee:source></ieee:interactivityLevel>
        <ieee:semanticDensity><ieee:source>LOMv1.0</ieee:source><ieee:source>very high</ieee:source></ieee:semanticDensity>
        <ieee:intendedEndUserRole><ieee:source>LOMv1.0</ieee:source><ieee:source>teacher</ieee:source></ieee:intendedEndUserRole>
        <ieee:intendedEndUserRole><ieee:source>LOMv1.0</ieee:source><ieee:source>learner</ieee:source></ieee:intendedEndUserRole>
        <ieee:context><ieee:source>LOMv1.0</ieee:source><ieee:source>school</ieee:source></ieee:context>
        <ieee:context><ieee:source>LOMv1.0</ieee:source><ieee:source>higher education</ieee:source></ieee:context>
        <ieee:typicalAgeRange>
          <!-- TODO typicalAgeRange -->
          <ieee:string xml:lang="en">7-</ieee:string>
        </ieee:typicalAgeRange>
        <ieee:difficulty><ieee:source>LOMv1.0</ieee:source><ieee:source><!-- TODO very easy / easy / medium / difficult / very difficult --></ieee:source></ieee:difficulty>
        <ieee:typicalLearningTime><!-- TODO Duration type, like PT1H30M or PT1M45S--></ieee:typicalLearningTime>
        <ieee:description><ieee:string xml:lang="en"><!-- TODO comments on how this learning object is to be used. URL? --></ieee:string></ieee:description>
        {record.languages.map(language => <ieee:language>{language}</ieee:language>)}
      </ieee:educational>
      <ieee:rights>
        <ieee:cost><ieee:source>LOMv1.0</ieee:source><ieee:source>no</ieee:source></ieee:cost>

        <!-- TODO what about copyright and other restrictions? -->
        <ieee:copyrightAndOtherRestrictions><ieee:source>LOMv1.0</ieee:source><ieee:source>no</ieee:source></ieee:copyrightAndOtherRestrictions>
        <ieee:description><ieee:string xml:lang="en"><!-- TODO description of rights, etc--></ieee:string></ieee:description>
      </ieee:rights>
      <ieee:relation>
        <!-- TODO: LOM 7 Relation related to other sims / etc? -->
      </ieee:relation>
      <!-- TODO: LOM 8 annotations? (like other people's comments in our metadata) -->
      <!-- TODO: LOM 9 classification (s) -->
    </ieee:lom>.toString
  }
}