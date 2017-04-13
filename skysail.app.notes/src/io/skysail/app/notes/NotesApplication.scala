package io.skysail.app.notes;

import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.ConfigurationPolicy
import org.osgi.service.component.annotations.Reference
import org.restlet.data.Protocol
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy
import org.osgi.service.component.annotations.Activate
import org.osgi.service.component.ComponentContext
import org.restlet.data.Protocol
import java.util.Arrays
import io.skysail.app.notes.resources.NotesResource
import io.skysail.app.notes.repository.NotesRepository
import org.osgi.service.component.annotations._
import io.skysail.app.notes.resources.PutNoteResource
import io.skysail.app.notes.resources.NoteResource
import io.skysail.app.notes.resources.PostNoteResource
import io.skysail.restlet.app.SkysailApplication
import io.skysail.restlet.app.ApplicationProvider
import io.skysail.restlet.app.ScalaServiceListProvider
import io.skysail.repo.orientdb.ScalaDbService
import io.skysail.core.ApiVersion
import io.skysail.restlet.ScalaRouteBuilder
import io.skysail.core.security.config.ScalaSecurityConfigBuilder
import io.skysail.restlet.app.ApplicationConfiguration
import io.skysail.restlet.services.MenuItemProvider

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
    router.attach(new ScalaRouteBuilder("", classOf[NotesResource]));
    router.attach(new ScalaRouteBuilder("/notes", classOf[NotesResource]));
    router.attach(new ScalaRouteBuilder("/notes/", classOf[PostNoteResource]));
    router.attach(new ScalaRouteBuilder("/notes/{id}", classOf[NoteResource]));
    router.attach(new ScalaRouteBuilder("/notes/{id}/", classOf[PutNoteResource]));
    createStaticDirectory();
  }

  override def defineSecurityConfig(securityConfigBuilder: ScalaSecurityConfigBuilder) = {
    securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
  }

}