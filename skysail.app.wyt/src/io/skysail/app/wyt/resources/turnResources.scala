package io.skysail.app.wyt.resources

import io.skysail.restlet.resources.EntityServerResource
import io.skysail.app.wyt.domain.Turn
import io.skysail.api.doc._
import org.json4s.DefaultFormats
import io.skysail.restlet.ResourceContextId
import io.skysail.app.wyt.services.Services

class TurnResource extends EntityServerResource[Turn] {

  setDescription("""resource dealing with retrieving "who's next"""")

  addToContext(ResourceContextId.LINK_TITLE, "turn")

  @ApiSummary("returns the current turn")
  override def getEntity(): Turn = {
    //Services.turns.getNextTurn(getAttribute("id"))
    null
  }

}