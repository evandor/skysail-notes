package io.skysail.app.wyt.services

import io.skysail.repo.orientdb.ScalaDbService
import io.skysail.core.model.ApplicationModel
import io.skysail.app.wyt.repository.WytRepository
import org.json4s.DefaultFormats

case class ConfirmationService(dbService: ScalaDbService, appModel: ApplicationModel) {

  private var repo: WytRepository = new WytRepository(dbService)
  private implicit val formats = DefaultFormats

//  def getById(id: String): Option[Connection] = {
//    val entry = repo.findOne(id)
//    if (entry.isDefined) Some(entry.get.extract[Connection]) else None
//  }
//
//  def find(f: Filter, p: Pagination) = repo.find(f, p).map { (row => row.extract[Connection]) }.toList
//
//  def findOne(id: String): Option[Connection] = {
//    val option = repo.findOne(id)
//    if (option.isDefined) Some(option.get.extract[Connection]) else None
//  }
//
//  def save(entity: Connection): Connection = {
//    val vertex = repo.save(entity, appModel)
//    // entity.setId(vertex.getId().toString())
//    entity.copy(id = Some(vertex.get.id.toString()))
//  }


}