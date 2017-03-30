package io.skysail.restlet.resources

import org.restlet.representation.Variant
import org.restlet.resource.Get
import org.restlet.data.Method
import io.skysail.api.responses.SkysailResponse
import io.skysail.restlet.ListRequestHandler
import io.skysail.restlet.ScalaSkysailServerResource

abstract class ListServerResource2[T](cls: Class[_]) extends ScalaSkysailServerResource {

  @Get("html|json|yaml|xml|csv|timeline|carbon|standalone|data")
  def getEntities(variant: Variant): SkysailResponse[List[T]] = {
    val timerMetric = getMetricsCollector().timerFor(this.getClass(), "getEntities");
    val entitiesList = listEntities();
    timerMetric.stop();
    new SkysailResponse[List[T]](getResponse(), entitiesList);
  }

  private final def listEntities(): List[T] = {
    val requestHandler = new ListRequestHandler();
    val responseWrapper = requestHandler.createForList(Method.GET).handleList(this, getResponse())
    return List()//responseWrapper.getEntity()
  }

}