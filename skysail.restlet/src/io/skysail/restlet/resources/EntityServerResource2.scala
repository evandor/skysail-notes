package io.skysail.restlet.resources

import io.skysail.api.responses.SkysailResponse
import io.skysail.restlet.SkysailServerResource

class EntityServerResource2[T] extends SkysailServerResource {
   def eraseEntity() = new SkysailResponse[T]()

  def getEntity(): Any = {
     ???
   }
}