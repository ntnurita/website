package edu.colorado.phet.website.util

import hibernate.{Task, HibernateUtils, VoidTask}
import org.hibernate.Session

object ScalaHibernateUtils {
  def wrapTransaction(task: Session => Unit): Boolean = {
    HibernateUtils.wrapTransaction(PhetRequestCycle.get().getHibernateSession, new VoidTask {
      def run(session: Session) {
        task(session)
      }
    })
  }

  def wrapSession(task: Session => Unit): Boolean = {
    HibernateUtils.wrapSession(new VoidTask {
      def run(session: Session) {
        task(session)
      }
    })
  }
}