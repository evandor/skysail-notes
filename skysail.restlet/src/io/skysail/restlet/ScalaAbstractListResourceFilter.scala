package io.skysail.restlet

import io.skysail.server.restlet.response.ListResponseWrapper
import org.restlet.Response
import io.skysail.server.restlet.response.ListResponseWrapper
import io.skysail.restlet.responses.ListResponse

abstract class ScalaAbstractListResourceFilter[T] {
  def handleList(resource: ScalaSkysailServerResource, response: Response): ScalaListResponseWrapper[T] = {
    val responseWrapper = new ScalaListResponseWrapper[T](response);
   // handleMe(resource, responseWrapper);
    return responseWrapper;
  }
}