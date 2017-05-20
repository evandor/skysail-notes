package io.skysail.app.wyt.resources

import io.skysail.restlet.resources.EntityServerResource
import io.skysail.app.wyt.domain.Turn
import io.skysail.api.doc._
import io.skysail.api.responses.SkysailResponse
import org.json4s.DefaultFormats

class TurnResource extends EntityServerResource[Turn] {

  setDescription("""resource dealing with retrieving "who's next"""")

  @ApiSummary("returns the current turn")
  override def getEntity(): Option[Turn] = {
    //implicit val formats = DefaultFormats
    //val noteJValue = NotesResource.noteRepo(getSkysailApplication()).findOne(getAttribute("id"))
    //if (noteJValue.isDefined) Some(noteJValue.get.extract[Turn]) else None
    Some(Turn(Some("1")))
  }

}