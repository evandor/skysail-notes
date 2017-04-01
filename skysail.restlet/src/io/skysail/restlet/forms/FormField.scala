package io.skysail.restlet.forms

import java.lang.reflect.Field
import io.skysail.restlet.app.ScalaSkysailApplicationService
import io.skysail.restlet.model.ScalaSkysailFieldModel

case class ScalaFormField(ssfm: ScalaSkysailFieldModel, currentEntity: Any, appService: ScalaSkysailApplicationService) {
  def getId() = ssfm.getId
}