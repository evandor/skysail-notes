package io.skysail.app.dbviewer.resources

import io.skysail.api.responses.SkysailResponse
import io.skysail.core.app.SkysailApplication
import io.skysail.restlet.ResourceContextId
import io.skysail.restlet.resources._
import io.skysail.queryfilter.filter.Filter
import io.skysail.queryfilter.pagination.Pagination
import org.json4s.DefaultFormats
import java.util.Date
import io.skysail.api.doc.ApiSummary
import io.skysail.api.doc.ApiDescription
import io.skysail.api.doc.ApiTags
import io.skysail.app.dbviewer.domain.Connection
import io.skysail.app.dbviewer.repository.DbViewerRepository
import io.skysail.app.dbviewer.DbViewerApplication
import java.sql.ResultSet
import io.skysail.app.dbviewer.domain.SchemaDetails

object SchemasResource {
  def connectionsRepo(app: SkysailApplication) = app.getRepository[DbViewerRepository](classOf[Connection])
}

class SchemasResource extends ListServerResource[SchemaDetails] { //(classOf[ConnectionResource]) {
  setDescription("resource class responsible of handling requests to get the list of all Connections")
  addToContext(ResourceContextId.LINK_TITLE, "list Connections");
  override def linkedResourceClasses() = List(classOf[PostConnectionResource])

  @ApiSummary("returns the (potentially filtered, sorted and paginated) Schemas")
  def getEntity(): List[SchemaDetails] = {
    val connService = getSkysailApplication().asInstanceOf[DbViewerApplication].connectionService
    val connection = connService.getById(getAttribute("id"))
    val ds = connService.getDataSourceForConnection(connection.get)
    val dsConnection = ds.getConnection()
    val meta = dsConnection.getMetaData()
    val columnItr = resultSetItr(meta.getColumns(null, null, "MyTable", null))
    val t = columnItr.map(col => {
      val columnType = col.getString("TYPE_NAME")
      val columnName = col.getString("COLUMN_NAME")
      val columnSize = col.getString("COLUMN_SIZE")
      SchemaDetails(Some(col.getString("TABLE_CAT")))
    }).toList
    t
  }

  def resultSetItr(resultSet: ResultSet): Stream[ResultSet] = {
    new Iterator[ResultSet] {
      def hasNext = resultSet.next()
      def next() = resultSet
    }.toStream
  }
}
