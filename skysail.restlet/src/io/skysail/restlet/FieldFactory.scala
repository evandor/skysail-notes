package io.skysail.restlet

import io.skysail.core.app.SkysailApplicationService
import io.skysail.server.forms.FormField
import java.util.Collections

trait FieldFactory {
  def determineFrom(resource: ScalaSkysailServerResource, appService: SkysailApplicationService): java.util.Map[String, FormField]

  def determine(resource: ScalaSkysailServerResource, cls: Class[_], service: SkysailApplicationService): java.util.Map[String, FormField] = {
            val entityModel = service.getEntityModel(cls.getName());
    
            if (entityModel == null) {
                //log.warn("entity Model for '{}' was null.", cls.getName());
                //log.warn("existing models are:");
                //service.getEntityModels().forEach(model -> log.info("{}", model.getName()));
            }
    
            if (entityModel.getFieldValues() == null) {
                return Collections.emptyMap();
            }
//            val collect = entityModel.getFieldValues().stream()
//            		.map(SkysailFieldModel.class::cast)
//                    .map(field -> new FormField(field, resource.getCurrentEntity(), service))
//                    .collect(MyCollectors.toLinkedMap(p -> p.getId(), p -> p));
            return null//collect;
  }

}

class NoFieldFactory extends FieldFactory {
  override def determineFrom(resource: ScalaSkysailServerResource, service: SkysailApplicationService): java.util.Map[String, FormField] = {
    Collections.emptyMap();
  }
}

class FormResponseEntityFieldFactory(t: Class[_]) extends FieldFactory {
  override def determineFrom(resource: ScalaSkysailServerResource, service: SkysailApplicationService): java.util.Map[String, FormField] = {
     determine(resource, t, service);
  }
}
