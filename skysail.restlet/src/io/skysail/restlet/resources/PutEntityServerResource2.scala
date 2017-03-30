package io.skysail.restlet.resources

import io.skysail.restlet.utils.ResourceUtils
import io.skysail.restlet.ScalaSkysailBeanUtils
import io.skysail.restlet.ScalaSkysailServerResource

class PutEntityServerResource2[T] extends ScalaSkysailServerResource {

  def copyProperties(dest: T, orig: T): Unit = {
    try {
      val beanUtilsBean = new ScalaSkysailBeanUtils[T](orig, ResourceUtils.determineLocale(this),
        getSkysailApplication().getSkysailApplicationService());
      beanUtilsBean.copyProperties(dest, orig, this);
    } catch {
      case e: Throwable => throw new RuntimeException("Error copying beans", e);
    }
  }

}