package io.skysail.app.notes;

import org.osgi.service.component.annotations._
import org.restlet.data.Protocol
import java.util.Arrays
import io.skysail.app.notes.repository.NotesRepository
import io.skysail.app.notes.resources._
import io.skysail.core.security.config.SecurityConfigBuilder
import io.skysail.restlet.app._
import io.skysail.repo.orientdb.ScalaDbService
import io.skysail.core.ApiVersion
import io.skysail.restlet.RouteBuilder
import io.skysail.restlet.app.ApplicationConfiguration
import io.skysail.restlet.services.MenuItemProvider
import org.osgi.service.component.ComponentContext

object NotesApplication {
  final val APP_NAME = "notes"
}

@Component(
  immediate = true,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  service = Array(classOf[ApplicationProvider], classOf[MenuItemProvider]))
class NotesApplication extends SkysailApplication(
  NotesApplication.APP_NAME,
  new ApiVersion(int2Integer(1))) with MenuItemProvider {

  setDescription("notes app")
  getConnectorService().getClientProtocols().add(Protocol.HTTPS)

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
    addRepository(new NotesRepository(dbService));
  }

  override def attach() = {
    router.attach(new RouteBuilder("", classOf[NotesResource]));
    router.attach(new RouteBuilder("/notes", classOf[NotesResource]));
    router.attach(new RouteBuilder("/notes/", classOf[PostNoteResource]));
    router.attach(new RouteBuilder("/notes/{id}", classOf[NoteResource]));
    router.attach(new RouteBuilder("/notes/{id}/", classOf[PutNoteResource]));
    createStaticDirectory();
  }

  override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder) = {
    securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
  }

}