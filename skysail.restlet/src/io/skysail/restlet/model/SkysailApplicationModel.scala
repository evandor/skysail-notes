package io.skysail.restlet.model

import io.skysail.domain.core.ApplicationModel
import io.skysail.domain.core.EntityModel
import io.skysail.domain.Entity

import scala.collection.JavaConverters._

class SecurityConfigBuilderModel(name: String) extends ApplicationModel(name) {

  override def addOnce[T <: Entity](entityModel: EntityModel[T]): ApplicationModel = {
    val appModel = super.addOnce(entityModel);
    checkEntitySupertypeRelations();
    appModel;
  }

  def checkEntitySupertypeRelations() = {
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

  }
}