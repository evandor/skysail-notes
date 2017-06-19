package io.skysail.app.checklists

import io.skysail.api.doc._
import org.json4s.DefaultFormats
import io.skysail.core.restlet.resources.ListServerResource
import io.skysail.core.restlet.ResourceContextId
import io.skysail.core.restlet.resources.PostEntityServerResource
import io.skysail.queryfilter.filter.Filter
import io.skysail.queryfilter.pagination.Pagination
import io.skysail.core.restlet.responses.ScalaSkysailResponse
import org.restlet.representation.Variant
import org.restlet.resource.Post
import org.restlet.data.Form
import io.skysail.core.restlet.transformations.Transformations
import org.json4s.jackson.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{ read, write }
import org.json4s.JsonAST.JObject
import io.skysail.core.restlet.resources.EntityServerResource
import io.skysail.core.restlet.resources.PutEntityServerResource

class ChecklistsResource extends ListServerResource[List[Checklist]](classOf[ChecklistResource]) {
  addToContext(ResourceContextId.LINK_TITLE, "Checklists")
  override def getEntity() = Services.service.find(new Filter(getRequest()), new Pagination(getRequest(), getResponse()))
  override def linkedResourceClasses() = List(classOf[PostChecklistResource], classOf[PutChecklistResource])
}

class ChecklistResource extends EntityServerResource[Checklist] {
  override def getEntity(): Checklist = Services.service.getById(getAttribute("id")).get
  override def eraseEntity() = null
  override def redirectTo() = super.redirectTo(classOf[ChecklistsResource])
}

class PostChecklistResource extends PostEntityServerResource[Checklist] {
  addToContext(ResourceContextId.LINK_TITLE, "post Checklist")
  def createEntityTemplate() = Checklist(None, "world")
  def addEntity(entity: Checklist): Checklist = Services.service.create(entity).get
  override def redirectTo() = super.redirectTo(classOf[ChecklistsResource])
}

class PutChecklistResource extends PutEntityServerResource[Checklist] {
  addToContext(ResourceContextId.LINK_TITLE, "update Checklist")
  override def getEntity(): Checklist = Services.service.getById(getAttribute("id")).get
  def updateEntity(entity: Checklist): Checklist = {
    val original = getEntity()
    copyProperties(original, entity)
    entity.id = original.id
    Services.service.update(entity).get
  }
}

