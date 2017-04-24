package io.skysail.core.model

import io.skysail.domain.core.EntityModel
import io.skysail.restlet.SkysailServerResource
import scala.collection.JavaConverters._
import java.lang.reflect.Field
import io.skysail.restlet.utils.ScalaReflectionUtils
import io.skysail.restlet.router.ScalaSkysailRouter

//case class ResourceModel(
//    val path: String,
//    val targetClass: Class[_]
//  ) {
//  
//  
//}

class SkysailResourceModel(
    val path: String,
    val targetClass: Class[_]) {

  require(path != null, "A ResourceModel's path must not be null")
  require(path.trim().length() > 0, "A ResourceModel's path must not be empty")

  require(targetClass != null, "A ResourceModel's target class must not be null")

  val targetResource: Option[SkysailServerResource[_]] = determineTargetResource()

  val targetEntity: Option[Class[_]] = determineTargetEntity()

  private def determineTargetResource() = {
    if (targetClass != null && classOf[SkysailServerResource[_]].isAssignableFrom(targetClass)) {
      val resourceInstance = targetClass.newInstance().asInstanceOf[SkysailServerResource[_]];
      Some(resourceInstance)
    } else {
      null
    }
  }

  private def determineTargetEntity() = {
    if (targetResource.isDefined) {
      Some(ScalaSkysailRouter.getResourcesGenericType(targetResource.get))
    } else {
      None
    }
  }

  /* val fields = deriveFields()
  def getScalaFields() = fields

  val relations = deriveRelations()

  //  setAssociatedResourceClass(resourceInstance);

  def identifiableClass(): Class[_] = identifiableClass

  private def deriveFields() = {
    ScalaReflectionUtils.getInheritedFields(identifiableClass)
  
    .filter { filterFormFields(_) }
      .map { f => new ScalaSkysailFieldModel(this, f) }
      .map(m => m.getId -> m)
      .toMap
  }

  private def filterFormFields(f: Field): Boolean = f.getAnnotation(classOf[io.skysail.domain.html.Field]) != null

  def deriveRelations() = {
    val fields = ScalaReflectionUtils.getInheritedFields(identifiableClass)
    fields
      .filter { filterFormFields(_) }
      .map { _.getName }
      //.map { r => new ScalaEntityRelation(r,null, EntityRelationType.ONE_TO_MANY))}
      .toList

  }*/
}