package io.skysail.restlet.app

import io.skysail.api.text.Translation
import io.skysail.api.um.AuthenticationService
import io.skysail.api.um.AuthenticationMode
import io.skysail.core.ApiVersion
import io.skysail.domain.Entity
import io.skysail.domain.repo.ScalaDbRepository
import io.skysail.restlet.router.ScalaSkysailRouter
import io.skysail.restlet.NoOpDbRepository
import io.skysail.restlet.utils.ScalaTranslationUtils
import io.skysail.restlet.services._
import java.util.ResourceBundle
import java.util.Collections
import java.util.ArrayList
import java.util.Arrays
import org.osgi.service.component.ComponentContext
import org.osgi.framework._
import org.osgi.service.component.annotations._
import org.restlet.data.LocalReference
import org.restlet.routing.Router
import org.restlet.resource.ServerResource
import org.restlet.Restlet
import org.restlet.data.MediaType
import org.restlet.data.Protocol
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import io.skysail.restlet.RouteBuilder
import io.skysail.restlet.utils.CompositeClassLoader
import io.skysail.restlet.utils.ClassLoaderDirectory
import io.skysail.core.security.config.SecurityConfigBuilder
import io.skysail.restlet.utils.ScalaReflectionUtils
import io.skysail.restlet.filter.OriginalRequestFilter
import io.skysail.restlet.menu.MenuItem
import io.skysail.restlet.menu.Category
import io.skysail.restlet.menu.APPLICATION_MAIN_MENU
import org.restlet.Request
import io.skysail.restlet.model.SkysailApplicationModel

object SkysailApplication {
  var serviceListProvider: ScalaServiceListProvider = null
  def setServiceListProvider(service: ScalaServiceListProvider) = this.serviceListProvider = service
  def unsetServiceListProvider(service: ScalaServiceListProvider) = this.serviceListProvider = null
}

