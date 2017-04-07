package io.skysail.restlet.utils

import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.api.links.Link
import org.slf4j.LoggerFactory

object ScalaLinkUtils {

  var log = LoggerFactory.getLogger(classOf[ScalaLinkUtils])

  def fromResources(sssr: ScalaSkysailServerResource, entity: Any, classes: Seq[Class[_]]): List[Link] = {
    //    val links = Arrays.stream(classes).map(determineLink(skysailServerResource)) //
    //      .filter(lh ->
    //        lh != null).collect(Collectors.toList());
    //
    //    links.addAll(getAssociatedLinks(entity, skysailServerResource));

    //    return links;
    classes.map(c => ScalaLinkUtils.fromResource(sssr, c)).filter(lh => lh != null).toList
  }

  def fromResource(sssr: ScalaSkysailServerResource, c: Class[_]) = {
    //     if (noRouteBuilderFound(skysailServerResource.getApplication(), ssr)) {
    //            log.warn("problem with linkheader for resource {}; no routeBuilder was found.", ssr.getSimpleName());
    //            return null;
    //        }
    createLink(sssr, c);
  }

  def createLink(sssr: ScalaSkysailServerResource, resourceClass: Class[_]) = {

    val mode = ScalaCookiesUtils.getModeFromCookie(sssr.getRequest());

    val app = sssr.getSkysailApplication();
   // val routeBuilder = app.getRouteBuilders(resourceClass).get(0);
    //        Optional<SkysailServerResource<?>> resource = createNewInstance(resourceClass);
    //
    //        LinkRelation relation = resource.isPresent() ? resource.get().getLinkRelation() : LinkRelation.ALTERNATE;
    //        String uri = determineUri2(skysailServerResource, resourceClass, routeBuilder);
    //        Reference resourceRef = skysailServerResource.getRequest().getResourceRef();
    //        if (uri.equals(resourceRef.getPath())) {
    //            relation = LinkRelation.SELF;
    //        }
    //       Builder linkBuilder = new Link.Builder(uri)
    //                .definingClass(resourceClass)
    //                .relation(relation)
    //                .title(resource.isPresent() ? resource.get().getFromContext(ResourceContextId.LINK_TITLE) : "unknown")
    //                .authenticationNeeded(routeBuilder.isNeedsAuthentication())
    //                .needsRoles(routeBuilder.getRolesForAuthorization())
    //                .image(MediaType.TEXT_HTML,
    //                        resource.isPresent() ? resource.get().getFromContext(ResourceContextId.LINK_GLYPH) : null);
    //        if (mode.equals(RenderingMode.DEBUG)) {
    //            linkBuilder.alt(mode.equals(RenderingMode.DEBUG) ? renderDebugInfo(linkBuilder, resourceClass) : "title");
    //        }
    //        Link link = linkBuilder.build();
    //
    //
    //        log.debug("created link {}", link);
    //        return link;
    //new Link()
    null
  }

}

class ScalaLinkUtils {

}