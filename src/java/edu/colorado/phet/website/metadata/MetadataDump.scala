package edu.colorado.phet.website.metadata

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.PageParameters
import edu.colorado.phet.website.util.ScalaHibernateUtils.wrapTransaction
import scala.collection.JavaConversions._
import edu.colorado.phet.website.data.Simulation
import edu.colorado.phet.website.components.RawLabel

/**
 * This dumps metadata for all visible simulations
 */
class MetadataDump(parameters: PageParameters) extends WebPage(parameters) {

  getResponse.setContentType("text/xml")

  // enter into a database transaction
  wrapTransaction(session => {
    val simulations = session.createQuery("select s from Simulation as s").list.map(_.asInstanceOf[Simulation])

    add(new RawLabel("simulations", simulations.filter(_.isVisible).map(sim => MetadataUtils.simulationToMasterFormat(sim)).mkString("\n")))
  })
}