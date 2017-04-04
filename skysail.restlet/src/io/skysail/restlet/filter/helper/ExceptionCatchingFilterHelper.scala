package io.skysail.restlet.filter.helper

import io.skysail.restlet.app.ScalaSkysailApplication
import io.skysail.restlet.Wrapper3
import org.slf4j.LoggerFactory
import org.restlet.data.Status

object ExceptionCatchingFilterHelper {

  val log = LoggerFactory.getLogger("Hier")//classOf[ExceptionCatchingFilterHelper])

  def handleError(e: Exception, application: ScalaSkysailApplication, responseWrapper: Wrapper3, cls: Class[_]): Unit = {
    //log.error(e.getMessage(), e);
    val genericErrorMessageForGui = cls.getSimpleName() + ".saved.failure"
    //responseWrapper.addError(genericErrorMessageForGui)
    val response = responseWrapper.getResponse()
    response.setStatus(Status.SERVER_ERROR_INTERNAL)
    //responseWrapper.addInfo(e.getMessage())
  }

}