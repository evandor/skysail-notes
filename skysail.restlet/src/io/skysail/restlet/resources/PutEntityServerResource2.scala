package io.skysail.restlet.resources

import io.skysail.restlet._
import io.skysail.restlet.utils.ScalaResourceUtils

class PutEntityServerResource2[T] extends ScalaSkysailServerResource {

  def copyProperties(dest: T, orig: T): Unit = {
    try {
      val beanUtilsBean = new ScalaSkysailBeanUtils[T](orig, ScalaResourceUtils.determineLocale(this),
        getSkysailApplication().getSkysailApplicationService());
      beanUtilsBean.copyProperties(dest, orig, this);
    } catch {
      case e: Throwable => throw new RuntimeException("Error copying beans", e);
    }
  }

}