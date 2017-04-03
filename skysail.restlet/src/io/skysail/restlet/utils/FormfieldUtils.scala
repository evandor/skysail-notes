package io.skysail.restlet.utils

import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.core.app.SkysailApplicationService
import io.skysail.server.forms.FormField
import io.skysail.api.responses.SkysailResponse
import io.skysail.restlet.FieldsFactory
import io.skysail.restlet.responses.ScalaSkysailResponse
import io.skysail.restlet.app.ScalaSkysailApplicationService

object FormfieldUtils {
  def determineFormfields(resource: ScalaSkysailServerResource, appService: SkysailApplicationService): java.util.Map[String, FormField] = {
    null//FieldsFactory.getFactory(resource).determineFrom(resource, appService);
  }

  def determineFormfields(response: ScalaSkysailResponse[_], resource: ScalaSkysailServerResource, appService: ScalaSkysailApplicationService) = {
    val f = FieldsFactory.getFactory(response)
    f.determineFrom(resource, appService);
  }

}