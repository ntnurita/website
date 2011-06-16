package edu.colorado.phet.website.oai

import java.text.SimpleDateFormat

object OaiUtils {
  val MasterFormatName = "phet-simulation"

  def dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss") // NOTE: also used in MetadataUtils

  val commonTimestamp = 1308200938000L; // used so we can update all converters at once
}