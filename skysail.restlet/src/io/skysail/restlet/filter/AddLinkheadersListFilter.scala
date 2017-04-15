package io.skysail.restlet.filter

import org.slf4j.LoggerFactory

import io.skysail.restlet.ScalaAbstractResourceFilter
import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.restlet.Wrapper3
import org.restlet.data.Header
import io.skysail.restlet.ScalaAbstractListResourceFilter
import io.skysail.restlet.ScalaListResponseWrapper
import io.skysail.restlet.utils.ScalaHeadersUtils

object AddLinkheadersListFilter {
  val MAX_LINK_HEADER_SIZE = 2048
}

class AddLinkheadersListFilter[T:Manifest] extends ScalaAbstractListResourceFilter[T] {

  override val log = LoggerFactory.getLogger(classOf[AddLinkheadersListFilter[T]])

  override def afterHandle(resource: ScalaSkysailServerResource, responseWrapper: Wrapper3) = {
    val responseHeaders = ScalaHeadersUtils.getHeaders(resource.getResponse());
    val linkheaderAuthorized = resource.getAuthorizedLinks();
//    linkheaderAuthorized.forEach(getPathSubstitutions(resource));
//    val links = linkheaderAuthorized.stream().map(link -> link.toString(""))
//      .collect(Collectors.joining(","));
    val linkCount = 50;
    val limitedLinks = "50" //shrinkLinkHeaderSizeIfNecessary(linkCount, links);
    //    if (limitedLinks.length() < links.length()) {
    //      responseHeaders.add(new Header("X-Link-Error", "link header was too large: " + links.length() + " bytes, cutting down to " + limitedLinks.length() + " bytes."));
    //    }
    responseHeaders.add(new Header("Link", limitedLinks));
  }

}