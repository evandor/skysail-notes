package io.skysail.core.model

import java.lang.reflect.Field
import java.lang.reflect.Type
import java.util.Collection
import io.skysail.restlet.utils.ScalaReflectionUtils

case class FieldModel(val f: java.lang.reflect.Field) {
  
  require(f != null, "you must provide a non-null field to construct a FieldModel")

  val name = f.getName
  
  def getInputType(): String = f.getAnnotation(classOf[io.skysail.core.html.Field]).inputType().name();

  def isMandatory(): Boolean = {
    val notNullAnnotation = f.getAnnotation(classOf[javax.validation.constraints.NotNull]);
    if (notNullAnnotation != null) {
      return true;
    }
    val sizeAnnotation = f.getAnnotation(classOf[javax.validation.constraints.Size]);
    if (sizeAnnotation != null) {
      if (sizeAnnotation.min() > 0) {
        return true;
      }
    }
    return false;
  }

  private def getEntityType() = {
    if (classOf[Collection[_]].isAssignableFrom(f.getType())) 
      ScalaReflectionUtils.getParameterizedType(f);
    else
      null
  }
}