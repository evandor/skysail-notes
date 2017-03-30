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

object ScalaSkysailRouter {
  def getResourcesGenericType(resourceInstance: ScalaSkysailServerResource) = resourceInstance.getParameterizedType()
}

class ScalaSkysailRouter(skysailApplication: SkysailApplication, apiVersion: ApiVersion) extends Router(skysailApplication.getContext()) {

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
    //
    //        log.info("routing path '{}' -> {}", "/" + skysailApplication.getName() + pathTemplate,
    //                routeToString(new StringBuilder(), isAuthenticatedAuthorizer).toString());
    //
    //        attach(pathTemplate, isAuthenticatedAuthorizer);
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
        // applicationModel.addOnce(EntityFactory.createFrom(skysailApplication, parameterizedType, resourceInstance));
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
    val predicateToUse = //if (routeBuilder.getRolesForAuthorization() == null) 
      //defaultRolesPredicate
      //else 
      routeBuilder.getRolesForAuthorization();
    routeBuilder.authorizeWith(predicateToUse);

    val authorizer = new RolesPredicateAuthorizer(predicateToUse);
    authorizer.setContext(getContext());
    authorizer.setNext(routeBuilder.getTargetClass());

    val authenticationGuard = securityConfig.authenticatorFor(getContext(), pathTemplate);

    authenticationGuard.setNext(authorizer);
    return authenticationGuard;
  }

}