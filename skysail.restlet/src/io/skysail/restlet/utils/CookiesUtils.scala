package io.skysail.restlet.utils

import org.restlet.Request
import io.skysail.server.Constants

object ScalaCookiesUtils {
  def getModeFromCookie(request: Request) = {

    val cookie = getOptionalCookieValue(request, Constants.COOKIE_NAME_MODE)

    ""//RenderingMode.valueOf(getOptionalCookieValue(request, Constants.COOKIE_NAME_MODE).orElse(RenderingMode.DEFAULT.name()).toUpperCase())
  }

  def getOptionalCookieValue(request: Request, name: String): Option[String] = {
    if (request == null || request.getCookies() == null) {
      return None
    }
    val templateCookie = request.getCookies().getFirst(name)
    if (templateCookie == null) None else Some(templateCookie.getValue())
  }

}