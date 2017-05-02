package io.skysail.restlet.resources

import io.skysail.restlet._
import io.skysail.restlet.utils.ScalaResourceUtils
import org.restlet.data.Method

class PutEntityServerResource[T] extends SkysailServerResource {

  override def getVerbs(): Set[Method] = Set(Method.GET, Method.PUT)

  def copyProperties(dest: T, orig: T): Unit = {
    try {
      val beanUtilsBean = new ScalaSkysailBeanUtils[T](orig, ScalaResourceUtils.determineLocale(this),
        getSkysailApplication().getSkysailApplicationService());
      beanUtilsBean.copyProperties(dest, orig, this);
    } catch {
      case e: Throwable => throw new RuntimeException("Error copying beans", e);
    }
  }

  def getEntity(): Any = {
    ???
  }

}