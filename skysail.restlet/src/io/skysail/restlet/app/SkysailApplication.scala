package io.skysail.restlet.app

import io.skysail.api.text.Translation
import io.skysail.core.app._
import io.skysail.core.model.SkysailApplicationModel
import io.skysail.core.utils._
import io.skysail.domain.Entity
import io.skysail.domain.core.repos.DbRepository
import io.skysail.restlet.router.ScalaSkysailRouter
import io.skysail.restlet.model.ScalaSkysailApplicationModel
import io.skysail.restlet.NoOpDbRepository
import io.skysail.server.services.ResourceBundleProvider
import io.skysail.server.menus.MenuItem
import io.skysail.server.security.config.SecurityConfigBuilder
import io.skysail.server.restlet.filter.OriginalRequestFilter
import io.skysail.api.um.AuthenticationService
import io.skysail.api.um.AuthenticationMode
import io.skysail.core.app.ServiceListProvider
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
import io.skysail.restlet.utils.ScalaTranslationUtils

abstract class ScalaSkysailApplication(
  name: String,
  apiVersion: ApiVersion,
  entityClasses: List[Class[_ <: Entity]])
    extends org.restlet.Application
    with ScalaApplicationProvider
    with ResourceBundleProvider {

  val IN_MEMORY_TRANSLATION_STORE = "InMemoryTranslationStore"

  val log = LoggerFactory.getLogger(classOf[ScalaSkysailApplication])

  var componentContext: ComponentContext = null
  def getComponentContext() = componentContext

  var applicationModel: ScalaSkysailApplicationModel = null
  def getApplicationModel() = applicationModel

  val repositories = new ArrayList[DbRepository]();

  var router: ScalaSkysailRouter = null

  val stringContextMap = new java.util.HashMap[ApplicationContextId, String]()

  var serviceListProvider: ScalaServiceListProvider = null

  setName(name);
  //getEncoderService().getIgnoredMediaTypes().add(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS);
  getEncoderService().setEnabled(true);
  log.debug("Instanciating new Skysail ApplicationModel '{}'", this.getClass().getSimpleName());
  applicationModel = new ScalaSkysailApplicationModel(name);
  //entityClasses.forEach(cls -> applicationModel.addOnce(EntityFactory.createFrom(this, cls, null)));

  def this(name: String, apiVersion: ApiVersion) {
    this(name, apiVersion, List())
  }

  def this(name: String) {
    this(name, new ApiVersion(1))
  }

  def getResourceBundles(): java.util.List[ResourceBundle] = {
    Collections.emptyList()
  }

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
    securityConfigBuilder.setAuthenticationService(serviceListProvider.getAuthenticationService());
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

  def addRepository(repository: DbRepository) = {
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

  def getMenuEntries(): java.util.List[MenuItem] = {
    val appMenu = new MenuItem(getName(), "/" + getName() + apiVersion.getVersionPath());
    appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
    appMenu.setIcon(stringContextMap.get(ApplicationContextId.IMG));
    return Arrays.asList(appMenu);
  }

  def getSkysailApplication() = this

  def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder): Unit = {
    securityConfigBuilder.authorizeRequests().startsWithMatcher("").authenticated();
  }

  def getAuthenticationService(): AuthenticationService = serviceListProvider.getAuthenticationService();
  def getMetricsCollector() = serviceListProvider.getMetricsCollector()
  def getSkysailApplicationService() = serviceListProvider.getSkysailApplicationService()

  def getBundle(): Bundle = {
    if (componentContext == null) {
      return null;
    }
    return componentContext.getBundleContext().getBundle();
  }

  //Class<? extends Entity>
  def getRepository[T <: DbRepository](entityClass: Class[_]): T = {
    val repo = repositories.asScala.filter { r =>
      val entityType = ReflectionUtils.getParameterizedType(r.getClass())
      entityClass.isAssignableFrom(entityType)
    }.headOption
      .getOrElse(
        //log.warn("no matching repository found for '{}'", entityClass.getName())
        //return new NoOpDbRepository[T]()
        throw new RuntimeException("no repo"))
    repo.asInstanceOf[T]
  }

  def translate(key: String, defaultMsg: String, resource: ServerResource): Translation = {
    if (serviceListProvider == null) {
      return new Translation(defaultMsg)
    }

    val stores = serviceListProvider.getTranslationStores()

    val optionalTranslation = ScalaTranslationUtils.getBestTranslation(stores, key, resource);
    if (!optionalTranslation.isDefined) {
      return new Translation(defaultMsg);
    }
    val translation = optionalTranslation.get
    val trs = serviceListProvider.getTranslationRenderServices();
    val renderedTranslation = ScalaTranslationUtils.render(trs, translation);

    if (isNotInMemoryStore(translation)) {
      val t = stores
        .filter(IN_MEMORY_TRANSLATION_STORE == _.getProps().get("name"))
        .headOption
        .exists { _.getStore.get.persist(key, renderedTranslation.getValue(), translation.getLocale(), null) }
    }

    return null; // renderedTranslation;
  }

  def isNotInMemoryStore(translation: Translation) = IN_MEMORY_TRANSLATION_STORE != translation.getStoreName()

}