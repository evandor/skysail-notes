package io.skysail.app.checklists

import org.osgi.service.component.annotations._
import org.restlet.data.Protocol
import java.util.Arrays
import io.skysail.core.security.config.SecurityConfigBuilder
import io.skysail.core.app._
import io.skysail.repo.orientdb.ScalaDbService
import io.skysail.core.ApiVersion
import io.skysail.core.restlet.RouteBuilder
import io.skysail.core.app.ApplicationConfiguration
import io.skysail.core.restlet.services.MenuItemProvider
import org.osgi.service.component.ComponentContext
import io.skysail.core.model.APPLICATION_CONTEXT_RESOURCE

object ChecklistsApplication {
  final val APP_NAME = "checklists"
}

@Component(
  immediate = true,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  service = Array(classOf[ApplicationProvider]))
class ChecklistsApplication extends SkysailApplication(ChecklistsApplication.APP_NAME,new ApiVersion(int2Integer(1))) {

  setDescription("Checklistworld reference application")
  getConnectorService().getClientProtocols().add(Protocol.HTTPS)

  addAssociatedResourceClasses(List((APPLICATION_CONTEXT_RESOURCE, classOf[ChecklistsResource])))

  var service: ChecklistsService = null

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
    service = new ChecklistsService(dbService, getApplicationModel2())
  }

  override def attach() = {
    router.attach(new RouteBuilder("", classOf[ChecklistsResource]))
    router.attach(new RouteBuilder("/Checklists", classOf[ChecklistsResource]))
    router.attach(new RouteBuilder("/Checklists/", classOf[PostChecklistResource]))
    router.attach(new RouteBuilder("/Checklists/{id}", classOf[ChecklistResource]))
    router.attach(new RouteBuilder("/Checklists/{id}/", classOf[PutChecklistResource]))

    createStaticDirectory();
  }

  override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder) = {
    securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
  }

}