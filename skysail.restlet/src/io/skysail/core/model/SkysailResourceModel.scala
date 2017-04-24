package io.skysail.core.model

import io.skysail.domain.core.EntityModel
import io.skysail.restlet.SkysailServerResource
import scala.collection.JavaConverters._
import java.lang.reflect.Field
import io.skysail.restlet.utils.ScalaReflectionUtils
import io.skysail.restlet.router.ScalaSkysailRouter

class SkysailResourceModel(
    override val path: String,
    override val targetClass: Class[_]
  )
  extends io.skysail.domain.model.ResourceModel(path, targetClass) {
  
  
  if (targetClass != null && classOf[SkysailServerResource[_]].isAssignableFrom(targetClass)) {
      try {
        val resourceInstance = targetClass.newInstance().asInstanceOf[SkysailServerResource[_]];
        println(s"Resource: ${resourceInstance.getClass.getName}")
        val parameterizedType = ScalaSkysailRouter.getResourcesGenericType(resourceInstance);
        println(parameterizedType)
      } catch {
        case e: Throwable =>
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