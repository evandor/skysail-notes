package io.skysail.core.model

import io.skysail.restlet.SkysailServerResource

class TestResource extends SkysailServerResource[TestEntity] { 
  
  def getEntity(): Any = {
    "hi"
  }
}