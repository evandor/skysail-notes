//package io.skysail.restlet.utils
//
//import io.skysail.restlet.SkysailServerResource
//import org.slf4j.LoggerFactory
//import io.skysail.restlet.app.SkysailApplication
//import org.restlet.data.MediaType
//import io.skysail.restlet.ResourceContextId
//import io.skysail.restlet.RouteBuilder
//import io.skysail.restlet.resources.ListServerResource2
//import io.skysail.api.links.LinkRole
//import scala.collection.mutable.ListBuffer
//import com.fasterxml.jackson.databind.ObjectMapper
//import java.util.HashMap
//import scala.collection.JavaConverters._
//import io.skysail.core.model.LinkRelation
//
//object LinkUtils {
//
//  private var log = LoggerFactory.getLogger(classOf[LinkUtils])
//
//  private val mapper = new ObjectMapper()
//
//  @deprecated def fromResource(app: SkysailApplication, ssr: Class[_ <: SkysailServerResource[_]], title: String = null) = {
//    //        if (noRouteBuilderFound(app, ssr)) {
//    //            log.warn("problem with linkheader for resource {}; no routeBuilder was found.", ssr.getSimpleName());
//    //            return null;
//    //        }
//    val routeBuilder = app.getRouteBuilders(ssr)(0)
//    val resource = createNewInstance(ssr);
//
////    val link = new Link(
////      determineUri(app, routeBuilder),
////      cls = ssr,
////      relation = (if (resource.isDefined) resource.get.getLinkRelation() else LinkRelation.ALTERNATE),
////      title = (if (resource.isDefined) getLinkTitleFromContextOrUnknonw(resource.get) else "unknown"),
////      needsAuth = routeBuilder.needsAuthentication
////    )
//    //.needsRoles(routeBuilder.getRolesForAuthorization())
//null
////    log.debug("created link {}", link);
////    link;
//  }
//
//  private def determineUri(app: SkysailApplication, routeBuilder: RouteBuilder): String =
//    "/" + app.getName() + routeBuilder.getPathTemplate(app.apiVersion);
//
//  @deprecated def fromResources[_ <: SkysailServerResource[_]](
//    currentResource: SkysailServerResource[_],
//    entity: Any,
//    classes: Seq[Class[_]]): List[Link] = {
//
//    //println(currentResource.getClass.getName + ": ")
////    println(classes)
////    println("==============================")
//    val links = classes.map(c => LinkUtils.fromResource(currentResource, c)).filter(lh => lh != null).toList
//    links.map(l => " > " + l.toString()).foreach(println)
//    val associatedLinks = getAssociatedLinks(entity, currentResource)
//    //println(associatedLinks)
//    val res = links ++ associatedLinks
//    res.map(l => " > " + l.toString()).foreach(println)
//    res
//  }
//
//  @deprecated def fromResource[_ <: SkysailServerResource[_]](sssr: SkysailServerResource[_], c: Class[_]) = {
//    //     if (noRouteBuilderFound(skysailServerResource.getApplication(), ssr)) {
//    //            log.warn("problem with linkheader for resource {} no routeBuilder was found.", ssr.getSimpleName())
//    //            return null
//    //        }
//    createLink(sssr, c)
//  }
//
//  def createLink[_ <: SkysailServerResource[_]](currentResource: SkysailServerResource[_], linkedResourceClass: Class[_]) = {
//
//    val mode = ScalaCookiesUtils.getModeFromCookie(currentResource.getRequest())
//
//    val app = currentResource.getSkysailApplication()
//    val routeBuilder = app.getRouteBuilders(linkedResourceClass)(0)
//    val resource = createNewInstance(linkedResourceClass)
//
//    var relation = if (resource.isDefined) resource.get.getLinkRelation() else LinkRelation.ALTERNATE
//    val uri = determineUri2(currentResource, linkedResourceClass, routeBuilder)
//    val resourceRef = currentResource.getRequest().getResourceRef()
//    if (uri.equals(resourceRef.getPath())) {
//      relation = LinkRelation.SELF
//    }
//    
//    val link = new Link(
//      uri = uri,
//      cls = linkedResourceClass,
//      relation = relation,
//      title = getTitle(resource),
//      //needsAuth = routeBuilder.rolesForAuthorization,
//      needsAuth=routeBuilder.needsAuthentication
//    )
//
////    val linkBuilder = new Link.Builder(uri)
////      .relation(relation)
////      .title(getTitle(resource))
////      .authenticationNeeded(routeBuilder.needsAuthentication)
////      .needsRoles(routeBuilder.rolesForAuthorization)
////      .image(MediaType.TEXT_HTML,
////        if (resource.isDefined) resource.get.getFromContext(ResourceContextId.LINK_GLYPH) else null)
//    log.debug("created link {}", link)
//    link
//  }
//
//  private def createNewInstance(resource: Class[_]): Option[SkysailServerResource[_]] = {
//    var newInstance = null
//    try {
//      val cls = resource.getClass()
//      val inst = resource.newInstance()
//      return Some(inst.asInstanceOf[SkysailServerResource[_]])
//    } catch {
//      case e: Throwable => log.error(e.getMessage(), e)
//    }
//    None
//  }
//
//  private def determineUri2(sssr: SkysailServerResource[_], resourceClass: Class[_], routeBuilder: RouteBuilder) = {
//    val app = sssr.getApplication().asInstanceOf[SkysailApplication]
//    val result = "/" + app.getName() + routeBuilder.getPathTemplate(app.apiVersion)
//    //    try {
//    //      var theTypes = resourceClass.newInstance().getRestrictedToMediaTypes()
//    //      if (theTypes.size() == 1) {
//    //        result += "?media=" + types.iterator().next()
//    //      }
//    //    } catch {
//    //      case e: Throwable => log.warn(e.getMessage())
//    //    }
//    result
//  }
//
//  def getTitle(resource: Option[SkysailServerResource[_]]): String = {
//    if (resource.isDefined && resource.get.getFromContext(ResourceContextId.LINK_TITLE) != null)
//      resource.get.getFromContext(ResourceContextId.LINK_TITLE)
//    else
//      "unknown"
//  }
//
//  def getLinkTitleFromContextOrUnknonw(resource: SkysailServerResource[_]): String = {
//    val title = resource.getFromContext(ResourceContextId.LINK_TITLE);
//    if (title == null) "unknown" else title
//  }
//
//  /**
//   * if the current resource is a {@link ListServerResource}, the associated
//   * EntityServerResource (if existent) is analyzed for its own links.
//   *
//   * <p>
//   * For each entity of the listServerResource, and for each associated link
//   * (which serves as a template), a new link is created and is having its
//   * path placeholders substituted. So, if the current ListServerResource has
//   * a list with two entities of a type which defines three classes in its
//   * getLinks method, we'll get six links in the result.
//   * </p>
//   */
//  private def getAssociatedLinks(entity: Any, currentResource: SkysailServerResource[_]): List[Link] = {
//    if (!(currentResource.isInstanceOf[ListServerResource2[_]])) {
//      return List()
//    }
//    val listServerResource = currentResource.asInstanceOf[ListServerResource2[_]]
// //   val entityResourceClasses = listServerResource.getAssociatedServerResources() // List<Class<? extends SkysailServerResource<?>>>
//    //        List<Link> result = new ArrayList<>();
//    //
//    //        if (entityResourceClasses != null && entity instanceof List) {
//    //            List<SkysailServerResource<?>> esrs = ResourceUtils.createSkysailServerResources(entityResourceClasses,
//    //                    SkysailServerResource[_]);
//    //
//    //            for (SkysailServerResource<?> esr : esrs) {
//    //                
//    //            }
//    //        }
//    //        return result;
//            val result = ListBuffer[Link]()
///*    val esrs = ScalaResourceUtils.createSkysailServerResources(entityResourceClasses, currentResource).toList
//    for (esr <- esrs) {
//      println(s"checking ${esr.getClass.getName} for links")
//      val entityLinkTemplates = esr.getAuthorizedLinks();
//      val theList = entity.asInstanceOf[List[_]]
//      //                    for (Object object : (List<?>) entity) {
//      //                        entityLinkTemplates.stream().filter(lh ->
//      //                            lh.getRole().equals(LinkRole.DEFAULT)
//      //                        ).forEach(link -> addLink(link, object, listServerResource, result));
//      //                    }
//      if (theList != null) {
//        for (l <- theList) {
//          entityLinkTemplates
//            .filter { t => t.linkRole == LinkRole.DEFAULT }
//            .foreach { l => addLink(l, entity, listServerResource, result) }
//        }
//      }
//    }*/
//    result.toList
//  }
//
//  private def addLink(linkTemplate: Link, theObject: Any, resource: ListServerResource2[_], result: ListBuffer[Link]): Unit = {
//
//    val path = linkTemplate.uri
//    val linkedResourceClass = linkTemplate.cls // Class<? extends ServerResource>
//    val routeBuilders = resource.getSkysailApplication().getRouteBuildersForResource(linkedResourceClass); // List<RouteBuilder>
//    val pathUtils = new PathSubstitutions(resource.getRequestAttributes(), routeBuilders);
//    val substitutions = pathUtils.getFor(theObject) // Map<String, String> 
//
//    val objectsMapRepresentation = mapper.convertValue(theObject, classOf[HashMap[_, _]]); // HashMap<String,Object>
//    //        objectsMapRepresentation.keySet().stream().forEach(key -> {
//    //            if ("id".equals(key)) {
//    //                return;
//    //            }
//    //            if (objectsMapRepresentation.get(key) instanceof String) {
//    //                substitutions.put("entity."+key, (String)objectsMapRepresentation.get(key));
//    //            }
//    //        });
//    // objectsMapRepresentation.keySet().asScala
//    //   .foreach { key => ??? }
//
//    var href = path;
//
//    //        for (Entry<String, String> entry : substitutions.entrySet()) {
//    //            String substitutable = new StringBuilder("{").append(entry.getKey()).append("}").toString();
//    //            if (path.contains(substitutable)) {
//    //                href = href.replace(substitutable, entry.getValue());
//    //            }
//    //        }
//    val newLink = Link(href,alt=linkTemplate.alt, linkRole=LinkRole.LIST_VIEW,relation=LinkRelation.ITEM)
////    val newLink = new Link.Builder(linkTemplate)
////      .uri(href)
////      .alt(linkTemplate.alt)
////      .role(LinkRole.LIST_VIEW)
////      .relation(LinkRelation.ITEM)
////      //.refId(substitutions.get(pathUtils.getIdVariable()))
////      .build();
//    result += newLink
//  }
//
//}
//
//class LinkUtils {
//
//}