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
    Physics classification example (LRE-0001)
                <classification>
                    <purpose>
                        <source>purposeValues</source>
                        <value>discipline</value>
                    </purpose>
                    <taxonPath>
                        <source>
                            <string language="x-none">LRE-0001</string>
                        </source>
                        <taxon>
                            <id>978</id>
                            <entry>
                                <string language="en">physics</string>
                            </entry>
                        </taxon>
                    </taxonPath>
                </classification>
     */

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
      <!-- LOM 1 General -->
      <general>
        <!-- identifier -->
        <identifier>
          <catalog>phet.colorado.edu</catalog>
          <entry>{record.simPageLink}</entry>
        </identifier>

        <!-- title -->
        <title>{convertLangString(record.translatedTitles)}</title>

        <!-- languages -->
        {record.languages.map(language => <language>{language}</language>)}

        <!-- description -->
        <description>{convertLangString(record.translatedDescriptions)}</description>

        <!-- keywords -->
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

      <!-- LOM 2 Lifecycle -->
      <lifeCycle>

        <!-- simulation version -->
        <version><string language="en">{record.versionString}</string></version>

        <status>
          <source>LOMv1.0</source>
          <value>final</value>
        </status>

        <!-- PhET -->
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

        <!-- other contributors -->
        {record.authors.map(author => <contribute>
          <role>
            <source>LOMv1.0</source>
            <value>author</value>
          </role>
          <entity>{vCardFromName(author)}</entity>
        </contribute>)}
      </lifeCycle>

      <!-- TODO consider LOM 3 Meta-Metadata -->

      <!-- LOM 4 Technical -->
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

      <!-- LOM 5 Educational -->
      <educational>
        <!-- interactivity type -->
        <interactivityType><source>LOMv1.0</source><value>active</value></interactivityType>

        <!-- learning resource type -->
        <learningResourceType><source>LOMv1.0</source><value>simulation</value></learningResourceType>
        <learningResourceType><source>LRE.learningResourceTypeValues</source><value>simulation</value></learningResourceType>
        <learningResourceType><source>DCMIType</source><value>InteractiveResource</value></learningResourceType>

        <!-- interactivity level -->
        <interactivityLevel><source>LOMv1.0</source><value>very high</value></interactivityLevel>

        <!-- semantic density -->
        <semanticDensity><source>LOMv1.0</source><value>very high</value></semanticDensity>

        <!-- intended user roles -->
        <intendedEndUserRole><source>LOMv1.0</source><value>teacher</value></intendedEndUserRole>
        <intendedEndUserRole><source>LRE.intendedEndUserRoleValues</source><value>teacher</value></intendedEndUserRole>
        <intendedEndUserRole><source>LOMv1.0</source><value>learner</value></intendedEndUserRole>
        <intendedEndUserRole><source>LRE.intendedEndUserRoleValues</source><value>learner</value></intendedEndUserRole>

        <!-- contexts -->
        <context><source>LOMv1.0</source><value>school</value></context>
        <context><source>LOMv1.0</source><value>higher education</value></context>
        <context><source>LRE.contextValues</source><value>compulsory education</value></context>

        <!-- TODO typical age range. set to European Schoolnet value -->
        <typicalAgeRange><string language="x-t-lre">12-20</string></typicalAgeRange>

        <!-- TODO difficulty: very easy / easy / medium / difficult / very difficult -->
        <difficulty><source>LOMv1.0</source><value>medium</value></difficulty>

        <!-- TODO Duration type, like PT1H30M or PT1M45S-->
        <!-- <typicalLearningTime></typicalLearningTime> -->

        <!-- TODO comments on how this learning object is to be used. URL? -->
        <!-- <description><string language="en"></string></description> -->

        <!-- languages (again, see above in LOM 1 General) -->
        {record.languages.map(language => <language>{language}</language>)}
      </educational>

      <!-- LOM 6 Rights -->
      <rights>
        <cost><source>LOMv1.0</source><value>no</value></cost>
        <cost><source>costValues</source><value>no</value></cost><!-- cost for IMS LODE ILOX -->

        <!-- TODO what about copyright and other restrictions? -->
        <copyrightAndOtherRestrictions><source>LOMv1.0</source><value>no</value></copyrightAndOtherRestrictions>
        <description><string language="en"><!-- TODO description of rights, etc--></string></description>
        <description><string language="x-t-cc-url">http://creativecommons.org/licenses/by/3.0/us/</string></description>
      </rights>

      <!-- LOM 7 Relation -->
      <relation>
        <!-- TODO: LOM 7 Relation related to other sims, or their teacher guides? -->
      </relation>

      <!-- TODO: LOM 8 annotations? (like other people's comments in our metadata) -->
      <!-- TODO: LOM 9 classification (s) -->
    </lom>
  }
}