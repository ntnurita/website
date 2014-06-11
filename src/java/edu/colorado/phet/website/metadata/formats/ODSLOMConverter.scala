// Copyright 2002-2013, University of Colorado
package edu.colorado.phet.website.metadata.formats

import xml.Node
import edu.colorado.phet.website.constants.Licenses
import edu.colorado.phet.website.metadata.MetadataUtils._
import edu.colorado.phet.website.metadata.{MetadataUtils, SimulationRecord, PhetMetadataConverter}

/**
 * Converts our master format simulation data to IEEE LOM, with modifications needed for ODS
 *
 * NOTE: do not change the package / class name of this, since it is referenced in the server-side web.xml of the
 * jOAI webapp
 */
trait ODSLOMConverter extends PhetMetadataConverter {
  def getToFormat = "ods-lom"

  override def toString = getToFormat

  def getSchemaURI = Some("http://ltsc.ieee.org/xsd/LOM")

  def getLRSchemaTypes = List("IEEE LOM 2002")

  def convertRecord(record: SimulationRecord): Node = {

    <lom:lom xmlns:lom="http://ltsc.ieee.org/xsd/LOM" xmlns:xalan="http://xml.apache.org/xalan">
      <!-- LOM 1 General -->
      <lom:general>
        <!-- identifier -->
        <lom:identifier>
          <lom:catalog>phet.colorado.edu</lom:catalog>
          <lom:entry>
            {record.simPageLink}
          </lom:entry>
        </lom:identifier>

        <!-- title -->
        <lom:title>
          {convertNSLomLangString(record.translatedTitles)}
        </lom:title>

        <!-- languages -->{record.languages.map(language => <lom:language>
        {language}
      </lom:language>)}<!-- description -->
        <lom:description>
          {convertNSLomLangString(record.translatedDescriptions)}
        </lom:description>

        <!-- keywords -->{record.translatedTerms.map(term => <lom:keyword>
        {convertNSLomLangString(term)}
      </lom:keyword>)}<lom:structure>
        <lom:source>LOMv1.0</lom:source>
        <lom:value>atomic</lom:value>
      </lom:structure>
        <lom:aggregationLevel>
          <lom:source>LOMv1.0</lom:source>
          <lom:value>1</lom:value>
        </lom:aggregationLevel>
      </lom:general>

      <!-- LOM 2 Lifecycle -->
      <lom:lifeCycle>

        <!-- simulation version -->
        <lom:version>
          <lom:language language="en">
            {record.versionString}
          </lom:language>
        </lom:version>

        <lom:status>
          <lom:source>LOMv1.0</lom:source>
          <lom:value>final</lom:value>
        </lom:status>

        <!-- PhET -->
        <lom:contribute>
          <lom:role>
            <lom:source>LOMv1.0</lom:source>
            <lom:value>publisher</lom:value>
          </lom:role>
          <lom:entity>
            <![CDATA[
            BEGIN:VCARD
            FN:PhET Interactive Simulations
            N:;;PhET Interactive Simulations
            ORG:PhET Interactive Simulations
            VERSION:3.0
            END:VCARD
            ]]>
          </lom:entity>
        </lom:contribute>

        <!-- other contributors -->{record.authors.map(author => <lom:contribute>
        <lom:role>
          <lom:source>LOMv1.0</lom:source>
          <lom:value>author</lom:value>
        </lom:role>
        <lom:entity>
          {MetadataUtils.vCardFromName(author)}
        </lom:entity>
      </lom:contribute>)}
      </lom:lifeCycle>

      <!-- TODO consider LOM 3 Meta-Metadata -->

      <!-- LOM 4 Technical -->
      <lom:technical>
        {record.mimeTypes.map(mimeType => <lom:format>
        {mimeType}
      </lom:format>)}<lom:size>
        {( record.kilobytes * 1000 ).toString}
      </lom:size>
        <lom:location>
          {record.simPageLink}
        </lom:location>
        <lom:installationRemarks>
          <lom:language language="en">Press either "Run Now!" to run the simulation, or "Download" to download it to your computer to run later</lom:language>
        </lom:installationRemarks>
        <lom:otherPlatformRequirements>
          <lom:language language="en">
            {record.englishSoftwareRequirements}
          </lom:language>
        </lom:otherPlatformRequirements>
      </lom:technical>

      <!-- LOM 5 Educational -->
      <lom:educational>
        <!-- learning resource type -->
        <lom:learningResourceType>
          <lom:source>LOMv1.0</lom:source> <lom:value>simulation</lom:value>
        </lom:learningResourceType>

        <!-- interactivity level -->
        <lom:interactivityLevel>
          <lom:source>LOMv1.0</lom:source> <lom:value>very high</lom:value>
        </lom:interactivityLevel>

        <!-- semantic density -->
        <lom:semanticDensity>
          <lom:source>LOMv1.0</lom:source> <lom:value>very high</lom:value>
        </lom:semanticDensity>

        <!-- intended user roles -->
        <lom:intendedEndUserRole>
          <lom:source>LOMv1.0</lom:source> <lom:value>teacher</lom:value>
        </lom:intendedEndUserRole>
        <lom:intendedEndUserRole>
          <lom:source>LOMv1.0</lom:source> <lom:value>learner</lom:value>
        </lom:intendedEndUserRole>

        <!-- contexts -->
        <lom:context>
          <lom:source>LOMv1.0</lom:source> <lom:value>school</lom:value>
        </lom:context>
        <lom:context>
          <lom:source>LOMv1.0</lom:source> <lom:value>higher education</lom:value>
        </lom:context>

        <lom:typicalAgeRange>
          <lom:language language="x-t-lre">
            {record.minGradeLevel.getLowAge + "-" + record.maxGradeLevel.getHighAge}
          </lom:language>
        </lom:typicalAgeRange>

        <!-- OPTION difficulty: very easy / easy / medium / difficult / very difficult -->
        <!--<difficulty><lom:source>LOMv1.0</lom:source><lom:value>medium</lom:value></difficulty>-->

        <!-- languages (again, see above in LOM 1 General) -->{record.languages.map(language => <lom:language>
        {language}
      </lom:language>)}
      </lom:educational>

      <!-- LOM 6 Rights -->
      <lom:rights>
        <lom:cost>
          <lom:source>LOMv1.0</lom:source> <lom:value>no</lom:value>
        </lom:cost>

        <lom:copyrightAndOtherRestrictions>
          <lom:source>LOMv1.0</lom:source> <lom:value>yes</lom:value>
        </lom:copyrightAndOtherRestrictions>
        <lom:description>
          {record.licenseURLs.map(licenseURL => licenseURL match {
          case Licenses.CC_BY_3 => <lom:string language="x-t-cc-url">
            {Licenses.CC_BY_3}
          </lom:string>
          case Licenses.CC_GPL_2 => <lom:string language="x-t-rights-url">
            {Licenses.CC_GPL_2}
          </lom:string>
        })}{convertNSLomLangString(record.rights)}
        </lom:description>
      </lom:rights>

      <!-- LOM 7 Relation -->
      <lom:relation>
        <!-- TODO: LOM 7 Relation related to other sims, or their teacher guides? -->
      </lom:relation>

      <!-- TODO: LOM 8 annotations? (like other people's comments in our metadata) -->

      <!-- LRE 9 Classification -->
      <!-- LRE terms -->{record.lreTerms.map(term => <lom:classification>
      <lom:purpose>
        <lom:source>LOMv1.0</lom:source>
        <lom:value>discipline</lom:value>
      </lom:purpose>
      <lom:taxonPath>
        <lom:source>
          <lom:language language="x-none">LRE-0001</lom:language>
        </lom:source>
        <lom:taxon>
          <lom:id>
            {term._1}
          </lom:id>
          <lom:entry>
            <lom:language language="en">
              {term._2}
            </lom:language>
          </lom:entry>
        </lom:taxon>
      </lom:taxonPath>
    </lom:classification>)}
    </lom:lom>
  }
}