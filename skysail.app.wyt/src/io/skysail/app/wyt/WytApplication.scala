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
   // addRepository(new NotesRepository(dbService));
  }

  override def attach() = {
    router.attach(new RouteBuilder("", classOf[PactsResource]));
//
//    router.attach(new RouteBuilder("/notes", classOf[NotesResource]));
//    router.attach(new RouteBuilder("/notes/", classOf[PostNoteResource]));
//    //router.attach(new RouteBuilder("/notes/{id}", classOf[NoteResource]));
//    //router.attach(new RouteBuilder("/notes/{id}/", classOf[PutNoteResource]));
    router.attach(new RouteBuilder("/confirmation/", classOf[PostConfirmationResource]))
    router.attach(new RouteBuilder("/turn", classOf[TurnResource]))
    createStaticDirectory();
  }

  override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder) = {
    securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
  }

}