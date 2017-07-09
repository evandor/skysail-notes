package io.skysail.app.notes.resources

import io.skysail.app.notes.NotesApplication
import io.skysail.app.notes.domain.Note
import io.skysail.app.notes.repository.NotesRepository
import io.skysail.core.app.SkysailApplication
import io.skysail.core.restlet.ResourceContextId
import io.skysail.core.restlet.resources._
import io.skysail.queryfilter.filter.Filter
import io.skysail.queryfilter.pagination.Pagination
import org.json4s.DefaultFormats
import java.util.Date
import io.skysail.api.doc.ApiSummary
import io.skysail.api.doc.ApiDescription
import io.skysail.api.doc.ApiTags

object NotesResource {
  def noteRepo(app: SkysailApplication) = app.getRepository[NotesRepository](classOf[Note])
}

class NotesResource extends ListServerResource[List[Note]](classOf[NoteResource]) {
  setDescription("resource class responsible of handling requests to get the list of all notes")
  addToContext(ResourceContextId.LINK_TITLE, "list Notes");
  override def linkedResourceClasses() = List(classOf[PostNoteResource])

  @ApiSummary("returns the (potentially filtered, sorted and paginated) notes")
  @ApiDescription("some description")
  @ApiTags(Array("Note","Testing Swagger"))
  def getEntity(): List[Note] = {
    implicit val formats = DefaultFormats
    val filter = new Filter(getRequest());
    val pagination = new Pagination(getRequest(), getResponse());
    val result = NotesResource.noteRepo(getSkysailApplication()).find(filter, pagination)
    result.map { row => row.extract[Note] }.toList
  }
  
}

class NoteResource extends EntityServerResource[Note] {

  setDescription("resource dealing with retrieving and deleting notes (by id)")

  @ApiSummary("returns the note identified by the provided path id")
  @ApiDescription("some description")
  @ApiTags(Array("Note","Testing Swagger"))
  override def getEntity(): Note = {
    implicit val formats = DefaultFormats
    val noteJValue = NotesResource.noteRepo(getSkysailApplication()).findOne(getAttribute("id"))
    //if (noteJValue.isDefined) Some(noteJValue.get.extract[Note]) else None
    noteJValue.get.extract[Note]
  }
  
  @ApiSummary("deletes the note identfied by its id")
  @ApiDescription("some description")
  @ApiTags(Array("Note","Testing Swagger"))
  override def eraseEntity() = {
    //NotesResource.noteRepo(getSkysailApplication()).delete(getAttribute("id"))
    //new SkysailResponse[Note]()
    null
  }
  override def redirectTo() = super.redirectTo(classOf[NotesResource])
 // override def getLinks() = super.getLinks(classOf[PutNoteResource])
}

class PostNoteResource extends PostEntityServerResource[Note] {
  setDescription("adds a note to the repository")
  addToContext(ResourceContextId.LINK_TITLE, "create Note");
  def createEntityTemplate() = Note()

  @ApiSummary("...")
  @ApiDescription("some description")
  @ApiTags(Array("Note","Testing Swagger"))
  def addEntity(entity: Note): Note = {
    entity.setCreated(new Date())
    entity.setModified(null)
    val repo = NotesResource.noteRepo(getSkysailApplication())
    val vertex = repo.save(entity, getModel)
    // entity.setId(vertex.getId().toString())
    entity.copy(id=Some(vertex.get.id.toString()))
  }
  override def redirectTo() = super.redirectTo(classOf[NotesResource])
}

class PutNoteResource extends PutEntityServerResource[Note] {
  override def getEntity() = {
    NotesResource.noteRepo(getSkysailApplication()).findOne(getAttribute("id")).get
    null
  }
  //override def redirectTo() = super.redirectTo(classOf[NotesResource])
  @ApiSummary("...")
  @ApiDescription("some description")
  @ApiTags(Array("Note","Testing Swagger"))
  def updateEntity(entity: Note): Note = {
    val original = getEntity()
    //    val originalCreated = original.getCreated()
    //    copyProperties(original, entity)
    //    original.setCreated(originalCreated)
    //    original.setModified(new Date())
    //    //NotesResource.noteRepo(getApplication()).update(original, getApplicationModel())
    null
  }
}
