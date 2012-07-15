package edu.colorado.phet.website.metadata

import edu.colorado.phet.website.translation.PhetLocalizer
import formats.{NSDLDCConverter, IMSLODEILOXConverter, IEEELOMConverter, DublinCoreConverter}
import scala.collection.JavaConversions._
import java.util.{Locale, Date}
import edu.colorado.phet.website.PhetWicketApplication
import edu.colorado.phet.common.phetcommon.util.LocaleUtils.stringToLocale
import edu.colorado.phet.website.util.PhetRequestCycle
import edu.colorado.phet.website.util.StringUtils.makeUrlAbsoluteProduction
import xml.{NodeSeq, Unparsed, Node}
import edu.colorado.phet.website.util.ScalaHibernateUtils._
import java.io.File
import edu.colorado.phet.website.content.simulations.SimulationPage
import edu.colorado.phet.common.phetcommon.util.FileUtils
import edu.colorado.phet.common.phetcommon.util.LocaleUtils.localeTo4646String
import java.text.SimpleDateFormat
import edu.colorado.phet.website.data.{Category, Keyword, Simulation, LocalizedSimulation}
import edu.colorado.phet.website.constants.WebsiteConstants
import java.util

/**
 * Utilities for metadata in general, and construction of the master format
 */
object MetadataUtils {

  val dublinCoreConverter = new DublinCoreConverter {}
  val ieeeLomConverter = new IEEELOMConverter {}
  val imsLodeIloxConverter = new IMSLODEILOXConverter {}
  val nsdlDcConverter = new NSDLDCConverter {}

  val MasterFormatName = "phet-simulation"

  def dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss")

  def iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

  def formatDateIso8601(date: util.Date) = iso8601DateFormat.format(date)

  /**
   * Turn a name into a VCard format entry
   */
  def vCardFromName(name: String): Node = Unparsed("<![CDATA[BEGIN:VCARD\nFN:" + name + "\nVERSION:3.0\nEND:VCARD]]>")

  /**
   * Turns a sequence of translated strings into an IEEE string (same meaning, but in different languages) that can be included in XML
   */
  def convertLangString(lang: Seq[LanguageString]): NodeSeq = lang.map(str => <string language={str.language}>{str.string}</string>)

  /**
   * Find and return the English translation from a list of LanguageStrings
   */
  def englishString(lang: Seq[LanguageString]): String = lang.find(_.language == "en").get.string

  // TODO: better documentation on why this is here! seems fishy!
  val commonTimestamp = 1340780168000L; // used so we can update all converters at once

  def convertNewlinesToPipes(str: String): String = if ( str == null ) "" else str.replace("<br/>", "|")

  def writeSimulations() {
    wrapTransaction(session => {
      val simulations = session.createQuery("select s from Simulation as s").list.map(_.asInstanceOf[Simulation])

      val metadataDir = PhetWicketApplication.get().getWebsiteProperties.getSimulationMetadataDir

      for ( sim <- simulations.filter(_.isVisible) ) {
        val metadataString: String = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + MetadataUtils.simulationToMasterFormat(sim)
        FileUtils.writeString(new File(metadataDir, sim.getName + ".xml"), metadataString)
      }
    })
  }

