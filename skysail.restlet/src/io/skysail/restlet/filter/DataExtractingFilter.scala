package io.skysail.restlet.filter

import io.skysail.restlet.ScalaAbstractResourceFilter
import org.slf4j.LoggerFactory
import io.skysail.restlet.SkysailServerResource
import io.skysail.restlet.Wrapper3
import io.skysail.restlet.ScalaResponseWrapper
import io.skysail.restlet.ScalaResponseWrapper

class DataExtractingFilter[T: Manifest]( /*entity: T*/ ) extends ScalaAbstractResourceFilter[T] {

  override val log = LoggerFactory.getLogger(this.getClass())

  override def doHandle(resource: SkysailServerResource[_], responseWrapper: Wrapper3): FilterResult = {
    log.debug("entering {}#doHandle", this.getClass().getSimpleName());
    //    val response = responseWrapper.getResponse();
    //    if (response.getRequest() == null || response.getRequest().getResourceRef() == null) {
    //      log.warn("request or resourceRef was null");
    //      FilterResult.STOP;
    //    }
    //    try {
    //      val data = getDataFromRequest(response.getRequest(), resource);
    //      responseWrapper.asInstanceOf[ScalaResponseWrapper[T]].setEntity(if (data != null) data else entity);
    //    } catch {
    //      case e: java.text.ParseException => throw new RuntimeException("could not parse form", e);
    //    }

    //val installation = CookiesUtils.getInstallationFromCookie(resource.getRequest()).orElse(null);
    val entity = resource.getEntity();
    if (entity.isInstanceOf[List[_]]) {
      println(entity)
      //      List < T > data = (List < T>) entity;
      //      sanitizeIds(data);
      //
      //      responseWrapper.setEntity(data);
      //      resource.setCurrentEntity(data); // TODO why both wrapper AND resource?
    } else {
      //      sanitizeIds((T)entity);
      //responseWrapper.setEntity(entity);
      //      resource.setCurrentEntity(entity); // TODO why both wrapper AND resource?

    }
    responseWrapper.asInstanceOf[ScalaResponseWrapper[T]].setEntity(entity)
    super.doHandle(resource, responseWrapper);
    FilterResult.CONTINUE;
  }

}