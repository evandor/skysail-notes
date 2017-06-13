package io.skysail.app.demo.resources

import io.skysail.app.demo.DemoApplication
import io.skysail.app.demo.domain.Todo
import io.skysail.app.demo.repository.TodosRepository
import io.skysail.core.app.SkysailApplication
import io.skysail.restlet.ResourceContextId
import io.skysail.restlet.resources._
import io.skysail.queryfilter.filter.Filter
import io.skysail.queryfilter.pagination.Pagination
import org.json4s.DefaultFormats
import java.util.Date

object TodosResource {
  def todoRepo(app: SkysailApplication) = app.getRepository[TodosRepository](classOf[Todo])
}

class TodosResource extends ListServerResource[List[Todo]](classOf[TodoResource]) {
  addToContext(ResourceContextId.LINK_TITLE, "list Todos");
  def getEntity(): List[Todo] = {
    implicit val formats = DefaultFormats
    val filter = new Filter(getRequest());
    val pagination = new Pagination(getRequest(), getResponse());
    val result = TodosResource.todoRepo(getSkysailApplication()).find(filter, pagination)
    result.map { row => row.extract[Todo] }.toList
  }
  //override def getLinks() = super.getLinks(classOf[PostTodoResource]/*, classOf[TodosResource]*/)
  override def linkedResourceClasses() = List(classOf[PostTodoResource])
}

class TodoResource extends EntityServerResource[Todo] {
  override def getEntity(): Todo = {
    implicit val formats = DefaultFormats
    val TodoJValue = TodosResource.todoRepo(getSkysailApplication()).findOne(getAttribute("id"))
    //if (TodoJValue.isDefined) Some(TodoJValue.get.extract[Todo]) else None
    TodoJValue.get.extract[Todo]
  }
  override def eraseEntity() = {
    //TodosResource.TodoRepo(getSkysailApplication()).delete(getAttribute("id"))
    //new SkysailResponse[Todo]()
    null
  }
  override def redirectTo() = super.redirectTo(classOf[TodosResource])
 // override def getLinks() = super.getLinks(classOf[PutTodoResource])
}

class PostTodoResource extends PostEntityServerResource[Todo] {
  setDescription("adds a Todo to the repository")
  addToContext(ResourceContextId.LINK_TITLE, "create Todo");
  def createEntityTemplate() = Todo()
  def addEntity(entity: Todo): Todo = {
    val repo = TodosResource.todoRepo(getSkysailApplication())
    val vertex = repo.save(entity, getSkysailApplication().applicationModel2)
    // entity.setId(vertex.getId().toString())
    entity.copy(id=Some(vertex.get.id.toString()))
  }
  override def redirectTo() = super.redirectTo(classOf[TodosResource])
}

class PutTodoResource extends PutEntityServerResource[Todo] {
  override def getEntity() = {
    val r = TodosResource.todoRepo(getSkysailApplication()).findOne(getAttribute("id")).get
    null
  }
  //override def redirectTo() = super.redirectTo(classOf[TodosResource])
  def updateEntity(entity: Todo): Todo = {
    val original = getEntity()
    //    val originalCreated = original.getCreated()
    //    copyProperties(original, entity)
    //    original.setCreated(originalCreated)
    //    original.setModified(new Date())
    //    //TodosResource.TodoRepo(getApplication()).update(original, getApplicationModel())
    null
  }
}