abstract class SkysailApplication(
  name: String,
  val apiVersion: ApiVersion,
  entityClasses: List[Class[_ <: Entity]])
    extends org.restlet.Application
    with ApplicationProvider
    with ResourceBundleProvider {

  val IN_MEMORY_TRANSLATION_STORE = "InMemoryTranslationStore"

  val log = LoggerFactory.getLogger(classOf[SkysailApplication])

  var componentContext: ComponentContext = null
  def getComponentContext() = componentContext

  var applicationModel: SkysailApplicationModel = null
  def getApplicationModel() = applicationModel

  val repositories = new ArrayList[ScalaDbRepository]();

  var router: ScalaSkysailRouter = null

  val stringContextMap = new java.util.HashMap[ApplicationContextId, String]()

  setName(name);
  //getEncoderService().getIgnoredMediaTypes().add(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS);
  getEncoderService().setEnabled(true);
  log.debug("Instanciating new Skysail ApplicationModel '{}'", this.getClass().getSimpleName());
  applicationModel = new SkysailApplicationModel(name);
  //entityClasses.forEach(cls -> applicationModel.addOnce(EntityFactory.createFrom(this, cls, null)));

  def this(name: String, apiVersion: ApiVersion) {
    this(name, apiVersion, List())
  }

  def this(name: String) {
    this(name, new ApiVersion(1))
  }

  def getResourceBundles() = List[ResourceBundle]()

  def getTemplatePaths[T](x$1: Class[T]): java.util.List[String] = {
    Collections.emptyList()
  }

  @Activate
  def activate(componentContext: ComponentContext) = {
    //        log.debug("Activating ApplicationModel {}", this.getClass().getName());
    this.componentContext = componentContext;
    //getApplicationModel().setBundleContext(getBundleContext());
  }

  @Activate
  def activate(appConfig: ApplicationConfiguration, componentContext: ComponentContext): Unit = {
    activate(componentContext);
    //    if (corsConfigProvided(appConfig)) {
    //      CorsService corsService = new CorsService();
    //      configureCorsProperties(appConfig, corsService);
    //      getServices().add(corsService);
    //    }
    // this.host = appConfig.host();
  }

  @Deactivate
  def deactivate(componentContext: ComponentContext): Unit = {
    // log.debug("Deactivating ApplicationModel {}", this.getClass().getName());
    this.componentContext = null;
    //          if (router != null) {
    //              router.detachAll();
    //          }
    //          log.debug("deactivating UserManagementApplication #" + this.hashCode());
    //          try {
    //              getApplication().stop();
    //          } catch (Exception e) {
    //              log.error(e.getMessage(), e);
    //          }
    //  
    //          setInboundRoot((Restlet) null);
    //          setOutboundRoot((Restlet) null);
  }

  def attach(): Unit = {
    if (applicationModel == null) {
      //log.warn("no applicationModel defined");
      return ;
    }
    if (applicationModel.getEntityIds().isEmpty()) {
      //log.warn("there are no entities defined for the applicationModel {}", applicationModel);
      return ;
    }
    //    val firstClassEntity = (SkysailEntityModel<?>) applicationModel
    //                    .getEntity(applicationModel.getEntityIds().iterator().next());

    //            attachToRouterIfNotNull(router, "", firstClassEntity.getListResourceClass());
    //            attachToRouterIfNotNull(router, "/", firstClassEntity.getListResourceClass());
    //
    //        applicationModel.getEntityIds().stream().map(key -> applicationModel.getEntity(key)) // NOSONAR
    //                .map(SkysailEntityModel.class::cast).forEach(entity -> {
    //                    attachToRouterIfNotNull(router, "/" + entity.getId(), entity.getListResourceClass());
    //                    attachToRouterIfNotNull(router, "/" + entity.getId() + "/", entity.getPostResourceClass());
    //                    attachToRouterIfNotNull(router, "/" + entity.getId() + "/{id}", entity.getEntityResourceClass());
    //                    attachToRouterIfNotNull(router, "/" + entity.getId() + "/{id}/", entity.getPutResourceClass());
    //                });
  }

  override def createInboundRoot(): Restlet = {
    log.info("creating new Router in {}", this.getClass().getName());
    router = new ScalaSkysailRouter(this, apiVersion)

    log.info("adding extensions to metadata service");
    getMetadataService().addExtension("x-www-form-urlencoded", MediaType.APPLICATION_WWW_FORM);

    // see
    // http://nexnet.wordpress.com/2010/09/29/clap-protocol-in-restlet-and-osgi/
    //log.info("adding protocols");
    getConnectorService().getClientProtocols().add(Protocol.HTTP);
    getConnectorService().getClientProtocols().add(Protocol.FILE);
    getConnectorService().getClientProtocols().add(Protocol.CLAP);

    val securityConfigBuilder = new SecurityConfigBuilder(apiVersion);
    defineSecurityConfig(securityConfigBuilder);
    securityConfigBuilder.setAuthenticationService(SkysailApplication.serviceListProvider.getAuthenticationService());
    router.setSecurityConfig(securityConfigBuilder.build())

    //getContext().setDefaultEnroler(serviceListProvider.getAuthorizationService().getEnroler());

    log.debug("attaching application-specific routes");
    attach();

    log.debug("attaching i18n route");
    //attachI18N();

    log.debug("attaching model route");
    //attachModel();

    //log.debug("attaching swagger documentation");
    //attachSwaggerDocumentation();

    log.debug("attaching static directory");
    router.attach(createStaticDirectory());

    log.debug("creating original request filter...");
    val originalRequestFilter = new OriginalRequestFilter(getContext());
    originalRequestFilter.setNext(router);

    val authenticationService = getAuthenticationService();
    val authenticationGuard = authenticationService.getApplicationAuthenticator(getContext(), AuthenticationMode.ANONYMOUS);

    authenticationGuard.setNext(originalRequestFilter);
    return authenticationGuard;
  }

  def getBundleContext(): Option[BundleContext] = {
    if (componentContext != null) {
      return Some(componentContext.getBundleContext())
    }
    None
  }

  def addRepository(repository: ScalaDbRepository) = {
    this.repositories.add(repository);
  }

  def createStaticDirectory(): ClassLoaderDirectory = {
    val localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD,
      "/" + getName() + "/");

    val customCL = new CompositeClassLoader();
    customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
    customCL.addClassLoader(classOf[Router].getClassLoader());
    customCL.addClassLoader(this.getClass().getClassLoader());

    return new ClassLoaderDirectory(getContext(), localReference, customCL);
  }

  def getMenuEntries(): List[MenuItem] = {
    val appMenu = new MenuItem(getName(), "/" + getName() + apiVersion.getVersionPath());
    //appMenu.setCategory(APPLICATION_MAIN_MENU);
    // appMenu.setIcon(stringContextMap.get(ApplicationContextId.IMG));
    List(appMenu);
  }

  def getSkysailApplication() = this

  def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder): Unit = {
    securityConfigBuilder.authorizeRequests().startsWithMatcher("").authenticated();
  }

  def getAuthenticationService(): AuthenticationService = SkysailApplication.serviceListProvider.getAuthenticationService();
  def getMetricsCollector() = SkysailApplication.serviceListProvider.getMetricsCollector()
  def getSkysailApplicationService() = SkysailApplication.serviceListProvider.getSkysailApplicationService()

  def getBundle(): Bundle = {
    if (componentContext == null) {
      return null;
    }
    return componentContext.getBundleContext().getBundle();
  }

  //Class<? extends Entity>
  def getRepository[T <: ScalaDbRepository](entityClass: Class[_]): T = {
    val repo = repositories.asScala.filter { r =>
      val entityType = ScalaReflectionUtils.getParameterizedType(r.getClass())
      entityClass.isAssignableFrom(entityType)
    }.headOption
      .getOrElse(
        //log.warn("no matching repository found for '{}'", entityClass.getName())
        //return new NoOpDbRepository[T]()
        throw new RuntimeException("no repo"))
    repo.asInstanceOf[T]
  }

  def translate(key: String, defaultMsg: String, resource: ServerResource): Translation = {
    if (SkysailApplication.serviceListProvider == null) {
      return new Translation(defaultMsg)
    }

    val stores = SkysailApplication.serviceListProvider.getTranslationStores()

    val optionalTranslation = ScalaTranslationUtils.getBestTranslation(stores, key, resource);
    if (!optionalTranslation.isDefined) {
      return new Translation(defaultMsg);
    }
    val translation = optionalTranslation.get
    val trs = SkysailApplication.serviceListProvider.getTranslationRenderServices();
    val renderedTranslation = ScalaTranslationUtils.render(trs, translation);

    if (isNotInMemoryStore(translation)) {
      val t = stores
        .filter(IN_MEMORY_TRANSLATION_STORE == _.props.get("name"))
        .headOption
        .exists { _.store.persist(key, renderedTranslation.value, translation.locale, null) }
    }

    return null; // renderedTranslation;
  }

  def isNotInMemoryStore(translation: Translation) = IN_MEMORY_TRANSLATION_STORE != translation.getStoreName()

  //public <T extends ServerResource> List<RouteBuilder> getRouteBuilders(Class<T> cls) {
  def getRouteBuilders(cls: Class[_]): List[RouteBuilder] = {
    if (router == null) {
      return List[RouteBuilder]()
    }
    return router.getRouteBuildersForResource(cls);
  }

  def isAuthenticated(request: Request): Boolean = {
    if (SkysailApplication.serviceListProvider == null || SkysailApplication.serviceListProvider.getAuthenticationService() == null) {
      log.warn(
        "serviceListProvider or AuthenticationService is null, returning isAuthenticated => false by default.");
      return false;
    }
    return SkysailApplication.serviceListProvider.getAuthenticationService().isAuthenticated(request);
  }

  def getRouteBuildersForResource(cls: Class[_] /*Class<? extends ServerResource>*/ ): List[RouteBuilder] = {
    router.getRouteBuildersForResource(cls);
  }

}