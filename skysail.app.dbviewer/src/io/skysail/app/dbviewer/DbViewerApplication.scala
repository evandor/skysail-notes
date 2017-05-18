package io.skysail.app.dbviewer;

import org.osgi.service.component.annotations._
import org.restlet.data.Protocol
import java.util.Arrays
import io.skysail.app.dbviewer.resources._
import io.skysail.core.security.config.SecurityConfigBuilder
import io.skysail.core.app._
import io.skysail.repo.orientdb.ScalaDbService
import io.skysail.core.ApiVersion
import io.skysail.restlet.RouteBuilder
import io.skysail.core.app.ApplicationConfiguration
import io.skysail.restlet.services.MenuItemProvider
import org.osgi.service.component.ComponentContext
import io.skysail.core.model.APPLICATION_CONTEXT_RESOURCE
import org.restlet.Request
import org.restlet.Response
import io.skysail.app.dbviewer.resources.ConnectionsResource
import io.skysail.app.dbviewer.repository.DbViewerRepository
import javax.sql.DataSource
import io.skysail.app.dbviewer.services.ConnectionService

object DbViewerApplication {
  final val APP_NAME = "dbviewer"
}

@Component(
  immediate = true,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  service = Array(classOf[ApplicationProvider]))
class DbViewerApplication extends SkysailApplication(
  DbViewerApplication.APP_NAME,
  new ApiVersion(int2Integer(1))) {

  setDescription("dbviewer app")
  getConnectorService().getClientProtocols().add(Protocol.HTTPS)

  addAssociatedResourceClasses(List((APPLICATION_CONTEXT_RESOURCE, classOf[ConnectionsResource])))

  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  var connectionService: ConnectionService = null
  
  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  var dbService: ScalaDbService = null

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
  def setApplicationListProvider(service: ScalaServiceListProvider) {
    SkysailApplication.serviceListProvider = service;
  }

  def unsetApplicationListProvider(service: ScalaServiceListProvider) {
    SkysailApplication.serviceListProvider = null;
  }

  @Activate
  override def activate(appConfig: ApplicationConfiguration, componentContext: ComponentContext) = {
    super.activate(appConfig, componentContext);
    addRepository(new DbViewerRepository(dbService));
  }

  override def attach() = {
    router.attach(new RouteBuilder("", classOf[ConnectionsResource]));
    router.attach(new RouteBuilder("/connections", classOf[ConnectionsResource]));
    router.attach(new RouteBuilder("/connections/", classOf[PostConnectionResource]));
    router.attach(new RouteBuilder("/connections/{id}", classOf[ConnectionResource]));
    router.attach(new RouteBuilder("/connections/{id}/", classOf[PutConnectionResource]));

    router.attach(new RouteBuilder("/connections/{id}/schemas", classOf[SchemasResource]));

    createStaticDirectory();
  }

  override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder) = {
    securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
  }

  override def handle(request: Request, response: Response) = {
    super.handle(request, response)
  }

}