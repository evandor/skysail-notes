package io.skysail.restlet

import io.skysail.restlet.app.SkysailApplication
import io.skysail.core.model.ScalaSkysailEntityModel

object ScalaEntityFactory {
  def createFrom( 
		skysailApplication: SkysailApplication, 
	  identifiable: Class[_],
    resourceInstance: SkysailServerResource[_]):ScalaSkysailEntityModel = { // <T extends Entity> SkysailEntityModel<T>
         new ScalaSkysailEntityModel(identifiable, resourceInstance);
    }
}