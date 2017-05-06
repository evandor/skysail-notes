package io.skysail.restlet

import java.util.Collections
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._
import io.skysail.restlet.app.SkysailApplicationService
import io.skysail.restlet.forms.ScalaFormField

trait FieldFactory {

  val log = LoggerFactory.getLogger(classOf[FieldFactory])

  def determineFrom(r: SkysailServerResource[_]): Map[String, ScalaFormField]

  def determine(
      resource: SkysailServerResource[_], cls: Class[_]): Map[String, ScalaFormField] = {
    
    //require(appService != null, "service must not be null")
    val appModel = resource.getSkysailApplication().applicationModel2
    
    val entityModel = appModel.entityModelFor(cls.getName)
    
    //val entityModel = appService.getEntityModel(cls.getName());

    if (entityModel.isEmpty) {
      log.warn("entity Model for '{}' was null.", cls.getName());
      log.warn("existing models are:");
     // appService.getEntityModels().foreach { model => log.info("{}", model.getName()) }
      return Map()
    }
    
    val fields = entityModel.get.fields
   // println(fields)

    fields.values
      .map { field => new ScalaFormField(field, resource.entity) }
      .map { sff => sff.getId() -> sff}
      .toMap
      
//    entityModel.getScalaFields().values
//      .map { field => new ScalaFormField(field, resource.entity, service) }
//      .map { sff => sff.getId() -> sff }
//      .toMap
//    if (entityModel.getFieldValues() == null) {
//      return Map()
//    }
//    entityModel.getFieldValues().asScala
//      .map { fv => fv.asInstanceOf[ScalaSkysailFieldModel] }
//      .map { field => new ScalaFormField(field, resource.entity, service) }
//      .map { sff => sff.getId() -> sff }
//      .toMap
  }

}

class NoFieldFactory extends FieldFactory {
  override def determineFrom(r: SkysailServerResource[_]) = Map()
}

class FormResponseEntityFieldFactory(t: Class[_]) extends FieldFactory {
  override def determineFrom(r: SkysailServerResource[_]) = determine(r, t)
}

class DefaultEntityFieldFactory(t: Class[_]) extends FieldFactory {
  override def determineFrom(r: SkysailServerResource[_]) =  determine(r, r.getParameterizedType())
}

class DefaultListFieldFactory() extends FieldFactory {
  override def determineFrom(r: SkysailServerResource[_]) = determine(r, r.getParameterizedType())
}
