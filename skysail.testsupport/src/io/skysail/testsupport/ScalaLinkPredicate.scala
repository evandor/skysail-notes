package io.skysail.testsupport

import org.restlet.util.Series
import org.restlet.data.Method
import org.restlet.data.Header
import com.google.common.base.Predicate
import io.skysail.core.model.LinkModel

abstract class ScalaLinkPredicate(series: Series[Header]) extends Predicate[LinkModel] {
  val link = series.getFirstValue("Link")
  override def toString() = "\n - " + link.replace(",", "\n - ")
}

class ScalaLinkTitlePredicate(title: String, series: Series[Header]) extends ScalaLinkPredicate(series) {
  def apply(l: LinkModel): Boolean = l.getTitle().equals(title)
}

object ScalaLinkMethodPredicate {
  
}

class ScalaLinkMethodPredicate(method: Method, series: Series[Header]) extends ScalaLinkPredicate(series) {
  def apply(l: LinkModel): Boolean = true//l.getVerbs().contains(method)
}
