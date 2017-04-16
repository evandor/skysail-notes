package io.skysail.core.app.resources

import io.skysail.restlet.resources.RedirectResource
import io.skysail.core.um.domain.Credentials
import io.skysail.restlet.ScalaSkysailServerResource

class LoginResource extends RedirectResource[Credentials] {
  def getEntity(): Any = null

  def redirectToResource(): ScalaSkysailServerResource = {
    null
  }

  override def redirectTo(): String = {
    val app = getSkysailApplication()
    if (app.isAuthenticated(getRequest())) {
      return "/";
    }
    return app.getSkysailApplication().getAuthenticationService().getLoginPath();
  }

}