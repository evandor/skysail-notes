package io.skysail.restlet.responses

import org.restlet.Response

sealed trait ScalaSkysailResponse[T] {
  def entity():T 
}

final case class FormResponse[T](response: Response, entity: T, target: String) extends ScalaSkysailResponse[T]
final case class ListResponse[T](response: Response, entity:List[T]) extends ScalaSkysailResponse[List[T]]