  /**
   * Returns an XML fragment that represents a simulation in our internal "master" metadata format.
   */
  def simulationToMasterFormat(sim: Simulation): String = {
    val project = sim.getProject

    val English = WebsiteConstants.ENGLISH // TODO: put this renaming somewhere else, since it is globally useful

    def localeSort(a: Locale, b: Locale): Boolean = {
      (a, b) match {
        case (English, _) => true
        case (_, English) => false
        case _ => a.toString.compareToIgnoreCase(b.toString) < 0
      }
    }

    // list of localized simulations TODO: add RichSimulation so we can get rid of casts like these
    val lsims: List[LocalizedSimulation] = sim.getLocalizedSimulations.map(_.asInstanceOf[LocalizedSimulation]).toList.sortWith((a, b) => localeSort(a.getLocale, b.getLocale))

    // list of all website translation locales (NOT sim translation locales)
    val webLocales = English :: PhetWicketApplication.get().getTranslationLocaleStrings.map(str => stringToLocale(str)).toList.sortWith(localeSort)

    // translate a string with a locale
    def translate(key: String, locale: Locale): String = PhetLocalizer.get().getBestString(PhetRequestCycle.get().getHibernateSession, key, locale)

    // turn a string key into a list of string elements (translated into each website translation, but only presented if is a unique translation)
    def translateToList(key: String): Seq[Node] = {
      val englishString = translate(key, English)
      for ( locale <- webLocales;
            translatedString = translate(key, locale)
            if ( locale == English || translatedString != englishString ) ) // TODO ignore duplicates of English
      yield { <string locale={localeTo4646String(locale)}>{translatedString}</string> }
    }

    def convertDate(date: Date): String = if ( date == null ) "" else dateFormat.format(date)

    val titles = lsims.map(lsim => <string locale={localeTo4646String(lsim.getLocale)}>{lsim.getTitle}</string>)
    val descriptions = translateToList(sim.getDescriptionKey)
    val learningGoals = translateToList(sim.getLearningGoalsKey)

    // TODO: include activities guide link if available?

    val xml =
    <simulation xmlns="http://phet.colorado.edu/xsd/phet-simulation/"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://phet.colorado.edu/xsd/phet-simulation/ http://phet.colorado.edu/files/schema/phet-simulation-1.0.xsd">
      <project id={project.getId.toString} name={project.getName}/>
      <simulation id={sim.getId.toString} name={sim.getName}/>
      <technology type={project.getType.toString}/>
      <version major={project.getVersionMajor.toString} minor={project.getVersionMinor.toString} revision={project.getVersionRevision.toString} timestamp={project.getVersionTimestamp.toString}/>
      <filesize kilobytes={sim.getKilobytes.toString}/>
      <credits designTeam={convertNewlinesToPipes(sim.getDesignTeam)} libraries={convertNewlinesToPipes(sim.getLibraries)} thanksTo={convertNewlinesToPipes(sim.getThanksTo)}/>
      <flags underConstruction={sim.isUnderConstruction.toString} guidanceRecommended={sim.isGuidanceRecommended.toString} classroomTested={sim.isClassroomTested.toString}/>
      <title>{titles}</title>
      <description>{descriptions}</description>
      <learningGoals>{learningGoals}</learningGoals>
      <thumbnail>{makeUrlAbsoluteProduction(sim.getThumbnailUrl)}</thumbnail>
      <screenshot>{makeUrlAbsoluteProduction(sim.getImageUrl)}</screenshot>
      <minGradeLevel>{sim.getMinGradeLevel}</minGradeLevel>
      <maxGradeLevel>{sim.getMaxGradeLevel}</maxGradeLevel>
      <createTime>{convertDate(sim.getCreateTime)}</createTime>
      <updateTime>{convertDate(sim.getUpdateTime)}</updateTime>
      <simPageLink>{makeUrlAbsoluteProduction(SimulationPage.getLinker(sim).getDefaultRawUrl)}</simPageLink>
      <languages>{lsims.map(lsim => <language>{localeTo4646String(lsim.getLocale)}</language>)}</languages>
      <keywords>{sim.getKeywords.map(keyword => <keyword>{translateToList(keyword.asInstanceOf[Keyword].getLocalizationKey)}</keyword>)}</keywords>
      <topics>{sim.getTopics.map(keyword => <keyword>{translateToList(keyword.asInstanceOf[Keyword].getLocalizationKey)}</keyword>)}</topics>
      <categories>{sim.getCategories.filter(category => category.asInstanceOf[Category].isContentCategory).map(category => <category>{translateToList(category.asInstanceOf[Category].getLocalizationKey)}</category>)}</categories>
      <licenses>{sim.getLicenseURLs.map(licenseURL => <license><url>{licenseURL}</url></license>)}</licenses>
      <scienceLiteracyMaps>{sim.getScienceLiteracyMapKeys.map(mapKey => <mapKey>{mapKey}</mapKey>)}</scienceLiteracyMaps>
      <classification>
        <lre0001>{sim.getLRETerms.map(term => <term id={term.id} english={term.englishCaption}/>)}</lre0001>
      </classification>
      <rights>{if ( sim.isHasCreativeCommonsAttributionLicense ) translateToList("metadata.rights") else translateToList("metadata.rightsGplOnly")}</rights>
    </simulation>

    xml.toString()
  }
}