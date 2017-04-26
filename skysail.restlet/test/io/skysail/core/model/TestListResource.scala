package io.skysail.core.model

import io.skysail.restlet.SkysailServerResource
import io.skysail.restlet.resources.ListServerResource2

class TestListResource extends ListServerResource2[TestEntity](classOf[TestEntityResource]) { 
  
  def getEntity(): Any = "hi"
  
  override def linkedResources() = List(classOf[TestEntityResource])
}