package io.skysail.restlet.filter

import io.skysail.core.model.ApplicationModel
import io.skysail.restlet.ScalaAbstractListResourceFilter
import org.slf4j.LoggerFactory
import io.skysail.restlet.SkysailServerResource
import io.skysail.restlet.Wrapper3

class RedirectListFilter[T: Manifest]() extends ScalaAbstractListResourceFilter[T] {

  override val log = LoggerFactory.getLogger(classOf[AddLinkheadersListFilter[T]])

  override def afterHandle(resource: SkysailServerResource[_], responseWrapper: Wrapper3) = {
    val redirectTo = resource.redirectTo();
    if (redirectTo != null) {
      val response = responseWrapper.getResponse();
      response.redirectSeeOther(redirectTo);
    }
  }
}