package io.skysail.app.wyt.resources

import io.skysail.restlet.resources.EntityServerResource
import io.skysail.api.doc._
import org.json4s.DefaultFormats
import io.skysail.app.wyt.domain.Confirmation
import io.skysail.restlet.resources.PostEntityServerResource
import io.skysail.restlet.ResourceContextId
import io.skysail.app.wyt.WytApplication
import io.skysail.app.wyt.services.Services

class PostConfirmationResource extends PostEntityServerResource[Confirmation] {

  setDescription("""resource dealing with posting confirmations""")
  addToContext(ResourceContextId.LINK_TITLE, "post confirmation")

  def createEntityTemplate() = Confirmation(None)

  @ApiSummary("returns a confirmation template")
  def addEntity(entity: Confirmation): Confirmation = {
    //Services.turns.confirm(entity)
    Confirmation(Some("1"))
  }

}