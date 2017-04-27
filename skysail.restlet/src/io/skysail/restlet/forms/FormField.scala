package io.skysail.restlet.forms

import java.lang.reflect.Field
import io.skysail.restlet.app.SkysailApplicationService
import io.skysail.domain.html.InputType
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import scala.beans.BeanProperty
import java.util.Collection
import io.skysail.core.model.SkysailFieldModel2

case class ScalaFormField(ssfm: SkysailFieldModel2, currentEntity: Any) {

  //        this.type = sfm.getF().getType();
  //        this.sfm = sfm;
  //
  //        this.appService = appService;
  //        setEntityClass(sfm.getF());
  //        setInputType(getFromFieldAnnotation(sfm.getF()));
  //        this.label = sfm.getF().getName();
  //        this.currentEntity = currentEntity;
  //        tab = postViewAnnotation != null ? postViewAnnotation.tab() : null;

  def getId() = ssfm.f.getDeclaringClass().getName() + "|" + ssfm.f.getName()
  def getLabel() = ssfm.f.getName()

  @BeanProperty val inputType: InputType = getFromFieldAnnotation(ssfm.f)

  def isReadonlyInputType(): Boolean = isOfInputType(InputType.READONLY)

  def notNullAnnotation() = ssfm.f.getAnnotation(classOf[NotNull])
  def sizeAnnotation() = ssfm.f.getAnnotation(classOf[Size])

  def isMandatory() = ssfm.isMandatory()
  
  def getHtmlId() = ssfm.f.getDeclaringClass().getName().replace(".", "_") + "_" + ssfm.f.getName();
  
  def getHtmlName(): String = {
//    if (currentEntity == null) {
//      return ssfm.f.getName();
//    }
    if (currentEntity.isInstanceOf[Collection[_]]) {
      return ssfm.f.getDeclaringClass().getName() + "|" + ssfm.f.getName();
    }
    //return currentEntity.getClass().getName() + "|" + ssfm.f.getName();
    ssfm.f.getDeclaringClass().getName() + "|" + ssfm.f.getName()
  }

  //    if (notNullAnnotation() != null) {
  //      true;
  //    } else if (sizeAnnotation() != null) {
  //      if (sizeAnnotation.min() > 0) true else false
  //    } else {
  //      false      
  //    }
  //  }

  private def isOfInputType(inputType: InputType) = this.inputType.equals(inputType)

  private def getFromFieldAnnotation(fieldAnnotation: Field) = {
    val annotation = fieldAnnotation.getAnnotation(classOf[io.skysail.domain.html.Field]);
    if (annotation != null) annotation.inputType() else null;
  }

  //   private def setAnnotations(field: Field):Unit = {
  //        referenceAnnotation = field.getAnnotation(classOf[Reference]);
  ////        fieldRelation = analyseFieldRelation();
  //        formFieldAnnotation = field.getAnnotation(classOf[io.skysail.domain.html.Field]);
  //        listViewAnnotation = field.getAnnotation(classOf[ListView]);
  //        postViewAnnotation = field.getAnnotation(classOf[PostView]);
  //        submitAnnotation = field.getAnnotation(classOf[Submit]);
  //        notNullAnnotation = field.getAnnotation(classOf[NotNull]);
  //        sizeAnnotation = field.getAnnotation(classOf[Size]);
  //    }

}