package io.skysail.app.dbviewer.resources

import io.skysail.core.app.SkysailApplication
import io.skysail.restlet.ResourceContextId
import io.skysail.restlet.resources._
import io.skysail.queryfilter.filter.Filter
import io.skysail.queryfilter.pagination.Pagination
import org.json4s.DefaultFormats
import java.util.Date
import io.skysail.api.doc._
import io.skysail.app.dbviewer.domain.Connection
import io.skysail.app.dbviewer.repository.DbViewerRepository
import io.skysail.app.dbviewer.DbViewerApplication
import io.skysail.restlet.responses.ScalaSkysailResponse

object Services {
  def connections = org.restlet.Application.getCurrent().asInstanceOf[DbViewerApplication].connectionService
}

class ConnectionsResource extends ListServerResource[List[Connection]](classOf[ConnectionResource]) {
  setDescription("resource class responsible of handling requests to get the list of all Connections")
  addToContext(ResourceContextId.LINK_TITLE, "list Connections");
  override def linkedResourceClasses() = List(classOf[PostConnectionResource])
  @ApiSummary("returns the (potentially filtered, sorted and paginated) Connections")
  def getEntity() = Services.connections.find(new Filter(getRequest()), new Pagination(getRequest(), getResponse()))
}

class ConnectionResource extends EntityServerResource[Connection] {
  setDescription("resource dealing with retrieving and deleting Connections (by id)")

  @ApiSummary("returns the Connection identified by the provided path id")
  override def getEntity(): Connection = Services.connections.findOne(getAttribute("id")).get

  @ApiSummary("deletes the Connection identfied by its id")
  @ApiDescription("some description")
  @ApiTags(Array("Connection", "Testing Swagger"))
  override def eraseEntity() = null//new ScalaSkysailResponse[Connection]()
  override def redirectTo() = super.redirectTo(classOf[ConnectionsResource])
}

class PostConnectionResource extends PostEntityServerResource[Connection] {
  setDescription("adds a Connection to the repository")
  addToContext(ResourceContextId.LINK_TITLE, "create Connection");
  def createEntityTemplate() = Connection()

  @ApiSummary("post an connection representation to create a new resource")
  def addEntity(entity: Connection) = Services.connections.save(entity)
  override def redirectTo() = super.redirectTo(classOf[ConnectionsResource])
}

class PutConnectionResource extends PutEntityServerResource[Connection] {
  override def getEntity() = Services.connections.findOne(getAttribute("id")).get
  @ApiSummary("...")
  @ApiDescription("some description")
  @ApiTags(Array("Connection", "Testing Swagger"))
  def updateEntity(entity: Connection): Connection = {
    val original = getEntity()
    null
  }
}
