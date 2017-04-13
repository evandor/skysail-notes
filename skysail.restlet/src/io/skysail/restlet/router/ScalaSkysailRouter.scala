package io.skysail.restlet.router

import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.restlet.app.SkysailApplication
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
import io.skysail.core.ApiVersion
import io.skysail.restlet.ScalaRouteBuilder
import io.skysail.core.security.config.ScalaSecurityConfig
import io.skysail.restlet.RolesPredicateAuthorizer

object ScalaSkysailRouter {
  def getResourcesGenericType(resourceInstance: ScalaSkysailServerResource) = resourceInstance.getParameterizedType()
}

class ScalaSkysailRouter(skysailApplication: SkysailApplication, apiVersion: ApiVersion) extends Router(skysailApplication.getContext()) {

  val log = LoggerFactory.getLogger(classOf[ScalaSkysailRouter])

  val pathRouteBuilderMap = new ConcurrentHashMap[String, ScalaRouteBuilder]();

  var securityConfig: ScalaSecurityConfig = null

  def setSecurityConfig(s: ScalaSecurityConfig) = this.securityConfig = s

  override def attach(pathTemplate: String, targetClass: Class[_ <: ServerResource]): TemplateRoute = {
    //log.warn("please use a RouteBuilder to attach this resource: {}", targetClass);
    return attach(pathTemplate, createFinder(targetClass));
  }

  def attach(routeBuilder: ScalaRouteBuilder): Unit = {

    updateApplicationModel(routeBuilder);

    val pathTemplate = routeBuilder.getPathTemplate(apiVersion);
    pathRouteBuilderMap.put(pathTemplate, routeBuilder);
    if (routeBuilder.targetClass == null) {
      attachForTargetClassNull(routeBuilder);
      return ;
    }

    if (!routeBuilder.needsAuthentication) {
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

  private def updateApplicationModel(routeBuilder: ScalaRouteBuilder): Unit = {
    var applicationModel = skysailApplication.getApplicationModel();
    if (applicationModel == null) {
      log.warn("applicationModel is null");
      return
    }

    val targetClass = routeBuilder.targetClass
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

  private def attachForTargetClassNull(routeBuilder: ScalaRouteBuilder) = {
    val restlet = routeBuilder.restlet
    if (restlet == null) {
      throw new IllegalStateException("RouteBuilder with neither TargetClass nor Restlet defined!");
    }
    //        log.info("routing path '{}' -> Restlet '{}'", routeBuilder.getPathTemplate(apiVersion),
    //                restlet.getClass().getSimpleName());
    restlet.setContext(getContext());
    attach(routeBuilder.getPathTemplate(apiVersion), restlet);
    updateApplicationModel(routeBuilder);
  }

  private def attachForNoAuthenticationNeeded(routeBuilder: ScalaRouteBuilder) {
    log.info("routing path '${routeBuilder.getPathTemplate(apiVersion)}' -> '{}'", routeBuilder.targetClass.getName());
    attach(routeBuilder.getPathTemplate(apiVersion), routeBuilder.targetClass);
  }

  private def createIsAuthenticatedAuthorizer(pathTemplate: String, routeBuilder: ScalaRouteBuilder): Restlet = {
    //    var predicateToUse = ""
    //    if (routeBuilder.getRolesForAuthorization() != null) 
    val predicateToUse = routeBuilder.rolesForAuthorization
    //    }
    //    routeBuilder.authorizeWith(predicateToUse);

    val c = getContext()
    val authorizer = new RolesPredicateAuthorizer(predicateToUse);
    authorizer.setContext(c);
    authorizer.setNext(routeBuilder.targetClass)

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
    val result = new scala.collection.mutable.ListBuffer[ScalaRouteBuilder]()
    for (entry <- pathRouteBuilderMap.entrySet().asScala) {
      if (entry.getValue() != null) {
        if (entry.getValue().targetClass == null) {
          val restlet = entry.getValue().restlet
          if (restlet != null) {
            handleRestlet(cls, result.toList, entry.getKey, entry.getValue, restlet);
          }
        }
        if (entry.getValue().targetClass.equals(cls)) {
          result += entry.getValue()
        }
      }
    }
    result.toList
  }

  private def handleRestlet(cls: Class[_], result: List[ScalaRouteBuilder], key: String, value: ScalaRouteBuilder,
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