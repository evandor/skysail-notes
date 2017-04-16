package io.skysail.core.app

import org.osgi.service.component.annotations._
import io.skysail.restlet.app.SkysailApplication
import io.skysail.restlet.app.ApplicationProvider
import org.osgi.service.cm.ManagedService
import io.skysail.restlet.services.ResourceBundleProvider
import java.util.Dictionary
import org.osgi.service.component._
import io.skysail.restlet.app.ScalaServiceListProvider
import io.skysail.core.security.config.SecurityConfigBuilder
import io.skysail.restlet.ScalaRouteBuilder
import io.skysail.core.app.resources.DefaultResource
import io.skysail.core.app.resources.LoginResource

object SkysailRootApplication {
  val ROOT_APPLICATION_NAME = "root";

  val LOGIN_PATH = "/_login";
  val LOGIN_CALLBACK = "/_logincallback";
  val PROFILE_PATH = "/_profile";
  val PUPLIC_PATH = "/_public";
  val LOGOUT_PATH = "/_logout";
}

@Component(
  immediate = true,
  property = { Array("service.pid=landingpages") },
  service = Array(classOf[ApplicationProvider], classOf[ResourceBundleProvider]))
class SkysailRootApplication extends SkysailApplication(SkysailRootApplication.ROOT_APPLICATION_NAME, null)
    with ApplicationProvider with ResourceBundleProvider with ManagedService {

  var properties: Dictionary[String, _] = null

  @Activate
  override def activate(componentContext: ComponentContext) = {
    if (getContext() != null) {
      setContext(getContext().createChildContext());
    }
    this.componentContext = componentContext;
    //setComponentContext(componentContext);
    //dumpBundlesInformationToLog(componentContext.getBundleContext().getBundles());
  }

  @Deactivate
  override def deactivate(componentContext: ComponentContext) = this.componentContext = null

  def updated(props: Dictionary[String, _]): Unit = this.properties = props

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
  def setApplicationListProvider(service: ScalaServiceListProvider) = SkysailApplication.setServiceListProvider(service)

  def unsetApplicationListProvider(service: ScalaServiceListProvider) = SkysailApplication.unsetServiceListProvider(service)

  override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder) = {
    securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
  }

  override def attach() {
    router.attach(new ScalaRouteBuilder("/", classOf[DefaultResource]))
    router.attach(new ScalaRouteBuilder(SkysailRootApplication.LOGIN_PATH, classOf[LoginResource]))
//    router.attach(new ScalaRouteBuilder(SkysailRootApplication.LOGOUT_PATH, classOf[LogoutResource]))
//    router.attach(new ScalaRouteBuilder(SkysailRootApplication.PROFILE_PATH, classOf[ProfileResource]))
//    router.attach(new ScalaRouteBuilder("/logs", classOf[LogsResource]))
  }
}