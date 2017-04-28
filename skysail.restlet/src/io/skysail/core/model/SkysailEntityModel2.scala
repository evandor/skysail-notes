package io.skysail.core.model

import io.skysail.domain.core.EntityModel
import io.skysail.restlet.SkysailServerResource
import scala.collection.JavaConverters._
import java.lang.reflect.Field
import io.skysail.restlet.utils.ScalaReflectionUtils

case class SkysailEntityModel2(entityClass: Class[_]) {
  
  require(entityClass != null, "The provided entity class must not be null")
  
  def name() = entityClass.getName

  val fields = deriveFields()

  private def deriveFields() = {
    ScalaReflectionUtils.getInheritedFields(entityClass)
      .filter { filterFormFields(_) }
      .map { f => new SkysailFieldModel2(f) }
      .map(m => m.name -> m)
      .toMap
  }

  private def filterFormFields(f: Field): Boolean = f.getAnnotation(classOf[io.skysail.domain.html.Field]) != null

}