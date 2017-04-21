package io.skysail.restlet

import io.skysail.restlet.app.SkysailApplication
import io.skysail.restlet.model.ScalaSkysailEntityModel

object ScalaEntityFactory {
  def createFrom( 
		skysailApplication: SkysailApplication, 
	  identifiable: Class[_],
    resourceInstance: SkysailServerResource):ScalaSkysailEntityModel = { // <T extends Entity> SkysailEntityModel<T>
         new ScalaSkysailEntityModel(identifiable, resourceInstance);
    }
}