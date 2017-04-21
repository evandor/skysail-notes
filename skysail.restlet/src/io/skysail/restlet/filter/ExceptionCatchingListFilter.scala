package io.skysail.restlet.filter

import io.skysail.restlet.ScalaAbstractListResourceFilter
import io.skysail.restlet.SkysailServerResource
import io.skysail.restlet.Wrapper3
import org.slf4j.LoggerFactory
import org.restlet.resource.ResourceException
import io.skysail.restlet.filter.helper.ExceptionCatchingFilterHelper
import io.skysail.restlet.ScalaListResponseWrapper

class ScalaExceptionCatchingListFilter[T:Manifest] extends ScalaAbstractListResourceFilter[T] {

  override val log = LoggerFactory.getLogger(classOf[ScalaExceptionCatchingListFilter[T]])

  override def doHandle(resource: SkysailServerResource, responseWrapper: Wrapper3): FilterResult = {
    log.debug("entering {}#doHandle", this.getClass().getSimpleName());
    try {
      super.doHandle(resource, responseWrapper)
    } catch {
      case r: ResourceException => throw r
      case e: Exception => ExceptionCatchingFilterHelper.handleError(e, resource.getSkysailApplication(), responseWrapper, resource.getClass());
    }
    FilterResult.CONTINUE;
  }

  override def afterHandle(resource: SkysailServerResource, responseWrapper: Wrapper3): Unit = {
    resource.getServerInfo().setAgent("Skysail-Server/0.0.1 " + resource.getServerInfo().getAgent());
  }
}