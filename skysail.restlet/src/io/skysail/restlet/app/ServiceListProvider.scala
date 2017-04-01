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

@org.osgi.annotation.versioning.ProviderType
trait ScalaServiceListProvider {
  //  def getValidatorService(): ValidatorService
  //  def getAuthorizationService(): AuthorizationService
  def getAuthenticationService(): AuthenticationService
  //  def getTranslationRenderServices(): Set[TranslationRenderServiceHolder]
  // def Set<TranslationStoreHolder> getTranslationStores(): 
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
  var skysailApplicationService: ScalaSkysailApplicationService = null
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

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
  def setSkysailComponentProvider(service: SkysailComponentProvider): Unit = {
    skysailComponentProvider = service;
    val appContext = skysailComponentProvider.getSkysailComponent().getContext().createChildContext();
    getSkysailApps().foreach { app => app.setContext(appContext) }
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