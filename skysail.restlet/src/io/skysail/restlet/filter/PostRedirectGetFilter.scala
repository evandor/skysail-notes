package io.skysail.restlet.filter

import io.skysail.restlet.ScalaAbstractResourceFilter
import org.slf4j.LoggerFactory
import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.restlet.Wrapper3
import org.restlet.data.MediaType
import org.restlet.representation.Variant

class PostRedirectGetFilter[T: Manifest](variant: Variant) extends ScalaAbstractResourceFilter[T] {

  override val log = LoggerFactory.getLogger(classOf[PostRedirectGetFilter[T]])

  override def afterHandle(resource: ScalaSkysailServerResource, responseWrapper: Wrapper3): Unit = {
    if (resource.getQuery() == null) {
      return
    }
    var redirectTo = resource.redirectTo();
    val noRedirects = resource.getQuery().getFirst(ScalaSkysailServerResource.NO_REDIRECTS);
    if (redirectTo != null && noRedirects == null) {
      //val variant = resource.getRequest().getAttributes().get(ScalaSkysailServerResource.SKYSAIL_SERVER_RESTLET_VARIANT).asInstanceOf[Variant]
      if (MediaType.TEXT_HTML.equals(variant.getMediaType())) {
        //redirectTo = augmentWithMessageIds(redirectTo, responseWrapper.getMessageIds());
        resource.getResponse().redirectSeeOther(redirectTo);
      }
    }
  }

  private def augmentWithMessageIds(redirectTo: String, messageIds: List[Long]): String = {
    if (messageIds.isEmpty) {
      return redirectTo;
    }
    var result: String = ""
    if (redirectTo.contains("?")) {
      result = redirectTo + "&";
    } else {
      result = redirectTo + "?";
    }
    return result + "msgIds=" + messageIds.map(id => id.toString()).mkString("|")
  }

}