package io.skysail.restlet.resources

import io.skysail.api.responses.SkysailResponse
import io.skysail.restlet.ScalaSkysailServerResource

class EntityServerResource2[T] extends ScalaSkysailServerResource {
   def eraseEntity() = new SkysailResponse[T]()

  def getEntity(): Any = {
     ???
   }
}