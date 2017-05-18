package io.skysail.app.dbviewer.resources

import io.skysail.api.responses.SkysailResponse
import io.skysail.core.app.SkysailApplication
import io.skysail.restlet.ResourceContextId
import io.skysail.restlet.resources._
import io.skysail.queryfilter.filter.Filter
import io.skysail.queryfilter.pagination.Pagination
import org.json4s.DefaultFormats
import java.util.Date
import io.skysail.api.doc.ApiSummary
import io.skysail.api.doc.ApiDescription
import io.skysail.api.doc.ApiTags
import io.skysail.app.dbviewer.domain.Connection
import io.skysail.app.dbviewer.repository.DbViewerRepository

object ConnectionsResource {
  def connectionRepo(app: SkysailApplication) = app.getRepository[DbViewerRepository](classOf[Connection])
}

class ConnectionsResource extends ListServerResource[Connection](classOf[ConnectionResource]) {
  setDescription("resource class responsible of handling requests to get the list of all Connections")
  addToContext(ResourceContextId.LINK_TITLE, "list Connections");
  override def linkedResourceClasses() = List(classOf[PostConnectionResource])

  @ApiSummary("returns the (potentially filtered, sorted and paginated) Connections")
  @ApiDescription("some description")
  @ApiTags(Array("Connection","Testing Swagger"))
  def getEntity(): List[Connection] = {
    implicit val formats = DefaultFormats
    val filter = new Filter(getRequest());
    val pagination = new Pagination(getRequest(), getResponse());
    val result = ConnectionsResource.connectionRepo(getSkysailApplication()).find(filter, pagination)
    result.map { row => row.extract[Connection] }.toList
  }
  
}

class ConnectionResource extends EntityServerResource[Connection] {

  setDescription("resource dealing with retrieving and deleting Connections (by id)")

  @ApiSummary("returns the Connection identified by the provided path id")
  @ApiDescription("some description")
  @ApiTags(Array("Connection","Testing Swagger"))
  override def getEntity(): Option[Connection] = {
    implicit val formats = DefaultFormats
    val connectionJValue = ConnectionsResource.connectionRepo(getSkysailApplication()).findOne(getAttribute("id"))
    if (connectionJValue.isDefined) Some(connectionJValue.get.extract[Connection]) else None
  }
  
  @ApiSummary("deletes the Connection identfied by its id")
  @ApiDescription("some description")
  @ApiTags(Array("Connection","Testing Swagger"))
  override def eraseEntity() = {
    //ConnectionsResource.ConnectionRepo(getSkysailApplication()).delete(getAttribute("id"))
    new SkysailResponse[Connection]()
  }
  override def redirectTo() = super.redirectTo(classOf[ConnectionsResource])
 // override def getLinks() = super.getLinks(classOf[PutConnectionResource])
}

class PostConnectionResource extends PostEntityServerResource[Connection] {
  setDescription("adds a Connection to the repository")
  addToContext(ResourceContextId.LINK_TITLE, "create Connection");
  def createEntityTemplate() = Connection()

  @ApiSummary("...")
  @ApiDescription("some description")
  @ApiTags(Array("Connection","Testing Swagger"))
  def addEntity(entity: Connection): Connection = {
    //entity.setCreated(new Date())
    //entity.setModified(null)
    val repo = ConnectionsResource.connectionRepo(getSkysailApplication())
    val vertex = repo.save(entity, getSkysailApplication().applicationModel2)
    // entity.setId(vertex.getId().toString())
    entity.copy(id=Some(vertex.get.id.toString()))
  }
  override def redirectTo() = super.redirectTo(classOf[ConnectionsResource])
}

class PutConnectionResource extends PutEntityServerResource[Connection] {
  override def getEntity() = ConnectionsResource.connectionRepo(getSkysailApplication()).findOne(getAttribute("id"))
  //override def redirectTo() = super.redirectTo(classOf[ConnectionsResource])
  @ApiSummary("...")
  @ApiDescription("some description")
  @ApiTags(Array("Connection","Testing Swagger"))
  def updateEntity(entity: Connection): Unit = {
    val original = getEntity()
    //    val originalCreated = original.getCreated()
    //    copyProperties(original, entity)
    //    original.setCreated(originalCreated)
    //    original.setModified(new Date())
    //    //ConnectionsResource.ConnectionRepo(getApplication()).update(original, getApplicationModel())
  }
}
