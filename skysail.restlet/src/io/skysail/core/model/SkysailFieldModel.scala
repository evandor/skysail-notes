package io.skysail.core.model

import io.skysail.domain.core.FieldModel
import java.lang.reflect.Field
import io.skysail.domain.html.InputType
import java.lang.reflect.Type
import java.util.Collection
import io.skysail.restlet.utils.ScalaReflectionUtils

class ScalaSkysailFieldModel(
  entityModel: SkysailEntityModel,
  val f: java.lang.reflect.Field)
    extends FieldModel(
      entityModel,
      f.getName(),
      f.getType()) {

  //        setReadonly(false);
  //        setTruncateTo(determineTruncation(f));
  //
  //        listViewLink = determineListViewLink(f);
  //        format = determineFormat(f);
  //        facet = determineFacet(facetsProvider,f);

  override def getInputType(): String = f.getAnnotation(classOf[io.skysail.domain.html.Field]).inputType().name();

  override def isMandatory(): Boolean = {
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

  def getEntityType() = {
    if (classOf[Collection[_]].isAssignableFrom(f.getType())) 
      ScalaReflectionUtils.getParameterizedType(f);
    else
      null
  }
}