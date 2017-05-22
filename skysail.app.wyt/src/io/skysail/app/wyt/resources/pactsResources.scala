package io.skysail.app.wyt.resources

import io.skysail.app.wyt.domain.Pact
import io.skysail.api.doc._
import org.json4s.DefaultFormats
import io.skysail.restlet.resources.ListServerResource
import io.skysail.restlet.ResourceContextId
import io.skysail.restlet.resources.PostEntityServerResource
import io.skysail.app.wyt.services.Services
import io.skysail.queryfilter.filter.Filter
import io.skysail.queryfilter.pagination.Pagination
import io.skysail.app.wyt.domain.Turn

class PactsResource extends ListServerResource[List[Pact]]() {

  setDescription("""resource dealing with retrieving all configured pacts""")

  addToContext(ResourceContextId.LINK_TITLE, "wyt")

  @ApiSummary("list of all pacts")
  override def getEntity() = {
    Services.pacts.find(new Filter(getRequest()), new Pagination(getRequest(),getResponse()))
    //associatedResourceClasses(Array(classOf[TurnResource]))
  }
  override def linkedResourceClasses() = List(
      classOf[TurnResource], classOf[PostConfirmationResource], classOf[PostPactResource])

}

class PostPactResource extends PostEntityServerResource[Pact] {

  setDescription("""resource dealing with posting pacts""")
  addToContext(ResourceContextId.LINK_TITLE, "post pact")

  def createEntityTemplate() = Pact(None,"initial", new Turn(None,Some("initial")))

  @ApiSummary("returns a pact template")
  def addEntity(entity: Pact): Pact = {
    //Services.turns.confirm(entity)
    val pact = Services.pacts.create(entity)
    //Pact(Some("1"), "default pact")
    pact.get
  }

}