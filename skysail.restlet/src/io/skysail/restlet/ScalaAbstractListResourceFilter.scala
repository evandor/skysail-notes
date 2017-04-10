package io.skysail.restlet

import io.skysail.server.restlet.response.ListResponseWrapper
import org.restlet.Response
import io.skysail.server.restlet.response.ListResponseWrapper
import io.skysail.restlet.responses.ListResponse
import io.skysail.server.restlet.filter._
import io.skysail.server.restlet.filter.FilterResult._
import org.slf4j.LoggerFactory

abstract class ScalaAbstractListResourceFilter[T] {

  val log = LoggerFactory.getLogger(this.getClass())
  var next: ScalaAbstractListResourceFilter[T] = null

  final def handle(resource: ScalaSkysailServerResource, response: Response): ScalaListResponseWrapper[T] = {
    val responseWrapper = new ScalaListResponseWrapper[T](response)
    handleMe(resource, responseWrapper)
    responseWrapper
  }

  private final def handleMe(resource: ScalaSkysailServerResource, responseWrapper: ScalaListResponseWrapper[T]): Unit = {
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

  protected def beforeHandle(resource: ScalaSkysailServerResource, responseWrapper: ScalaListResponseWrapper[T]) = CONTINUE

  protected def doHandle(resource: ScalaSkysailServerResource, responseWrapper: ScalaListResponseWrapper[T]): FilterResult = {
    val next = getNext();
    if (next != null) {
      // logger.debug("next filter in chain: {}", next.getClass().getSimpleName());
      next.handleMe(resource, responseWrapper);
    }
    CONTINUE;
  }

  def afterHandle(resource: ScalaSkysailServerResource, responseWrapper: ScalaListResponseWrapper[T]): Unit = {}

  def calling(next: ScalaAbstractListResourceFilter[T]) = {
    val lastInChain = getLast();
    lastInChain.setNext(next);
    this;
  }

  private def getLast() = {
    var result = this;
    while (result.getNext() != null) {
      result = result.getNext();
    }
    result;
  }

  private def getNext(): ScalaAbstractListResourceFilter[T] = next
  private def setNext(next: ScalaAbstractListResourceFilter[T]) = this.next = next

}