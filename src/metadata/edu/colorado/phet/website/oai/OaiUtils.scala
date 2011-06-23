package edu.colorado.phet.website.oai

import java.text.SimpleDateFormat
import java.util.Date

object OaiUtils {
  val MasterFormatName = "phet-simulation"

  // TODO: date cleanup
  def dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss") // NOTE: also used in MetadataUtils

  def iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

  def formatDateIso8601(date: Date) = iso8601DateFormat.format(date)

    val commonTimestamp = 1308678761000L; // used so we can update all converters at once
//  val commonTimestamp = System.currentTimeMillis(); // TODO: remove this, since it always regenerates
}