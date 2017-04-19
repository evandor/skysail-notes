package io.skysail.restlet.utils

import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.api.links.Link
import org.slf4j.LoggerFactory
import io.skysail.api.links.LinkRelation
import io.skysail.restlet.app.SkysailApplication
import org.restlet.data.MediaType
import io.skysail.restlet.ResourceContextId
import io.skysail.restlet.ScalaRouteBuilder
import io.skysail.restlet.resources.ListServerResource2

object ScalaLinkUtils {

  var log = LoggerFactory.getLogger(classOf[ScalaLinkUtils])

  def fromResource(app: SkysailApplication, ssr: Class[_ <: ScalaSkysailServerResource], title: String = null) = {
    //        if (noRouteBuilderFound(app, ssr)) {
    //            log.warn("problem with linkheader for resource {}; no routeBuilder was found.", ssr.getSimpleName());
    //            return null;
    //        }
    val routeBuilder = app.getRouteBuilders(ssr)(0)
    val resource = createNewInstance(ssr);

    val link = new Link.Builder(determineUri(app, routeBuilder))
      .definingClass(ssr)
      .relation(if (resource.isDefined) resource.get.getLinkRelation() else LinkRelation.ALTERNATE)
      .title(if (resource.isDefined) getLinkTitleFromContextOrUnknonw(resource.get) else "unknown")
      .authenticationNeeded(routeBuilder.needsAuthentication)
      //.needsRoles(routeBuilder.getRolesForAuthorization())
      .image(MediaType.TEXT_HTML, if (resource.isDefined) resource.get.getFromContext(ResourceContextId.LINK_GLYPH) else null)
      .build();

    log.debug("created link {}", link);
    link;
  }

  private def determineUri(app: SkysailApplication, routeBuilder: ScalaRouteBuilder): String =
    "/" + app.getName() + routeBuilder.getPathTemplate(app.apiVersion);

  def fromResources[_ <: ScalaSkysailServerResource](sssr: ScalaSkysailServerResource, entity: Any, classes: Seq[Class[_]]): List[Link] = {
    //    val links = Arrays.stream(classes).map(determineLink(skysailServerResource)) //
    //      .filter(lh ->
    //        lh != null).collect(Collectors.toList())
    //

    //    return links
    val links = classes.map(c => ScalaLinkUtils.fromResource(sssr, c)).filter(lh => lh != null)
    val associatedLinks = getAssociatedLinks(entity, sssr)
    (links :: associatedLinks).toList
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
    val app = sssr.getApplication().asInstanceOf[SkysailApplication]
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

  def getLinkTitleFromContextOrUnknonw(resource: ScalaSkysailServerResource): String = {
    val title = resource.getFromContext(ResourceContextId.LINK_TITLE);
    if (title == null) "unknown" else title
  }

  /**
   * if the current resource is a {@link ListServerResource}, the associated
   * EntityServerResource (if existent) is analyzed for its own links.
   *
   * <p>
   * For each entity of the listServerResource, and for each associated link
   * (which serves as a template), a new link is created and is having its
   * path placeholders substituted. So, if the current ListServerResource has
   * a list with two entities of a type which defines three classes in its
   * getLinks method, we'll get six links in the result.
   * </p>
   */
  private def getAssociatedLinks(entity: Any, sssr: ScalaSkysailServerResource): List[Link] = {
    if (!(sssr.isInstanceOf[ListServerResource2[_]])) {
      return List()
    }
    val listServerResource = sssr.asInstanceOf[ListServerResource2[_]]
    val entityResourceClasses = listServerResource.getAssociatedServerResources() // List<Class<? extends SkysailServerResource<?>>>
    //        List<Link> result = new ArrayList<>();
    //
    //        if (entityResourceClasses != null && entity instanceof List) {
    //            List<SkysailServerResource<?>> esrs = ResourceUtils.createSkysailServerResources(entityResourceClasses,
    //                    skysailServerResource);
    //
    //            for (SkysailServerResource<?> esr : esrs) {
    //                List<Link> entityLinkTemplates = esr.getAuthorizedLinks();
    //                for (Object object : (List<?>) entity) {
    //                    entityLinkTemplates.stream().filter(lh ->
    //                        lh.getRole().equals(LinkRole.DEFAULT)
    //                    ).forEach(link -> addLink(link, object, listServerResource, result));
    //                }
    //            }
    //        }
    //        return result;
    null
  }

}

class ScalaLinkUtils {

}