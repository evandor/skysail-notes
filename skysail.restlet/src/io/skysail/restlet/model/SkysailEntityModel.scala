package io.skysail.restlet.model

import io.skysail.domain.core.EntityModel
import io.skysail.restlet.SkysailServerResource
import scala.collection.JavaConverters._
import java.lang.reflect.Field
import io.skysail.restlet.utils.ScalaReflectionUtils

class ScalaSkysailEntityModel(
  identifiableClass: Class[_],
  resourceInstance: SkysailServerResource)
    extends EntityModel(identifiableClass.getName()) {

  val fields = deriveFields()
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

  }
}