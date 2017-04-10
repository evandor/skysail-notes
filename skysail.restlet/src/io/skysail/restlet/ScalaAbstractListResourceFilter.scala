package io.skysail.restlet

import io.skysail.server.restlet.response.ListResponseWrapper
import org.restlet.Response
import io.skysail.server.restlet.response.ListResponseWrapper
import io.skysail.restlet.responses.ListResponse
import io.skysail.server.restlet.filter._
import io.skysail.server.restlet.filter.FilterResult._
import org.slf4j.LoggerFactory

abstract class ScalaAbstractListResourceFilter[T] extends ScalaResourceFilter[T] {

  override val log = LoggerFactory.getLogger(this.getClass())

  final def handle(resource: ScalaSkysailServerResource, response: Response): ScalaListResponseWrapper[T] = {
    val responseWrapper = new ScalaListResponseWrapper[T](response)
    handleMe(resource, responseWrapper)
    responseWrapper
  }
 
}