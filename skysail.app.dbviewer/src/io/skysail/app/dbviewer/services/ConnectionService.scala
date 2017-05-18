package io.skysail.app.dbviewer.services

import org.osgi.service.component.annotations._
import io.skysail.app.dbviewer.domain.Connection
import io.skysail.repo.orientdb.ScalaDbService
import io.skysail.app.dbviewer.repository.DbViewerRepository
import org.json4s.DefaultFormats
import javax.sql.DataSource
import org.apache.commons.dbcp.BasicDataSource

@Component(service = Array(classOf[ConnectionService]))
class ConnectionService {

  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  var dbService: ScalaDbService = null

  var repo: DbViewerRepository = _

  @Activate
  def activate() { repo = new DbViewerRepository(dbService) }

  def getById(id: String): Option[Connection] = {
    implicit val formats = DefaultFormats
    val entry = repo.findOne(id)
    if (entry.isDefined) Some(entry.get.extract[Connection]) else None
  }
  
  def getDataSourceForConnection(connection: Connection) = {
    val ds = new BasicDataSource();
    ds.setUrl("jdbc:mysql://localhost/sakila")//connection.getUrl());
    ds.setUsername(connection.getUsername());
    ds.setDriverClassName("com.mysql.jdbc.Driver")//connection.getDriverName());
    ds.setPassword(connection.getPassword());
    ds
  }
 
}