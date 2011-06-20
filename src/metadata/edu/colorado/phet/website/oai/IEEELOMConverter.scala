package edu.colorado.phet.website.oai

import javax.servlet.ServletContext
import xml.{Unparsed, Node, NodeSeq}

/**
 * Converts our master format simulation data to IEEE LOM
 *
 * metadata format spec: http://ltsc.ieee.org/wg12/files/LOM_1484_12_1_v1_Final_Draft.pdf
 * XML binding spec: http://ltsc.ieee.org/wg12/files/IEEE_1484_12_03_d8_submitted.pdf
 */
class IEEELOMConverter extends PhetFormatConverter {
  def getToFormat = "lom"

  /**
   * Turns a sequence of translated strings into an IEEE string (same meaning, but in different languages) that can be included in XML
   */
  def convertLangString(lang: Seq[LanguageString]): NodeSeq = lang.map(str => <string language={str.language}>{str.string}</string>)

  def convertRecord(record: SimulationRecord, servletContext: ServletContext): NodeSeq = {

    def vCardFromName(name: String): Node = Unparsed("<![CDATA[BEGIN:VCARD\nFN:" + name + "\nVERSION:3.0\nEND:VCARD]]>")

    /*
begin:vcard
fn:Jonathan Olson
n:Olson;Jonathan
email;internet:olsonsjc@gmail.com
version:2.1
end:vcard
     */
    // NOTE: do we need LOM 3 (meta-metadata)

    <lom xmlns="http://ltsc.ieee.org/xsd/LOM"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://ltsc.ieee.org/xsd/LOM http://ltsc.ieee.org/xsd/lomv1.0/lom.xsd">
      <general>
        <identifier>
          <catalog>phet.colorado.edu</catalog>
          <entry>{record.simPageLink}</entry>
        </identifier>
        <title>{convertLangString(record.translatedTitles)}</title>
        {record.languages.map(language => <language>{language}</language>)}
        <description>{convertLangString(record.translatedDescriptions)}</description>
        {record.translatedTerms.map(term => <keyword>{convertLangString(term)}</keyword>)}
        <structure>
          <source>LOMv1.0</source>
          <value>atomic</value>
        </structure>
        <aggregationLevel>
          <source>LOMv1.0</source>
          <value>1</value>
        </aggregationLevel>
      </general>
      <lifeCycle>
        <version><string language="en">{record.versionString}</string></version>
        <status>
          <source>LOMv1.0</source>
          <value>final</value>
        </status>
        <contribute>
          <role>
            <source>LOMv1.0</source>
            <value>publisher</value>
          </role>
          <entity><![CDATA[BEGIN:VCARD
FN:PhET Interactive Simulations
N:;;PhET Interactive Simulations
ORG:PhET Interactive Simulations
VERSION:3.0
END:VCARD]]></entity>
        </contribute>
        {record.authors.map(author => <contribute>
          <role>
            <source>LOMv1.0</source>
            <value>author</value>
          </role>
          <entity>{vCardFromName(author)}</entity>
        </contribute>)}
      </lifeCycle>
      <technical>
        {record.mimeTypes.map(mimeType => <format>{mimeType}</format>)}
        <size>{( record.kilobytes * 1000 ).toString}</size>
        <location>{record.simPageLink}</location>
        <installationRemarks>
          <string language="en">Press either "Run Now!" to run the simulation, or "Download" to download it to your computer to run later</string>
        </installationRemarks>
        <otherPlatformRequirements>
          <string language="en">{record.englishSoftwareRequirements}</string>
        </otherPlatformRequirements>
      </technical>
      <educational>
        <interactivityType><source>LOMv1.0</source><value>active</value></interactivityType>
        <learningResourceType><source>LOMv1.0</source><value>simulation</value></learningResourceType>
        <interactivityLevel><source>LOMv1.0</source><value>very high</value></interactivityLevel>
        <semanticDensity><source>LOMv1.0</source><value>very high</value></semanticDensity>
        <intendedEndUserRole><source>LOMv1.0</source><value>teacher</value></intendedEndUserRole>
        <intendedEndUserRole><source>LOMv1.0</source><value>learner</value></intendedEndUserRole>
        <context><source>LOMv1.0</source><value>school</value></context>
        <context><source>LOMv1.0</source><value>higher education</value></context>
        <typicalAgeRange>
          <!-- TODO typicalAgeRange -->
          <string language="en">7-</string>
        </typicalAgeRange>
        <difficulty><source>LOMv1.0</source><value><!-- TODO very easy / easy / medium / difficult / very difficult --></value></difficulty>
        <typicalLearningTime><!-- TODO Duration type, like PT1H30M or PT1M45S--></typicalLearningTime>
        <description><string language="en"><!-- TODO comments on how this learning object is to be used. URL? --></string></description>
        {record.languages.map(language => <language>{language}</language>)}
      </educational>
      <rights>
        <cost><source>LOMv1.0</source><value>no</value></cost>

        <!-- TODO what about copyright and other restrictions? -->
        <copyrightAndOtherRestrictions><source>LOMv1.0</source><value>no</value></copyrightAndOtherRestrictions>
        <description><string language="en"><!-- TODO description of rights, etc--></string></description>
      </rights>
      <relation>
        <!-- TODO: LOM 7 Relation related to other sims / etc? -->
      </relation>
      <!-- TODO: LOM 8 annotations? (like other people's comments in our metadata) -->
      <!-- TODO: LOM 9 classification (s) -->
    </lom>
  }
}