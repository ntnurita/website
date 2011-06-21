package edu.colorado.phet.website.oai

import javax.servlet.ServletContext
import xml.Node

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
      <description>
        <facet>
          <vocabularyID>LRE.workDescriptionFacetValues</vocabularyID>
          <value>main</value>
        </facet>
        <metadata>
          <schema>http://ltsc.ieee.org/xsd/LOM</schema>
          <!-- include our entire IEEE LOM metadata inside here -->
          {new IEEELOMConverter().convertRecord(record, servletContext)}
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