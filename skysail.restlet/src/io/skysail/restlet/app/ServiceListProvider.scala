package io.skysail.restlet.app

import io.skysail.api.validation.ValidatorService
import io.skysail.api.um.AuthorizationService
import io.skysail.api.um.AuthenticationService
import io.skysail.server.app.TranslationRenderServiceHolder
import io.skysail.core.SkysailComponent
import org.osgi.service.component.annotations._
import io.skysail.api.um.UserManagementProvider
import org.slf4j.LoggerFactory
import io.skysail.server.app.SkysailComponentProvider
import io.skysail.api.metrics.MetricsCollector
import io.skysail.api.metrics.NoOpMetricsCollector
import io.skysail.core.text.TranslationStoreHolder
import io.skysail.api.text.TranslationStore

@org.osgi.annotation.versioning.ProviderType
trait ScalaServiceListProvider {
  //  def getValidatorService(): ValidatorService
  //  def getAuthorizationService(): AuthorizationService
  def getAuthenticationService(): AuthenticationService
  def getTranslationRenderServices(): Set[TranslationRenderServiceHolder]
  def getTranslationStores(): Set[TranslationStoreHolder]
  //  def getSkysailComponent(): SkysailComponent
  def getMetricsCollector(): MetricsCollector
  //    FacetsProvider getFacetsProvider();
  //	FilterParser getFilterParser();
  def getSkysailApplicationService(): ScalaSkysailApplicationService

}

@Component(immediate = true)
class ScalaServiceList extends ScalaServiceListProvider {

  val log = LoggerFactory.getLogger(classOf[ScalaServiceList])

  var authorizationService: AuthorizationService = null
  var authenticationService: AuthenticationService = null
  var skysailComponentProvider: SkysailComponentProvider = null

  def getAuthenticationService(): AuthenticationService = authenticationService

  //@Reference(cardinality = ReferenceCardinality.MANDATORY)
  var metricsCollector = new NoOpMetricsCollector();
  def getMetricsCollector() = metricsCollector

  @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
  @volatile var skysailApplicationService: ScalaSkysailApplicationService = null
  def getSkysailApplicationService = skysailApplicationService

  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  var applicationListProvider: ApplicationListProvider = new NoOpApplicationListProvider()
  def getApplicationListProvider() = applicationListProvider

  @Reference(policy = ReferencePolicy.STATIC, cardinality = ReferenceCardinality.MANDATORY)
  def setUserManagementProvider(provider: UserManagementProvider): Unit = {
    log.info("USER MANAGEMENT PROVIDER: setting provider to '{}'", provider.getClass().getName());
    this.authenticationService = provider.getAuthenticationService();
    this.authorizationService = provider.getAuthorizationService();
  }

  val translationStores = scala.collection.mutable.Set[TranslationStoreHolder]() //Collections.synchronizedSet(new HashSet<>());
  def getTranslationStores() = translationStores.toSet

  val translationRenderServices = scala.collection.mutable.Set[TranslationRenderServiceHolder]()
  def getTranslationRenderServices() = translationRenderServices.toSet

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
  def setSkysailComponentProvider(service: SkysailComponentProvider): Unit = {
    skysailComponentProvider = service;
    val appContext = skysailComponentProvider.getSkysailComponent().getContext().createChildContext();
    getSkysailApps().foreach { app => app.setContext(appContext) }
  }

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
  def addTranslationStore(service: TranslationStore, props: java.util.Map[String, String]):Unit = {
    this.translationStores += new TranslationStoreHolder(service, props)
  }

  def removeTranslationStore(service: TranslationStore):Unit = {
    this.translationStores -= new TranslationStoreHolder(service, new java.util.HashMap[String, String]())
  }

  @Activate
  def activate() = applicationListProvider.attach(skysailComponentProvider.getSkysailComponent())

  @Deactivate
  def deactivate() = {}

  def unsetSkysailComponentProvider(service: SkysailComponentProvider) = {
    this.skysailComponentProvider = null;
    getSkysailApps().foreach { a => a.setContext(null) }
  }

  def unsetUserManagementProvider(provider: UserManagementProvider): Unit = {
    log.info("USER MANAGEMENT PROVIDER: unsetting provider '{}'", provider.getClass().getName());
    this.authenticationService = null;
    this.authorizationService = null;
  }

  private def getSkysailApps() = {
    //    if (applicationListProvider == null) {
    //      return java.util.stream.Stream.empty()
    //    }
    //return applicationListProvider.getApplications().stream();
    applicationListProvider.getApplications()
  }

}