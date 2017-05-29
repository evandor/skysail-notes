package io.skysail.app.wyt.services

import io.skysail.repo.orientdb.ScalaDbService
import io.skysail.core.model.ApplicationModel
import io.skysail.app.wyt.repository.WytRepository
import org.json4s.DefaultFormats
import io.skysail.app.wyt.domain.Pact
import scala.util.Try
import io.skysail.queryfilter.filter.Filter
import io.skysail.queryfilter.pagination.Pagination
import io.skysail.app.wyt.domain.Turn

class PactService(dbService: ScalaDbService, appModel: ApplicationModel) {

  private var repo: WytRepository = new WytRepository(dbService)
  private implicit val formats = DefaultFormats
  
  private var i = 0

  def create(pact: Pact): Try[Pact] = {
    if (pact.turn == null) {
      pact.turn = Turn("default")
    }
    repo.save(pact, appModel)
  }
//  def getById(id: String): Option[Connection] = {
//    val entry = repo.findOne(id)
//    if (entry.isDefined) Some(entry.get.extract[Connection]) else None
//  }
//
  def find(f: Filter, p: Pagination) = repo.find(f, p).map { (row => row.extract[Pact]) }.toList
  
   def getNextTurn(pactId: String) = {
    if (i % 2 == 0) {
      Turn("Georgios is next")
    } else {
      Turn("Carsten is next")
    }
  }
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