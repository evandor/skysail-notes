package io.skysail.ui.raw

import org.osgi.service.cm.ConfigurationException
import org.osgi.service.component.ComponentContext
import org.osgi.service.component.annotations._
import io.skysail.restlet.app._
import io.skysail.core.app._
import io.skysail.server.security.config.SecurityConfigBuilder

object RawUiApplication {
  final val APP_NAME = "_ui/" + RawTemplatesProvider.NAMESPACE;
}

@Component(
  immediate = true,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  service = Array(classOf[ScalaApplicationProvider]))
class RawUiApplication extends ScalaSkysailApplication(RawUiApplication.APP_NAME, new ApiVersion(1)) {

  setDescription("UI provider for " + RawTemplatesProvider.NAMESPACE);

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
  def setApplicationListProvider(service: ScalaServiceListProvider) {
    serviceListProvider = service;
  }

  def unsetApplicationListProvider(service: ScalaServiceListProvider) {
    serviceListProvider = null;
  }

  @Activate
  override def activate(appConfig: ApplicationConfiguration, componentContext: ComponentContext) = {
    super.activate(appConfig, componentContext);
  }

  override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder) = {
    securityConfigBuilder.authorizeRequests().startsWithMatcher("").authenticated();
  }
}