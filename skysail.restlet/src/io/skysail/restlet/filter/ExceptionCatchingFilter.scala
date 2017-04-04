package io.skysail.restlet.filter

import io.skysail.restlet.ScalaAbstractResourceFilter
import io.skysail.restlet.ScalaSkysailServerResource
import org.slf4j.LoggerFactory
import io.skysail.server.restlet.filter.FilterResult

class ExceptionCatchingFilter[T] extends ScalaAbstractResourceFilter[T] {

  val log = LoggerFactory.getLogger(classOf[ExceptionCatchingFilter[_]])

  override def doHandle(resource: ScalaSkysailServerResource /*,  responseWrapper: Wrapper2*/ ): FilterResult = {
    log.debug("entering {}#doHandle", this.getClass().getSimpleName());
    
    try {
      super.doHandle(resource)
    } catch {
      case _: Throwable =>
    }
    
    //        try {
    //            super.doHandle(resource, responseWrapper);
    //        } catch (ResourceException re) {
    //            throw re;
    //        } catch (Exception e) {
    //            ExceptionCatchingFilterHelper.handleError(e, resource.getApplication(), responseWrapper, resource.getClass());
    //        }
    FilterResult.CONTINUE;
  }

  override def afterHandle(resource: ScalaSkysailServerResource):Unit = { //, Wrapper2 responseWrapper) {
    resource.getServerInfo().setAgent("Skysail-Server/0.0.1 " + resource.getServerInfo().getAgent());
  }
}