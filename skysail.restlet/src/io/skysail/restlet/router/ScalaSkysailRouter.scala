package io.skysail.restlet.router

import org.restlet.routing.Router
import io.skysail.core.app.ApiVersion
import io.skysail.core.app.SkysailApplication
import org.restlet.resource.ServerResource
import org.restlet.routing.TemplateRoute
import io.skysail.server.restlet.RouteBuilder
import io.skysail.restlet.ScalaSkysailServerResource
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import io.skysail.server.restlet.RolesPredicateAuthorizer
import org.restlet.Restlet
import io.skysail.server.security.config.SecurityConfig
import io.skysail.restlet.app.ScalaSkysailApplication
import io.skysail.restlet.ScalaEntityFactory
import org.restlet.routing.Filter
import org.restlet.resource.Finder

object ScalaSkysailRouter {
  def getResourcesGenericType(resourceInstance: ScalaSkysailServerResource) = resourceInstance.getParameterizedType()
}

class ScalaSkysailRouter(skysailApplication: ScalaSkysailApplication, apiVersion: ApiVersion) extends Router(skysailApplication.getContext()) {

  val log = LoggerFactory.getLogger(classOf[ScalaSkysailRouter])

  val pathRouteBuilderMap = new ConcurrentHashMap[String, RouteBuilder]();

  var securityConfig: SecurityConfig = null

  def setSecurityConfig(s: SecurityConfig) = this.securityConfig = s

  override def attach(pathTemplate: String, targetClass: Class[_ <: ServerResource]): TemplateRoute = {
    //log.warn("please use a RouteBuilder to attach this resource: {}", targetClass);
    return attach(pathTemplate, createFinder(targetClass));
  }

  def attach(routeBuilder: RouteBuilder): Unit = {

    updateApplicationModel(routeBuilder);

    val pathTemplate = routeBuilder.getPathTemplate(apiVersion);
    pathRouteBuilderMap.put(pathTemplate, routeBuilder);
    if (routeBuilder.getTargetClass() == null) {
      attachForTargetClassNull(routeBuilder);
      return ;
    }

    if (!routeBuilder.isNeedsAuthentication()) {
      attachForNoAuthenticationNeeded(routeBuilder);
      return ;
    }
    val isAuthenticatedAuthorizer = createIsAuthenticatedAuthorizer(pathTemplate, routeBuilder);

    log.info("routing path '{}' -> {}", "/" + skysailApplication.getName() + pathTemplate,
      routeToString(new StringBuilder(), isAuthenticatedAuthorizer).toString():Any);

    attach(pathTemplate, isAuthenticatedAuthorizer);
    //
    //        if (addGenericTemplates) {
    //            String metapathTemplate = "/_meta" + pathTemplate;
    //            RouteBuilder metaRouteBuilder = new RouteBuilder(metapathTemplate, EntityMetaResource.class);
    //            log.info("routing path '{}' -> {}", "/" + skysailApplication.getName() + metapathTemplate,
    //                    "metaRouteBuilder");
    //            // routeToString(new StringBuilder(),
    //            // isAuthenticatedAuthorizer).toString());
    //            pathRouteBuilderMap.put(metapathTemplate, metaRouteBuilder);
    //            // attach(metapathTemplate, metaRouteBuilder.getTargetClass());
    //            attach(pathTemplate, createFinder(metaRouteBuilder.getTargetClass()));
    //        }

  }

  private def updateApplicationModel(routeBuilder: RouteBuilder): Unit = {
    var applicationModel = skysailApplication.getApplicationModel();
    if (applicationModel == null) {
      log.warn("applicationModel is null");
      return
    }

    val targetClass = routeBuilder.getTargetClass();
    if (targetClass != null && classOf[ScalaSkysailServerResource].isAssignableFrom(targetClass)) {
      try {
        val resourceInstance = targetClass.newInstance().asInstanceOf[ScalaSkysailServerResource];
        val parameterizedType = ScalaSkysailRouter.getResourcesGenericType(resourceInstance);
        applicationModel.addOnce(ScalaEntityFactory.createFrom(skysailApplication, parameterizedType, resourceInstance));
      } catch {
        case e: Throwable => log.error(e.getMessage(), e);
      }
    } else {
      log.debug("targetClass was null");
    }
  }

  private def attachForTargetClassNull(routeBuilder: RouteBuilder) = {
    val restlet = routeBuilder.getRestlet();
    if (restlet == null) {
      throw new IllegalStateException("RouteBuilder with neither TargetClass nor Restlet defined!");
    }
    //        log.info("routing path '{}' -> Restlet '{}'", routeBuilder.getPathTemplate(apiVersion),
    //                restlet.getClass().getSimpleName());
    restlet.setContext(getContext());
    attach(routeBuilder.getPathTemplate(apiVersion), restlet);
    updateApplicationModel(routeBuilder);
  }

  private def attachForNoAuthenticationNeeded(routeBuilder: RouteBuilder) {
    //    log.info("routing path '{}' -> '{}'", routeBuilder.getPathTemplate(apiVersion),
    //      routeBuilder.getTargetClass().getName());
    attach(routeBuilder.getPathTemplate(apiVersion), routeBuilder.getTargetClass());
  }

  private def createIsAuthenticatedAuthorizer(pathTemplate: String, routeBuilder: RouteBuilder): Restlet = {
    //    var predicateToUse = ""
    //    if (routeBuilder.getRolesForAuthorization() != null) 
    val predicateToUse = routeBuilder.getRolesForAuthorization()
    //    }
    //    routeBuilder.authorizeWith(predicateToUse);

    val c = getContext()
    val authorizer = new RolesPredicateAuthorizer(predicateToUse);
    authorizer.setContext(c);
    authorizer.setNext(routeBuilder.getTargetClass());

    val authenticationGuard = securityConfig.authenticatorFor(c, pathTemplate);

    authenticationGuard.setNext(authorizer);
    return authenticationGuard;
  }

  private def routeToString(sb: StringBuilder, restlet: Restlet): StringBuilder = {
    sb.append(restlet.getClass().getSimpleName());
    if (restlet.isInstanceOf[Filter]) {
      sb.append(" -> ").append(routeToString(new StringBuilder(), restlet.asInstanceOf[Filter].getNext()));
    } else if (restlet.isInstanceOf[Finder]) {
      sb.append(" -> ").append(restlet.asInstanceOf[Finder].getTargetClass().getSimpleName());
    } else {
      log.info("unknown: {}", restlet.getClass().getName());
    }
    return sb;
  }

}