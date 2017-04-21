package io.skysail.restlet

import org.restlet.Response
import io.skysail.restlet.responses.ListResponse
import org.slf4j.LoggerFactory

abstract class ScalaAbstractListResourceFilter[T:Manifest] extends ScalaResourceFilter[T] {

  override val log = LoggerFactory.getLogger(this.getClass())

  final def handle(resource: SkysailServerResource, response: Response): ScalaListResponseWrapper[T] = {
    val responseWrapper = new ScalaListResponseWrapper[T](response)
    handleMe(resource, responseWrapper)
    responseWrapper
  }
 
}