package io.skysail.restlet.model

import io.skysail.domain.core.FieldModel
import java.lang.reflect.Field
import io.skysail.domain.html.InputType

class ScalaSkysailFieldModel(
    entityModel: ScalaSkysailEntityModel,
    val f: java.lang.reflect.Field)
  extends FieldModel(
    entityModel, 
    f.getName(), 
    f.getType()) {

  //        setReadonly(false);
  //        setTruncateTo(determineTruncation(f));
  //        setEntityType(f);
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
  
}