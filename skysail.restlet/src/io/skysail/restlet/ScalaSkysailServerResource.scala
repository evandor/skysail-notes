package io.skysail.restlet

import org.restlet.resource.ServerResource
import io.skysail.core.app.SkysailApplication
import io.skysail.core.utils.ReflectionUtils
import java.util.function.Consumer
import io.skysail.api.links.Link
import io.skysail.core.utils.LinkUtils
import io.skysail.server.ResourceContextId

abstract class ScalaSkysailServerResource extends ServerResource {

  var entity: AnyRef = null
  def setEntity(e: AnyRef) = entity = e

  val stringContextMap = new java.util.HashMap[ResourceContextId, String]()

  def getSkysailApplication() = getApplication().asInstanceOf[SkysailApplication]
  def getMetricsCollector() = getSkysailApplication().getMetricsCollector()
  def getParameterizedType() = ReflectionUtils.getParameterizedType(getClass());

  /*def getPathSubstitutions(): Consumer[Link] = {
    return l -> {
      val uri = l.getUri();
      l.setUri(LinkUtils.replaceValues(uri, getRequestAttributes()));
    }
  }*/

  def getFromContext(id: ResourceContextId) = stringContextMap.get(id)

  def addToContext(id: ResourceContextId, value: String): Unit = stringContextMap.put(id, value)

}