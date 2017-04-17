package io.skysail.um.shiro.app.resources

import io.skysail.restlet.resources.PostEntityServerResource2
import io.skysail.core.um.domain.Credentials
import org.restlet.resource.Get
import io.skysail.restlet.responses.FormResponse
import org.restlet.data.Form
import io.skysail.core.app.SkysailRootApplication
import io.skysail.um.shiro.app.ShiroUmApplication

class LoginResource extends PostEntityServerResource2[Credentials] {

  var app: ShiroUmApplication = null

  override protected def doInit() = { app = getApplication().asInstanceOf[ShiroUmApplication] }

  @Get("htmlform")
  def createForm(): FormResponse[Credentials] = {
    return new FormResponse(getResponse(), getEntity(), SkysailRootApplication.LOGIN_PATH);
  }

  override def getEntity() = new Credentials()

  def getData(form: Form): Credentials = {
    if (form == null) {
      return new Credentials();
    }
    return new Credentials(form.getFirstValue("username"), form.getFirstValue("password"));
  }

  override def createEntityTemplate() = new Credentials()

  override def addEntity(entity: Credentials) = {}

  override def redirectTo(): String = {
    if (app.isAuthenticated(getRequest())) {
      return "/";
    }
    return SkysailRootApplication.LOGIN_PATH;
  }

}