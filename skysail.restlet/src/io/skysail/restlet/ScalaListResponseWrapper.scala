package io.skysail.restlet

import org.restlet.Response
import io.skysail.restlet.responses.ListResponse

//class ScalaListResponseWrapper[T](response: Response) {
//  def getEntity() = {
//    response
//    List()
//  }
//}

class ScalaListResponseWrapper[T](response: Response) extends Wrapper3 {

  var entity: List[T] = null
  
  def getEntity() = entity

  def getResponse(): Response = response

  def setEntity(entity: List[T]): Unit = {
    this.entity = entity
  }
}