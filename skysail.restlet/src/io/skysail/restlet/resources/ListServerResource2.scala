package io.skysail.restlet.resources

import org.restlet.representation.Variant
import org.restlet.resource.Get
import org.restlet.data.Method
import io.skysail.api.responses.SkysailResponse
import io.skysail.restlet.ListRequestHandler
import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.restlet.responses.ScalaSkysailResponse
import io.skysail.restlet.responses.ListResponse

abstract class ListServerResource2[T](cls: Class[_]) extends ScalaSkysailServerResource {

  @Get("html|json|yaml|xml|csv|timeline|carbon|standalone|data")
  def getEntities(variant: Variant): ListResponse[T] = {
    val timerMetric = getMetricsCollector().timerFor(this.getClass(), "getEntities");
    val entitiesList = listEntities();
    timerMetric.stop();
    new ListResponse[T](getResponse(), entitiesList);
  }

  private final def listEntities(): List[T] = {
    val requestHandler = new ListRequestHandler();
    val responseWrapper = requestHandler.createForList(Method.GET).handleList(this, getResponse())
    return responseWrapper.getEntity()
  }

}