package io.skysail.core.model

import io.skysail.domain.core.EntityModel
import io.skysail.restlet.SkysailServerResource
import scala.collection.JavaConverters._
import java.lang.reflect.Field
import io.skysail.restlet.utils.ScalaReflectionUtils
import io.skysail.restlet.router.ScalaSkysailRouter

/**
 * A ResourceModel captures the link between a path and a SkysailServerResource, defining
 * which path should be handled by which resource.
 *
 *  @constructor create a new resource model, given a path and a SkysailServerResource.
 *
 *  @param path the uri path relative to the application
 *  @param targetClass a SkysailServerResource class to handle requests to the given path
 */
case class SkysailResourceModel2(val path: String, val targetClass: Class[_ <: SkysailServerResource[_]]) {

  require(path != null, "A ResourceModel's path must not be null")
  require(path.trim().length() > 0, "A ResourceModel's path must not be empty")
  require(targetClass != null, "A ResourceModel's target class must not be null")

  val targetResource: SkysailServerResource[_] = determineTargetResource()
  val targetEntity: Class[_] = determineTargetEntity()

  private def determineTargetResource() = targetClass.newInstance().asInstanceOf[SkysailServerResource[_]]
  private def determineTargetEntity() = ScalaSkysailRouter.getResourcesGenericType(targetResource)

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