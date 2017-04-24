package io.skysail.restlet.utils

import io.skysail.restlet.SkysailServerResource
import io.skysail.api.responses.SkysailResponse
import io.skysail.restlet.FieldsFactory
import io.skysail.restlet.responses.ScalaSkysailResponse
import io.skysail.restlet.app.SkysailApplicationService
import io.skysail.restlet.forms.ScalaFormField

object ScalaFormfieldUtils {
  def determineFormfields(resource: SkysailServerResource[_], appService: SkysailApplicationService): java.util.Map[String, ScalaFormField] = {
    null//FieldsFactory.getFactory(resource).determineFrom(resource, appService);
  }

  def determineFormfields(response: ScalaSkysailResponse[_], resource: SkysailServerResource[_], appService: SkysailApplicationService) = {
    val f = FieldsFactory.getFactory(response)
    f.determineFrom(resource, appService);
  }

}