package edu.colorado.phet.website.util

import hibernate.{HibernateUtils, VoidTask}
import org.hibernate.Session

object ScalaHibernateUtils {
  def wrapTransaction(task: Session => Unit): Boolean = {
    return HibernateUtils.wrapTransaction(PhetRequestCycle.get().getHibernateSession, new VoidTask {
      def run(session: Session) = {
        task(session)
        null
      }
    })
  }
}