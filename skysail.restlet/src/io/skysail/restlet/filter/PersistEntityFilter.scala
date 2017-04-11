package io.skysail.restlet.filter

import io.skysail.restlet.ScalaAbstractResourceFilter
import org.slf4j.LoggerFactory
import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.restlet.Wrapper3
import io.skysail.server.restlet.filter.FilterResult
import io.skysail.restlet.resources.PostEntityServerResource2
import io.skysail.restlet.ScalaResponseWrapper

class PersistEntityFilter[T:Manifest](entity: T) extends ScalaAbstractResourceFilter[T] {

  override val log = LoggerFactory.getLogger(classOf[PersistEntityFilter[T]])

  override def doHandle(resource: ScalaSkysailServerResource, responseWrapper:  Wrapper3): FilterResult = {
    log.debug("entering {}#doHandle", this.getClass().getSimpleName());
    val response = responseWrapper.getResponse();
    resource.asInstanceOf[PostEntityServerResource2[T]].addEntity(entity);
    //        val id = entity.asInstanceOf[T].getId();
    //        if (id != null) {
    //            response.setLocationRef(response.getRequest().getResourceRef().addSegment(id.replace("#", "")));
    //        }
    super.doHandle(resource, responseWrapper);
    return FilterResult.CONTINUE;
  }

}