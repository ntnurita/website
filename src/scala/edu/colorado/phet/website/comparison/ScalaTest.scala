package edu.colorado.phet.website.comparison

import edu.colorado.phet.website.data.Project
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.PageParameters
import org.apache.wicket.markup.html.basic.Label
import edu.colorado.phet.website.util.ScalaHibernateUtils.wrapTransaction
import scala.collection.JavaConversions._

/**
 * This is our test Scala page. It will show some text, probably from the database
 */
class ScalaTest(parameters: PageParameters) extends WebPage(parameters) {

  // enter into a database transaction
  wrapTransaction(session => {

    // get a list of projects from the database
    val projects = session.createQuery("select p from Project as p").list

    // add a sorted list of project names separated by newlines to the output
    add(new Label("text", projects.map(_.asInstanceOf[Project].getName).sorted.mkString("\n")))
  })
}