package io.skysail.app.checklists

import io.skysail.repo.orientdb.ScalaDbService
import io.skysail.core.model.ApplicationModel
import io.skysail.queryfilter.pagination.Pagination
import io.skysail.queryfilter.filter.Filter
import scala.util.Try
import org.json4s.DefaultFormats


object Services {
  def service = org.restlet.Application.getCurrent().asInstanceOf[ChecklistsApplication].service
}

class ChecklistsService(dbService: ScalaDbService, appModel: ApplicationModel) {

  private var repo = new ChecklistsRepository(dbService)
  private implicit val formats = DefaultFormats

  private var i = 0

  def create(car: Checklist): Try[Checklist] = repo.save(car, appModel)
  def update(car: Checklist): Try[Checklist] = repo.update(car, appModel)

  def getById(id: String): Option[Checklist] = {
    val entry = repo.findOne(id)
    if (entry.isDefined) Some(entry.get.extract[Checklist]) else None
  }

  def find(f: Filter, p: Pagination) = repo.find(f, p).map { row => row.extract[Checklist] }.toList

}