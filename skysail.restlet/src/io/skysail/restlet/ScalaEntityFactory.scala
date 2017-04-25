package io.skysail.restlet

import io.skysail.restlet.app.SkysailApplication
import io.skysail.core.model.SkysailEntityModel

object ScalaEntityFactory {
  def createFrom( 
		skysailApplication: SkysailApplication, 
	  identifiable: Class[_],
    resourceInstance: SkysailServerResource[_]):SkysailEntityModel = { // <T extends Entity> SkysailEntityModel<T>
         new SkysailEntityModel(identifiable, resourceInstance);
    }
}