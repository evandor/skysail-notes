package io.skysail.app.notes.resources

import io.skysail.api.responses.SkysailResponse
import io.skysail.app.notes.domain.Note
import io.skysail.app.notes.NotesApplication
import java.util.Date
import io.skysail.restlet.resources.PostEntityServerResource2
import io.skysail.restlet.resources.EntityServerResource2
import io.skysail.restlet.resources.PutEntityServerResource2
import io.skysail.restlet.resources.ListServerResource2
import io.skysail.restlet.app.SkysailApplication
import io.skysail.app.notes.repository.NotesRepository
import org.json4s.DefaultFormats
import io.skysail.restlet.ResourceContextId
import io.skysail.queryfilter.filter.Filter
import io.skysail.queryfilter.pagination.Pagination


object NotesResource {
  def noteRepo(app: SkysailApplication) = app.getRepository[NotesRepository](classOf[Note])
}

class NotesResource extends ListServerResource2[Note](classOf[NoteResource]) {
  addToContext(ResourceContextId.LINK_TITLE, "list Notes");
  def getEntity(): List[Note] = {
    implicit val formats = DefaultFormats
    val filter = new Filter(getRequest());
    val pagination = new Pagination(getRequest(), getResponse());
    val result = NotesResource.noteRepo(getSkysailApplication()).find(filter, pagination)
    result.map { row => row.extract[Note] }.toList
  }
  override def getLinks() = super.getLinks(classOf[PostNoteResource], classOf[NotesResource])
}

class NoteResource extends EntityServerResource2[Note] {
  override def getEntity(): Option[Note] = NotesResource.noteRepo(getSkysailApplication()).findOne(getAttribute("id"))
  override def eraseEntity() = {
    //NotesResource.noteRepo(getSkysailApplication()).delete(getAttribute("id"))
    new SkysailResponse[Note]()
  }
  //override def redirectTo() = super.redirectTo(classOf[NotesResource])
  //override def getLinks() = super.getLinks(classOf[PutNoteResource])
}

class PostNoteResource extends PostEntityServerResource2[Note] {
  def createEntityTemplate() = Note(Some("1"),"hi")
  override def getEntity() = Note(None,"").asInstanceOf[Nothing]
  def addEntity(entity: Note): Unit = {
    println(entity)
    entity.setCreated(new Date())
    entity.setModified(new Date())
    val repo = NotesResource.noteRepo(getSkysailApplication())
    val vertex = repo.save(entity, getSkysailApplication().applicationModel)
    println(s"vertext: $vertex")
   // entity.setId(vertex.getId().toString())
    // entity.copy(id=vertex.getId.toString())
  }

  // override def redirectTo() = super.redirectTo(classOf[NotesResource])
}

class PutNoteResource extends PutEntityServerResource2[Note] {
  override def getEntity() = NotesResource.noteRepo(getSkysailApplication()).findOne(getAttribute("id"))
  //override def redirectTo() = super.redirectTo(classOf[NotesResource])
  def updateEntity(entity: Note): Unit = {
//    val original = getEntity()
//    val originalCreated = original.getCreated()
//    copyProperties(original, entity)
//    original.setCreated(originalCreated)
//    original.setModified(new Date())
//    //NotesResource.noteRepo(getApplication()).update(original, getApplicationModel())
  }
}
