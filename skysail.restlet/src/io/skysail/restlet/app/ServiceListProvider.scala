package io.skysail.restlet.app

import io.skysail.api.validation.ValidatorService
import io.skysail.api.um.AuthorizationService
import io.skysail.api.um.AuthenticationService
import org.osgi.service.component.annotations._
import io.skysail.api.um.UserManagementProvider
import org.slf4j.LoggerFactory
import io.skysail.api.metrics.MetricsCollector
import io.skysail.api.metrics.NoOpMetricsCollector
import io.skysail.api.text.TranslationStore
import io.skysail.core.app.ScalaTranslationRenderServiceHolder
import io.skysail.core.text.ScalaTranslationStoreHolder
import io.skysail.restlet.services.ScalaSkysailComponentProvider

@org.osgi.annotation.versioning.ProviderType
trait ScalaServiceListProvider {
  //  def getValidatorService(): ValidatorService
  //  def getAuthorizationService(): AuthorizationService
  def getAuthenticationService(): AuthenticationService
  def getTranslationRenderServices(): Set[ScalaTranslationRenderServiceHolder]
  def getTranslationStores(): Set[ScalaTranslationStoreHolder]
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
  var skysailComponentProvider: ScalaSkysailComponentProvider = null

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

  val translationStores = scala.collection.mutable.Set[ScalaTranslationStoreHolder]() //Collections.synchronizedSet(new HashSet<>());
  def getTranslationStores() = translationStores.toSet

  val translationRenderServices = scala.collection.mutable.Set[ScalaTranslationRenderServiceHolder]()
  def getTranslationRenderServices() = translationRenderServices.toSet

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
  def setSkysailComponentProvider(service: ScalaSkysailComponentProvider): Unit = {
    skysailComponentProvider = service;
    val appContext = skysailComponentProvider.getSkysailComponent().getContext().createChildContext();
    getSkysailApps().foreach { app => app.setContext(appContext) }
  }

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
  def addTranslationStore(service: TranslationStore, props: Map[String, String]):Unit = {
    this.translationStores += new ScalaTranslationStoreHolder(service, props)
  }

  def removeTranslationStore(service: TranslationStore):Unit = {
    this.translationStores -= new ScalaTranslationStoreHolder(service, Map[String, String]())
  }

  @Activate
  def activate() = applicationListProvider.attach(skysailComponentProvider.getSkysailComponent())

  @Deactivate
  def deactivate() = {}

  def unsetSkysailComponentProvider(service: ScalaSkysailComponentProvider) = {
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