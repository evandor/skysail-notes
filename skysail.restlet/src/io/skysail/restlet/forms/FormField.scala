package io.skysail.restlet.forms

import java.lang.reflect.Field
import io.skysail.restlet.app.ScalaSkysailApplicationService
import io.skysail.restlet.model.ScalaSkysailFieldModel
import io.skysail.domain.html.InputType
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

case class ScalaFormField(ssfm: ScalaSkysailFieldModel, currentEntity: Any, appService: ScalaSkysailApplicationService) {

  //        this.type = sfm.getF().getType();
  //        this.sfm = sfm;
  //
  //        this.appService = appService;
  //        setEntityClass(sfm.getF());
  //        setInputType(getFromFieldAnnotation(sfm.getF()));
  //        this.label = sfm.getF().getName();
  //        this.currentEntity = currentEntity;
  //        tab = postViewAnnotation != null ? postViewAnnotation.tab() : null;
  //        this.htmlId = sfm.getF().getDeclaringClass().getName().replace(".","_") + "_" + sfm.getF().getName();
  //        //this.htmlName = sfm.getF().getDeclaringClass().getName() + "|" + sfm.getF().getName();
  //        this.htmlName = determineHtmlName(sfm.getF());

  def getId() = ssfm.getId
  def getLabel() = ssfm.f.getName()

  val inputType: InputType = getFromFieldAnnotation(ssfm.f)

  def isReadonlyInputType(): Boolean = isOfInputType(InputType.READONLY)

  def notNullAnnotation() = ssfm.f.getAnnotation(classOf[NotNull])
  def sizeAnnotation() = ssfm.f.getAnnotation(classOf[Size])

  def isMandatory() = ssfm.isMandatory()
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