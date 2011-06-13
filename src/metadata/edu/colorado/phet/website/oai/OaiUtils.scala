package edu.colorado.phet.website.oai

import java.text.SimpleDateFormat

object OaiUtils {
  val MasterFormatName = "phet-simulation"

  def dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss") // NOTE: also used in MetadataUtils
}