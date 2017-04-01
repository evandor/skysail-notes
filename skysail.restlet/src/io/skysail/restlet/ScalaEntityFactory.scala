package io.skysail.restlet

import io.skysail.restlet.app.ScalaSkysailApplication
import io.skysail.restlet.model.ScalaSkysailEntityModel

object ScalaEntityFactory {
  def createFrom( 
		skysailApplication: ScalaSkysailApplication, 
	  identifiable: Class[_],
    resourceInstance: ScalaSkysailServerResource):ScalaSkysailEntityModel = { // <T extends Entity> SkysailEntityModel<T>
         new ScalaSkysailEntityModel(identifiable);
    }
}