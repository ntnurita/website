package edu.colorado.phet.website.oai

import javax.servlet.ServletContext
import xml.Node
import edu.colorado.phet.website.oai.OaiUtils.convertLangString

/**
 * Metadata conversion to the IMS LODE ILOX metadata format
 *
 * spec: http://lreforschools.eun.org/c/document_library/get_file?p_l_id=10970&folderId=12073&name=DLFE-1.pdf
 * thesaurus: http://demo.lexaurus.net/demo/linkeddata/LRE/LRE-0001/revision/4/attachment
 */
class IMSLODEILOXConverter extends PhetFormatConverter {
  def getToFormat = "oai_ilox"

  def convertRecord(record: SimulationRecord, servletContext: ServletContext): Node = {
    val runNowManifestation = if ( record.isJava ) "jnlp" else "experience"

    <work xmlns="http://www.imsglobal.org/xsd/imsloilox_v1p0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://www.imsglobal.org/xsd/imsloilox_v1p0 http://www.imsglobal.org/xsd/imsloilox_v1p0.xsd">
      <identifier>
        <catalog>phet.colorado.edu</catalog>
        <entry>{record.simPageLink}</entry>
      </identifier>

      <!-- all licenses that the simulation is covered by -->
      {record.licenseURLs.map(licenseURL => <description>
        <facet><vocabularyID>LRE.workDescriptionFacetValues</vocabularyID><value>license</value></facet>
        <metadata><schema>http://ltsc.ieee.org/xsd/LOM/imslode/ilox/any/rights</schema>
          <lom xsi:schemaLocation="http://ltsc.ieee.org/xsd/LOM/imslode/ilox/any/rights
               http://www.imsglobal.org/profile/lode/lodev1p0/lodev1p0_ilox_any_rights_lom_v1p0.xsd"
               xmlns="http://ltsc.ieee.org/xsd/LOM/imslode/ilox/any/rights">
            <rights>
              <cost><source>costValues</source><value>no</value></cost>
              <copyrightAndOtherRestrictions><source>copyrightAndOtherRestrictionsValues</source><value>yes</value></copyrightAndOtherRestrictions>
              <description><string language="x-t-rights-url">{licenseURL}</string></description>
            </rights>
          </lom>
        </metadata>
      </description>)}

      <!-- main simulation information -->
      <description>
        <facet>
          <vocabularyID>LRE.workDescriptionFacetValues</vocabularyID>
          <value>main</value>
        </facet>
        <metadata>
          <schema>http://ltsc.ieee.org/xsd/LOM</schema>
          <lom xmlns="http://ltsc.ieee.org/xsd/LOM"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://ltsc.ieee.org/xsd/LOM http://ltsc.ieee.org/xsd/lomv1.0/lomLoose.xsd">
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
                <entity>{OaiUtils.vCardFromName(author)}</entity>
              </contribute>)}
            </lifeCycle>

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
              <!-- learning resource type -->
              <learningResourceType><source>LRE.learningResourceTypeValues</source><value>simulation</value></learningResourceType>

              <!-- intended user roles -->
              <intendedEndUserRole><source>LRE.intendedEndUserRoleValues</source><value>teacher</value></intendedEndUserRole>
              <intendedEndUserRole><source>LRE.intendedEndUserRoleValues</source><value>learner</value></intendedEndUserRole>

              <!-- contexts -->
              <context><source>LRE.contextValues</source><value>compulsory education</value></context>
              <context><source>LRE.contextValues</source><value>higher education</value></context>
              <context><source>LRE.contextValues</source><value>distance education</value></context>

              <typicalAgeRange><string language="x-t-lre">{record.minGradeLevel.getLowAge + "-" + record.maxGradeLevel.getHighAge}</string></typicalAgeRange>

              <!-- languages (again, see above in LOM 1 General) -->
              {record.languages.map(language => <language>{language}</language>)}
            </educational>
          </lom>
        </metadata>
      </description>

      <!-- expression for each language -->
      {record.languages.map(language =>
      <expression>
        <dimension>
          <name><vocabularyID>LRE.expressionDimensionNameValues</vocabularyID><value>language</value></name>
          <parameter><vocabularyID>languageListValues</vocabularyID><value>{language}</value></parameter>
        </dimension>

        <!-- "Run Now" -->
        <manifestation>
          <name><vocabularyID>LRE.manifestationNameValues</vocabularyID><value>{runNowManifestation}</value></name>
          <item><location><uri>{record.runNowUrl(language)}</uri></location></item>
        </manifestation>

        <!-- "Download" -->
        <manifestation>
          <name><vocabularyID>LRE.manifestationNameValues</vocabularyID><value>package in</value></name>
          <parameter><vocabularyID>LRE.packageInValues</vocabularyID><value>jar</value></parameter>
          <item><location><uri>{record.downloadUrl(language)}</uri></location></item>
        </manifestation>

        <!-- thumbnail -->
        <manifestation>
          <name><vocabularyID>LRE.manifestationNameValues</vocabularyID><value>thumbnail</value></name>
          <item><location><uri>{record.thumbnailLink}</uri></location></item>
        </manifestation>

        <!-- simulation page -->
        <manifestation>
          <name><vocabularyID>LRE.manifestationNameValues</vocabularyID><value>landing page</value></name>
          <item><location><uri>{record.simPageLink}</uri></location></item>
        </manifestation>
      </expression>)}
</work>
  }
}