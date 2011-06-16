package edu.colorado.phet.website.oai

import java.text.SimpleDateFormat

object OaiUtils {
  val MasterFormatName = "phet-simulation"

  def dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss") // NOTE: also used in MetadataUtils

//  val commonTimestamp = 1308202238000L; // used so we can update all converters at once
  val commonTimestamp = System.currentTimeMillis(); // TODO: remove this, since it always regenerates
}