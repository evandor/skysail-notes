package io.skysail.restlet.filter

import io.skysail.restlet.ScalaAbstractResourceFilter
import org.slf4j.LoggerFactory
import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.restlet.Wrapper3
import org.restlet.data.MediaType

class PostRedirectGetFilter[T] extends ScalaAbstractResourceFilter[T] {

  override val log = LoggerFactory.getLogger(classOf[PostRedirectGetFilter[T]])

  override
    def afterHandle(resource: ScalaSkysailServerResource, responseWrapper: Wrapper3): Unit = {
    	if (resource.getQuery() == null) {
    		return
    	}
        val redirectTo =""// resource.redirectTo();
        //val noRedirects = resource.getQuery().getFirst(ScalaSkysailServerResource.NO_REDIRECTS);
//        if (redirectTo != null && noRedirects == null) {
//            val variant = (Variant) resource.getRequest().getAttributes().get(SkysailServerResource.SKYSAIL_SERVER_RESTLET_VARIANT);
//            if (MediaType.TEXT_HTML.equals(variant.getMediaType())) {
//                redirectTo = augmentWithMessageIds(redirectTo, responseWrapper.getMessageIds());
//                resource.getResponse().redirectSeeOther(redirectTo);
//            }
//        }
    }

//   private def augmentWithMessageIds( redirectTo: String,  messageIds List[Long]) = {
//        if (messageIds.isEmpty()) {
//            return redirectTo;
//        }
//        String result;
//        if (redirectTo.contains("?")) {
//            result = redirectTo + "&";
//        } else {
//            result = redirectTo + "?";
//        }
//        return result + "msgIds=" + messageIds.stream().map(id -> id.toString()).collect(Collectors.joining("|"));
//    }
  
}