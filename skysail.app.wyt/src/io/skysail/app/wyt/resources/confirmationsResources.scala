package io.skysail.app.wyt.resources

import io.skysail.restlet.resources.EntityServerResource
import io.skysail.api.doc._
import io.skysail.api.responses.SkysailResponse
import org.json4s.DefaultFormats
import io.skysail.app.wyt.domain.Confirmation
import io.skysail.restlet.resources.PostEntityServerResource

class PostConfirmationResource extends PostEntityServerResource[Confirmation] {

  setDescription("""resource dealing with posting confirmations""")

  def createEntityTemplate(): Confirmation = {
    Confirmation(None)
  }

  @ApiSummary("returns a confirmation template")
  def addEntity(entity: Confirmation): Confirmation = {
    Confirmation(Some("1"))
  }

}