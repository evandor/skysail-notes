package io.skysail.restlet.router

import io.skysail.core.app.ApiVersion
import io.skysail.core.app.SkysailApplication
import io.skysail.server.restlet.RouteBuilder
import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.server.restlet.RolesPredicateAuthorizer
import io.skysail.server.security.config.SecurityConfig
import io.skysail.restlet.app.ScalaSkysailApplication
import io.skysail.restlet.ScalaEntityFactory
import org.restlet.routing.Router
import org.restlet.resource.ServerResource
import org.restlet.routing.TemplateRoute
import org.slf4j.LoggerFactory
import org.restlet.Restlet
import org.restlet.routing.Filter
import org.restlet.resource.Finder
import java.util.concurrent.ConcurrentHashMap

import scala.collection.JavaConverters._

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
      routeToString(new StringBuilder(), isAuthenticatedAuthorizer).toString(): Any);

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

  def getRouteBuildersForResource(cls: Class[_]) = {
    val result = new scala.collection.mutable.ListBuffer[RouteBuilder]()
    for (entry <- pathRouteBuilderMap.entrySet().asScala) {
      if (entry.getValue() != null) {
        if (entry.getValue().getTargetClass() == null) {
          val restlet = entry.getValue().getRestlet();
          if (restlet != null) {
            handleRestlet(cls, result.toList, entry.getKey, entry.getValue, restlet);
          }
        }
        if (entry.getValue().getTargetClass().equals(cls)) {
          //result.add(entry.getValue());
        }
      }
    }
    result.toList
  }

  private def handleRestlet(cls: Class[_], result: List[RouteBuilder], key:String, value: RouteBuilder,
    restlet: Restlet) {
    if (restlet.isInstanceOf[Filter]) {
      val next = restlet.asInstanceOf[Filter].getNext();
      if (next == null) {
        return ;
      }
      if (next.getClass().equals(cls)) {
       // result.add(entries.getValue());
        return ;
      }
     // handleRestlet(cls, result, entries, next);
    } else if (restlet.isInstanceOf[Finder]) {
      val targetClass = restlet.asInstanceOf[Finder].getTargetClass()
      if (targetClass == null) {
        return ;
      }
      if (targetClass.equals(cls)) {
      //  result.add(entries.getValue());
        return ;
      }

    }
  }

}