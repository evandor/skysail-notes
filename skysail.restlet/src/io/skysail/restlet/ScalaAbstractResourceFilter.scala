package io.skysail.restlet

import io.skysail.server.restlet.response.ListResponseWrapper
import org.restlet.Response
import io.skysail.server.restlet.response.ListResponseWrapper
import io.skysail.restlet.responses.ListResponse
import io.skysail.server.restlet.filter._
import io.skysail.server.restlet.filter.FilterResult._
import org.slf4j.LoggerFactory

abstract class ScalaAbstractResourceFilter[T] {

  var log = LoggerFactory.getLogger(classOf[ScalaAbstractResourceFilter[_]])

  var next: ScalaAbstractResourceFilter[T] = null

  def handleList(resource: ScalaSkysailServerResource, response: Response): ScalaResponseWrapper[T] = {
    val responseWrapper = new ScalaResponseWrapper[T](response);
    // handleMe(resource, responseWrapper);
    return responseWrapper;
  }

  private def getNext(): ScalaAbstractResourceFilter[T] = next

  protected def beforeHandle(resource: ScalaSkysailServerResource, responseWrapper: Wrapper3) = CONTINUE

  protected def doHandle(resource: ScalaSkysailServerResource, responseWrapper: Wrapper3): FilterResult = {
    val next = getNext();
    if (next != null) {
      // logger.debug("next filter in chain: {}", next.getClass().getSimpleName());
      next.handleMe(resource, responseWrapper);
    }
    CONTINUE;
  }

  def afterHandle(resource: ScalaSkysailServerResource, responseWrapper: Wrapper3)

  private final def handleMe(resource: ScalaSkysailServerResource, responseWrapper: Wrapper3): Unit = {
    beforeHandle(resource, responseWrapper) match {
      case CONTINUE => {
        doHandle(resource, responseWrapper) match {
          case CONTINUE => afterHandle(resource, responseWrapper)
          case SKIP => log.info("skipping filter chain at filter {}", this.getClass().getSimpleName());
          case STOP => log.info("stopping filter chain at filter {}", this.getClass().getSimpleName());
        }
      }
      case SKIP => {
        log.info("skipping filter chain at filter {}", this.getClass().getName());
        afterHandle(resource, responseWrapper);
      }
      case STOP => log.info("stopping filter chain at filter {}", this.getClass().getName());
    }
  }
}