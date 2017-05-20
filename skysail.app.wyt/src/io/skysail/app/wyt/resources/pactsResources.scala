package io.skysail.app.wyt.resources

import io.skysail.app.wyt.domain.Pact
import io.skysail.api.doc._
import io.skysail.api.responses.SkysailResponse
import org.json4s.DefaultFormats
import io.skysail.restlet.resources.ListServerResource

class PactsResource extends ListServerResource[Pact]() {

  setDescription("""resource dealing with retrieving all configured pacts""")

  @ApiSummary("rePacts the current Pact")
  override def getEntity(): Option[Pact] = {
    //implicit val formats = DefaultFormats
    //val noteJValue = NotesResource.noteRepo(getSkysailApplication()).findOne(getAttribute("id"))
    //if (noteJValue.isDefined) Some(noteJValue.get.extract[Pact]) else None
    Some(Pact(Some("1")))
  }

}