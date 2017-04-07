package io.skysail.restlet.filter

import io.skysail.restlet.ScalaAbstractResourceFilter
import org.slf4j.LoggerFactory
import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.restlet.Wrapper3
import io.skysail.server.restlet.filter.FilterResult

class FormDataExtractingFilter[T](entity: T) extends ScalaAbstractResourceFilter[T] {

  override val log = LoggerFactory.getLogger(classOf[FormDataExtractingFilter[T]])

  override def doHandle(resource: ScalaSkysailServerResource, responseWrapper: Wrapper3): FilterResult = {
    log.debug("entering {}#doHandle", this.getClass().getSimpleName());
    val response = responseWrapper.getResponse();
    if (response.getRequest() == null || response.getRequest().getResourceRef() == null) {
      log.warn("request or resourceRef was null");
      FilterResult.STOP;
    }
    try {
      responseWrapper.setEntity(entity);
    } catch {
      case e: java.text.ParseException => throw new RuntimeException("could not parse form", e);
    }

    super.doHandle(resource, responseWrapper);
  }

}