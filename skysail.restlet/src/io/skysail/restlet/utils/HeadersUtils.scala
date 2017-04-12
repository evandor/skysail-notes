package io.skysail.restlet.utils

import org.restlet.data.Header
import org.restlet.util.Series
import org.restlet.Response

object ScalaHeadersUtils {
  def getHeaders(response: Response): Series[Header] = {
    var responseHeaders: Series[Header] = null
    if (response == null) {
      responseHeaders = new Series(classOf[Header])
    } else {
      responseHeaders = response.getAttributes().get("org.restlet.http.headers").asInstanceOf[Series[Header]]
      if (responseHeaders == null) {
        responseHeaders = new Series(classOf[Header])
        response.getAttributes().put("org.restlet.http.headers", responseHeaders)
      }
    }
    responseHeaders
  }
}