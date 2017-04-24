package io.skysail.restlet.filter

import org.slf4j.LoggerFactory
import io.skysail.restlet.ScalaAbstractResourceFilter
import io.skysail.restlet.SkysailServerResource
import io.skysail.restlet.Wrapper3
import java.util.HashSet
import io.skysail.restlet.ScalaResponseWrapper

class CheckBusinessViolationsFilter[T:Manifest](entity: T) extends ScalaAbstractResourceFilter[T] {

  override val log = LoggerFactory.getLogger(classOf[CheckBusinessViolationsFilter[T]])

  override def doHandle(resource: SkysailServerResource[_], responseWrapper:  Wrapper3): FilterResult = {
    log.debug("entering {}#doHandle", this.getClass().getSimpleName());
    val  violations = validate(entity);
    val response = responseWrapper.getResponse();
    if (!violations.isEmpty) {
      log.info("found {} business validation violation(s): {}", violations.length, violations.toString());
      //            responseWrapper.setConstraintViolationResponse(new ConstraintViolationsResponse<>(response, (T)responseWrapper.getEntity(), violations));
      //            responseWrapper.setEntity(entity);
      //            resource.setCurrentEntity(entity);
      //            response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
      //            response.getHeaders().add("X-Status-Reason", "Validation failed");

      return FilterResult.STOP;
    }
    super.doHandle(resource, responseWrapper);
    FilterResult.CONTINUE;
  }

  def validate(entity: T):List[_] = {
    List[Nothing]()
  }

}