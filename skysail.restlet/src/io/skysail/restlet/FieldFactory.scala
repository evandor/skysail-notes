package io.skysail.restlet

import java.util.Collections
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._
import io.skysail.restlet.app.SkysailApplicationService
import io.skysail.core.model.ScalaSkysailFieldModel
import io.skysail.restlet.forms.ScalaFormField

trait FieldFactory {

  val log = LoggerFactory.getLogger(classOf[FieldFactory])

  def determineFrom(r: SkysailServerResource[_], s: SkysailApplicationService): Map[String, ScalaFormField]

  def determine(
      resource: SkysailServerResource[_], cls: Class[_], 
      service: SkysailApplicationService): Map[String, ScalaFormField] = {
    
    require(service != null, "service must not be null")
    val entityModel = service.getEntityModel(cls.getName());

    if (entityModel == null) {
      log.warn("entity Model for '{}' was null.", cls.getName());
      log.warn("existing models are:");
      service.getEntityModels().foreach { model => log.info("{}", model.getName()) }
      return Map()
    }

    entityModel.getScalaFields().values
      .map { field => new ScalaFormField(field, resource.entity, service) }
      .map { sff => sff.getId() -> sff }
      .toMap
    
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
  override def determineFrom(r: SkysailServerResource[_], s: SkysailApplicationService) = Map()
}

class FormResponseEntityFieldFactory(t: Class[_]) extends FieldFactory {
  override def determineFrom(r: SkysailServerResource[_], s: SkysailApplicationService) = determine(r, t, s)
}

class DefaultEntityFieldFactory(t: Class[_]) extends FieldFactory {
  override def determineFrom(r: SkysailServerResource[_], s: SkysailApplicationService) =  determine(r, t, s)
}

class DefaultListFieldFactory() extends FieldFactory {
  override def determineFrom(r: SkysailServerResource[_], s: SkysailApplicationService) = determine(r, r.getParameterizedType(), s)
}
