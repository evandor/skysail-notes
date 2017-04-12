package io.skysail.restlet.utils

import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.api.links.Link
import org.slf4j.LoggerFactory
import io.skysail.api.links.LinkRelation
import io.skysail.restlet.app.ScalaSkysailApplication
import org.restlet.data.MediaType
import io.skysail.restlet.ResourceContextId
import io.skysail.restlet.ScalaRouteBuilder

object ScalaLinkUtils {

  var log = LoggerFactory.getLogger(classOf[ScalaLinkUtils])

  def fromResources[_ <: ScalaSkysailServerResource](sssr: ScalaSkysailServerResource, entity: Any, classes: Seq[Class[_]]): List[Link] = {
    //    val links = Arrays.stream(classes).map(determineLink(skysailServerResource)) //
    //      .filter(lh ->
    //        lh != null).collect(Collectors.toList())
    //

    //    return links
    val links = classes.map(c => ScalaLinkUtils.fromResource(sssr, c)).filter(lh => lh != null)
    //    links.addAll(getAssociatedLinks(entity, skysailServerResource))
    links.toList
  }

  def fromResource[_ <: ScalaSkysailServerResource](sssr: ScalaSkysailServerResource, c: Class[_]) = {
    //     if (noRouteBuilderFound(skysailServerResource.getApplication(), ssr)) {
    //            log.warn("problem with linkheader for resource {} no routeBuilder was found.", ssr.getSimpleName())
    //            return null
    //        }
    createLink(sssr, c)
  }

  def createLink[_ <: ScalaSkysailServerResource](sssr: ScalaSkysailServerResource, resourceClass: Class[_]) = {

    val mode = ScalaCookiesUtils.getModeFromCookie(sssr.getRequest())

    val app = sssr.getSkysailApplication()
    val routeBuilder = app.getRouteBuilders(resourceClass)(0)
    val resource = createNewInstance(resourceClass)

    var relation = if (resource.isDefined) resource.get.getLinkRelation() else LinkRelation.ALTERNATE
    val uri = determineUri2(sssr, resourceClass, routeBuilder)
    val resourceRef = sssr.getRequest().getResourceRef()
    if (uri.equals(resourceRef.getPath())) {
      relation = LinkRelation.SELF
    }
    val linkBuilder = new Link.Builder(uri)
      .definingClass(resourceClass)
      .relation(relation)
      .title(getTitle(resource))
      .authenticationNeeded(routeBuilder.needsAuthentication)
      .needsRoles(routeBuilder.rolesForAuthorization)
      .image(MediaType.TEXT_HTML,
        if (resource.isDefined) resource.get.getFromContext(ResourceContextId.LINK_GLYPH) else null)
    //            if (mode.equals(RenderingMode.DEBUG)) {
    //                linkBuilder.alt(if (mode.equals(RenderingMode.DEBUG)) renderDebugInfo(linkBuilder, resourceClass) else "title")
    //            }
    val link = linkBuilder.build()
    log.debug("created link {}", link)
    link
  }

  private def createNewInstance(resource: Class[_]): Option[ScalaSkysailServerResource] = {
    var newInstance = null
    try {
      val cls = resource.getClass()
      val inst = resource.newInstance()
      return Some(inst.asInstanceOf[ScalaSkysailServerResource])
    } catch {
      case e: Throwable => log.error(e.getMessage(), e)
    }
    None
  }

  private def determineUri2(sssr: ScalaSkysailServerResource, resourceClass: Class[_], routeBuilder: ScalaRouteBuilder) = {
    val app = sssr.getApplication().asInstanceOf[ScalaSkysailApplication]
    val result = "/" + app.getName() + routeBuilder.getPathTemplate(app.apiVersion)
    //    try {
    //      var theTypes = resourceClass.newInstance().getRestrictedToMediaTypes()
    //      if (theTypes.size() == 1) {
    //        result += "?media=" + types.iterator().next()
    //      }
    //    } catch {
    //      case e: Throwable => log.warn(e.getMessage())
    //    }
    result
  }

  def getTitle(resource: Option[ScalaSkysailServerResource]): String = {
    if (resource.isDefined && resource.get.getFromContext(ResourceContextId.LINK_TITLE) != null) 
      resource.get.getFromContext(ResourceContextId.LINK_TITLE) 
    else
      "unknown"
  }

}

class ScalaLinkUtils {

}