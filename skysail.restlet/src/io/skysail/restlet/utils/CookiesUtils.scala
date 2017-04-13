package io.skysail.restlet.utils

import org.restlet.Request
import io.skysail.core.Constants
import org.restlet.data.CookieSetting

object ScalaCookiesUtils {

  def createCookie(name: String, path: String, maxAgeInSeconds: Int) = {
    val cookieSetting = new CookieSetting(name, null)
    cookieSetting.setAccessRestricted(true)
    cookieSetting.setPath(path)
    //cookieSetting.setComment("cookie to remember where to redirect to after posts or puts")
    cookieSetting.setMaxAge(maxAgeInSeconds)
    cookieSetting
  }

  def getThemeFromCookie(request: Request) = {
    getOptionalCookieValue(request, Constants.COOKIE_NAME_THEME)
  }

  def getModeFromCookie(request: Request) = {
    val cookie = getOptionalCookieValue(request, Constants.COOKIE_NAME_MODE)
    "" //RenderingMode.valueOf(getOptionalCookieValue(request, Constants.COOKIE_NAME_MODE).orElse(RenderingMode.DEFAULT.name()).toUpperCase())
  }

  def getInstallationFromCookie(request: Request) = {
    getOptionalCookieValue(request, Constants.COOKIE_NAME_INSTALLATIONS)
  }

  def getStylingFromCookie(request: Request) = {
    getOptionalCookieValue(request, Constants.COOKIE_NAME_STYLING)
  }

  def getMainPageFromCookie(request: Request) = {
    getOptionalCookieValue(request, Constants.COOKIE_NAME_MAINPAGE)
  }

  private def getOptionalCookieValue(request: Request, name: String): Option[String] = {
    if (request == null || request.getCookies() == null) {
      return None
    }
    val templateCookie = request.getCookies().getFirst(name)
    if (templateCookie == null) None else Some(templateCookie.getValue())
  }

}