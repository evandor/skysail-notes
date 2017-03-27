package io.skysail.app.notes.resources

import org.restlet.resource.ServerResource
import io.skysail.core.app.SkysailApplication
import io.skysail.core.utils.ReflectionUtils
import java.util.function.Consumer
import io.skysail.api.links.Link
import io.skysail.core.utils.LinkUtils

abstract class ScalaSkysailServerResource[T] extends io.skysail.core.resources.SkysailServerResource[T] {
 
  var entity: T 
  def setEntity(e: T) = entity = e
  
  def getSkysailApplication() = getApplication().asInstanceOf[SkysailApplication]
  //def getMetricsCollector() = getSkysailApplication().getMetricsCollector()
  //def getParameterizedType() = ReflectionUtils.getParameterizedType(getClass());
 
  /*def getPathSubstitutions(): Consumer[Link] = {
    return l -> {
      val uri = l.getUri();
      l.setUri(LinkUtils.replaceValues(uri, getRequestAttributes()));
    }
  }*/

}