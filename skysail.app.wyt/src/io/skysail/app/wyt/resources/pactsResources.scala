package io.skysail.app.wyt.resources

import io.skysail.app.wyt.domain.Pact
import io.skysail.api.doc._
import org.json4s.DefaultFormats
import io.skysail.restlet.resources.ListServerResource
import io.skysail.restlet.ResourceContextId

class PactsResource extends ListServerResource[List[Pact]]() {

  setDescription("""resource dealing with retrieving all configured pacts""")

  addToContext(ResourceContextId.LINK_TITLE, "wyt")

  @ApiSummary("list of all pacts")
  override def getEntity() = {
    //implicit val formats = DefaultFormats
    //val noteJValue = NotesResource.noteRepo(getSkysailApplication()).findOne(getAttribute("id"))
    //if (noteJValue.isDefined) Some(noteJValue.get.extract[Pact]) else None
    List(Pact(Some("1")))
    //associatedResourceClasses(Array(classOf[TurnResource]))
  }
  override def linkedResourceClasses() = List(classOf[TurnResource])

}