package io.skysail.restlet

import io.skysail.server.restlet.response.ListResponseWrapper
import org.restlet.Response
import io.skysail.server.restlet.response.ListResponseWrapper
import io.skysail.restlet.responses.ListResponse
import io.skysail.server.restlet.filter._
import io.skysail.server.restlet.filter.FilterResult._
import org.slf4j.LoggerFactory
import org.restlet.Request
import io.skysail.restlet.resources.EntityServerResource2
import io.skysail.restlet.resources.PostEntityServerResource2

abstract class ScalaAbstractResourceFilter[T] {

  val log = LoggerFactory.getLogger(this.getClass())
  var next: ScalaAbstractResourceFilter[T] = null

  final def handle(resource: ScalaSkysailServerResource, response: Response): ScalaResponseWrapper[T] = {
    val responseWrapper = new ScalaResponseWrapper[T](response)
    handleMe(resource, responseWrapper)
    responseWrapper
  }

  private final def handleMe(resource: ScalaSkysailServerResource, responseWrapper: ScalaResponseWrapper[T]): Unit = {
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

  protected def beforeHandle(resource: ScalaSkysailServerResource, responseWrapper: Wrapper3) = CONTINUE

  protected def doHandle(resource: ScalaSkysailServerResource, responseWrapper:  ScalaResponseWrapper[T]): FilterResult = {
    val next = getNext();
    if (next != null) {
      // logger.debug("next filter in chain: {}", next.getClass().getSimpleName());
      next.handleMe(resource, responseWrapper);
    }
    CONTINUE;
  }

  def afterHandle(resource: ScalaSkysailServerResource, responseWrapper: Wrapper3): Unit = {}

  def calling(next: ScalaAbstractResourceFilter[T]) = { // AbstractResourceFilter<R, T>
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

  protected def getDataFromRequest(request: Request, resource: ScalaSkysailServerResource): Any = {
    val entityAsObject = request.getAttributes().get(ScalaSkysailServerResource.SKYSAIL_SERVER_RESTLET_ENTITY).asInstanceOf[T]
    if (entityAsObject != null) {
      if (resource.isInstanceOf[EntityServerResource2[T]]) {
      } else if (resource.isInstanceOf[PostEntityServerResource2[T]]) {
        return null //resource.asInstanceOf[PostEntityServerResource2[T]].getData(entityAsObject);
      }

      return null;
    }
    //        val form = (Form) request.getAttributes().get(EntityServerResource.SKYSAIL_SERVER_RESTLET_FORM);
    //        if (resource instanceof EntityServerResource) {
    //            return null;// ((EntityServerResource<T>) resource).getData(form);
    //        } else if (resource instanceof PostEntityServerResource) {
    //            return ((PostEntityServerResource<T>) resource).getData(form);
    //        } else if (resource instanceof PutEntityServerResource) {
    //            return null;// ((PutEntityServerResource<T>) resource).getData(form);
    //        } else if (resource instanceof PatchEntityServerResource) {
    //            return null;// ((PatchEntityServerResource<T>) resource).getData(form);
    ////        } else if (resource instanceof PostRelationResource) {
    ////            return null;//((PostRelationResource<?,?>) resource).getData(form);
    //        }

    null;
  }

  private def getNext(): ScalaAbstractResourceFilter[T] = next
  private def setNext(next: ScalaAbstractResourceFilter[T]) = this.next = next

}