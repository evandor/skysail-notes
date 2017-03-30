package io.skysail.restlet.model

import io.skysail.domain.core.FieldModel

class ScalaSkysailFieldModel(
    entityModel: ScalaSkysailEntityModel,
    f: java.lang.reflect.Field) extends FieldModel(entityModel, f.getName(), f.getType()) {

}