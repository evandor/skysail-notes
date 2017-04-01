package io.skysail.restlet

import org.restlet.Response
import io.skysail.restlet.responses.ListResponse

class ScalaListResponseWrapper[T](response: Response) {
  def getEntity() = {
    response
    List()
  }
}