package io.skysail.app.dbviewer.services

import io.skysail.app.dbviewer.domain.Connection
import io.skysail.app.dbviewer.repository.DbViewerRepository
import io.skysail.repo.orientdb.ScalaDbService
import io.skysail.queryfilter.filter.Filter
import io.skysail.queryfilter.pagination.Pagination
import io.skysail.app.dbviewer.DbViewerApplication
import io.skysail.core.app._
import io.skysail.core.model.ApplicationModel
import org.json4s.DefaultFormats
import org.osgi.service.component.annotations._
import org.apache.commons.dbcp.BasicDataSource
import javax.sql.DataSource

class ConnectionService(dbService: ScalaDbService, appModel: ApplicationModel) {

  private var repo: DbViewerRepository = new DbViewerRepository(dbService)
  private implicit val formats = DefaultFormats

  def getById(id: String): Option[Connection] = {
    val entry = repo.findOne(id)
    if (entry.isDefined) Some(entry.get.extract[Connection]) else None
  }

  def find(f: Filter, p: Pagination) = repo.find(f, p).map { (row => row.extract[Connection]) }.toList

  def findOne(id: String): Option[Connection] = {
    val option = repo.findOne(id)
    if (option.isDefined) Some(option.get.extract[Connection]) else None
  }

  def save(entity: Connection): Connection = {
    val vertex = repo.save(entity, appModel)
    // entity.setId(vertex.getId().toString())
    entity.copy(id = Some(vertex.get.id.toString()))
  }

  def getDataSourceForConnection(connection: Connection) = {
    val ds = new BasicDataSource();
    ds.setUrl(connection.getUrl()) // jdbc:mysql://localhost/sakila
    ds.setUsername(connection.getUsername());
    ds.setDriverClassName("com.mysql.jdbc.Driver") //connection.getDriverName());
    ds.setPassword(connection.getPassword());
    ds
  }

}