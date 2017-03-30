package io.skysail.restlet

import io.skysail.server.restlet.response.ListResponseWrapper
import org.restlet.Response
import io.skysail.server.restlet.response.ListResponseWrapper

abstract class ScalaAbstractListResourceFilter {
  def handleList(resource: ScalaSkysailServerResource, response: Response): ScalaListResponseWrapper = {
    val responseWrapper = new ScalaListResponseWrapper(response);
   // handleMe(resource, responseWrapper);
    return responseWrapper;
  }
}