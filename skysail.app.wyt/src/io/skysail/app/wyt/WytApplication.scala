package io.skysail.app.wyt

import org.osgi.service.component.annotations._
import org.restlet.data.Protocol
import java.util.Arrays
import io.skysail.core.security.config.SecurityConfigBuilder
import io.skysail.core.app._
import io.skysail.repo.orientdb.ScalaDbService
import io.skysail.core.ApiVersion
import io.skysail.restlet.RouteBuilder
import io.skysail.core.app.ApplicationConfiguration
import io.skysail.restlet.services.MenuItemProvider
import org.osgi.service.component.ComponentContext
import io.skysail.app.wyt.resources._
import io.skysail.core.model.APPLICATION_CONTEXT_RESOURCE
import io.skysail.app.wyt.resources.PostConfirmationResource
import io.skysail.app.wyt.services.PactService
import io.skysail.app.wyt.domain.Pact
import io.skysail.app.wyt.services.ConfirmationService
import io.skysail.app.wyt.services.CarService
import io.skysail.app.wyt.resources.CarsResource

object WytApplication {
  final val APP_NAME = "wyt"
}

@Component(
  immediate = true,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  service = Array(classOf[ApplicationProvider]))
class WytApplication extends SkysailApplication(
  WytApplication.APP_NAME,
  new ApiVersion(int2Integer(1))) {

  setDescription("wait-your-turn backend application")
  getConnectorService().getClientProtocols().add(Protocol.HTTPS)

  addAssociatedResourceClasses(List((APPLICATION_CONTEXT_RESOURCE, classOf[PactsResource])))

  var pactService: PactService = null
  var confirmationService: ConfirmationService = null
  var carService: CarService = null

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
    pactService = new PactService(dbService, getApplicationModel2())
    carService = new CarService(dbService, getApplicationModel2())
    confirmationService = new ConfirmationService(dbService, getApplicationModel2())
  }

  override def attach() = {
    router.attach(new RouteBuilder("", classOf[PactsResource])); // TODO Redirect to pacts
    router.attach(new RouteBuilder("/pact/", classOf[PostPactResource]))
    router.attach(new RouteBuilder("/confirmation/", classOf[PostConfirmationResource]))
    router.attach(new RouteBuilder("/pact/{id}/turn", classOf[TurnResource]))

    router.attach(new RouteBuilder("/cars", classOf[CarsResource]))
    router.attach(new RouteBuilder("/cars/", classOf[PostCarResource]))

    createStaticDirectory();
  }

  override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder) = {
    securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
  }

}