package io.skysail.restlet.resources

import io.skysail.api.responses.SkysailResponse
import io.skysail.restlet.SkysailServerResource
import org.restlet.data.Method
import io.skysail.core.model.LinkRelation

class EntityServerResource[T] extends SkysailServerResource {
   def eraseEntity() = new SkysailResponse[T]()

  override def getVerbs(): Set[Method] = Set(Method.GET)
  
  override def getLinkRelation() = LinkRelation.ITEM

  def getEntity(): Any = {
     ???
   }
}