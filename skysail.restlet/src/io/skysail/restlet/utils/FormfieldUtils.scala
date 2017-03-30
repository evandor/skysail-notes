package io.skysail.restlet.utils

import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.core.app.SkysailApplicationService
import io.skysail.server.forms.FormField
import io.skysail.api.responses.SkysailResponse
import io.skysail.restlet.FieldsFactory

object FormfieldUtils {
  def determineFormfields(resource: ScalaSkysailServerResource, appService: SkysailApplicationService): java.util.Map[String, FormField] = {
    null//FieldsFactory.getFactory(resource).determineFrom(resource, appService);
  }

  def determineFormfields(response: SkysailResponse[_], resource: ScalaSkysailServerResource, appService: SkysailApplicationService) {
    FieldsFactory.getFactory(response).determineFrom(resource, appService);
  }

}