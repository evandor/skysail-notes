package io.skysail.restlet

import io.skysail.server.restlet.response.ListResponseWrapper
import org.restlet.Response
import io.skysail.server.restlet.response.ListResponseWrapper
import io.skysail.restlet.responses.ListResponse
import io.skysail.server.restlet.filter.FilterResult

abstract class ScalaAbstractResourceFilter[T] {
  
      var next: ScalaAbstractResourceFilter[T] = null

  def handleList(resource: ScalaSkysailServerResource, response: Response): ScalaResponseWrapper[T] = {
    val responseWrapper = new ScalaResponseWrapper[T](response);
   // handleMe(resource, responseWrapper);
    return responseWrapper;
  }
  
      private def getNext():ScalaAbstractResourceFilter[T] = next

  
      protected def doHandle(resource: ScalaSkysailServerResource,  responseWrapper: Wrapper3): FilterResult = {
//        val next = getNext();
//        if (next != null) {
//            logger.debug("next filter in chain: {}", next.getClass().getSimpleName());
//            next.handleMe(resource, responseWrapper);
//        }
         FilterResult.CONTINUE;
    }

}