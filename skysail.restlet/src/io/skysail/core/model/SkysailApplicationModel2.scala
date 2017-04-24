package io.skysail.core.model

import scala.collection.JavaConverters._
import io.skysail.domain.model.ResourceModel
import io.skysail.domain.model.ApplicationModel

class SkysailApplicationModel2(name: String) extends io.skysail.domain.model.ApplicationModel(name) {

  override def addOnce(resourceModel: ResourceModel): ApplicationModel = {
    val appModel = super.addOnce(resourceModel);
    //checkEntitySupertypeRelations();
    appModel;
  }

  /*def checkEntitySupertypeRelations() = {
    getEntityIds().asScala.foreach { entityKey =>
      for (otherEntityKey <- getEntityIds().asScala) {
        if (!entityKey.equals(otherEntityKey)) {
          val entityModel = getEntity(entityKey).asInstanceOf[ScalaSkysailEntityModel]
          val otherEntityModel = getEntity(otherEntityKey).asInstanceOf[ScalaSkysailEntityModel]
          if (entityModel.identifiableClass.isAssignableFrom(otherEntityModel.identifiableClass)) {
            //supertypes.put(otherEntityModel, entityModel);
            //subtypes.put(entityModel, otherEntityModel);
          }
        }
      }
    }

  }*/
}