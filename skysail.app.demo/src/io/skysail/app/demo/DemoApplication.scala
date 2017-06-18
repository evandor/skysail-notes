package io.skysail.app.demo;

import org.osgi.service.component.annotations._
import org.restlet.data.Protocol
import java.util.Arrays
import io.skysail.app.demo.repository.TodosRepository
import io.skysail.app.demo.resources._
import io.skysail.core.security.config.SecurityConfigBuilder
import io.skysail.core.app._
import io.skysail.repo.orientdb.ScalaDbService
import io.skysail.core.ApiVersion
import io.skysail.core.restlet.RouteBuilder
import io.skysail.core.app.ApplicationConfiguration
import io.skysail.core.restlet.services.MenuItemProvider
import org.osgi.service.component.ComponentContext
import io.skysail.core.model.APPLICATION_CONTEXT_RESOURCE

object DemoApplication {
  final val APP_NAME = "demo"
}

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL, service = Array(classOf[ApplicationProvider]))
class DemoApplication extends SkysailApplication(DemoApplication.APP_NAME, new ApiVersion(int2Integer(1))) {
  
  setDescription("demo app")
  getConnectorService().getClientProtocols().add(Protocol.HTTPS)

  addAssociatedResourceClasses(List((APPLICATION_CONTEXT_RESOURCE, classOf[TodosResource])))

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
    addRepository(new TodosRepository(dbService));
  }

  override def attach() = {
    router.attach(new RouteBuilder("", classOf[TodosResource]));
    router.attach(new RouteBuilder("/todos", classOf[TodosResource]));
    router.attach(new RouteBuilder("/todos/", classOf[PostTodoResource]));
    router.attach(new RouteBuilder("/todos/{id}", classOf[TodoResource]));
    router.attach(new RouteBuilder("/todos/{id}/", classOf[PutTodoResource]));
    createStaticDirectory();
  }

  override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder) = {
    securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
  }

}