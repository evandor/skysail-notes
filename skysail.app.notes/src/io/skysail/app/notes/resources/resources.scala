package io.skysail.app.notes.resources

import io.skysail.api.responses.SkysailResponse
import io.skysail.app.notes.domain.Note
import io.skysail.app.notes.NotesApplication
import io.skysail.server.restlet.resources.ListServerResource
import io.skysail.server.queryfilter.filtering.Filter
import io.skysail.server.queryfilter.pagination.Pagination
import io.skysail.app.notes.repository.NotesRepository
import io.skysail.server.restlet.resources.PutEntityServerResource
import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.server.ResourceContextId
import io.skysail.core.app.SkysailApplication
import java.util.Date
import io.skysail.restlet.resources.PostEntityServerResource2
import io.skysail.restlet.resources.EntityServerResource2
import io.skysail.restlet.resources.PutEntityServerResource2
import io.skysail.restlet.resources.ListServerResource2

object NotesResource {
  def noteRepo(app: SkysailApplication) = app.getRepository[NotesRepository](classOf[Note])
}

class NotesResource extends ListServerResource2[Note](classOf[NoteResource]) {
  addToContext(ResourceContextId.LINK_TITLE, "list Notes");
  def getEntity(): java.util.List[Note] = {
    val filter = new Filter(getRequest());
    val pagination = new Pagination(getRequest(), getResponse());
    NotesResource.noteRepo(getSkysailApplication()).find(filter, pagination);
  }
  //override def getLinks() = super.getLinks(classOf[PostNoteResource], classOf[NotesResource])
}

class NoteResource extends EntityServerResource2[Note] {
  def getEntity(): Note = NotesResource.noteRepo(getSkysailApplication()).findOne(getAttribute("id"))
  override def eraseEntity() = {
    NotesResource.noteRepo(getSkysailApplication()).delete(getAttribute("id"))
    new SkysailResponse[Note]()
  }
  //override def redirectTo() = super.redirectTo(classOf[NotesResource])
  //override def getLinks() = super.getLinks(classOf[PutNoteResource])
}

class PostNoteResource extends PostEntityServerResource2[Note] {
  def createEntityTemplate() = Note("", "")
  //def getEntity() = Note("","").asInstanceOf[Nothing]
  def addEntity(entity: Note): Unit = {
    entity.setCreated(new Date())
    entity.setModified(new Date())
    //val vertex = NotesResource.noteRepo(getApplication()).save(entity, getApplicationModel())
    //entity.setId(vertex.getId.toString())
    // entity.copy(id=vertex.getId.toString())
  }
  // override def redirectTo() = super.redirectTo(classOf[NotesResource])
}

class PutNoteResource extends PutEntityServerResource2[Note] {
  def getEntity() = NotesResource.noteRepo(getSkysailApplication()).findOne(getAttribute("id"))
  //override def redirectTo() = super.redirectTo(classOf[NotesResource])
  def updateEntity(entity: Note): Unit = {
    val original = getEntity()
    val originalCreated = original.getCreated()
    copyProperties(original, entity)
    original.setCreated(originalCreated)
    original.setModified(new Date())
    //NotesResource.noteRepo(getApplication()).update(original, getApplicationModel())
  }
}
