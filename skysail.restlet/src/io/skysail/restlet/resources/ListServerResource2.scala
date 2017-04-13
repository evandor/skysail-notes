package io.skysail.restlet.resources

import org.restlet.representation.Variant
import org.restlet.resource.Get
import org.restlet.data.Method
import io.skysail.api.responses.SkysailResponse
import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.restlet.responses.ScalaSkysailResponse
import io.skysail.restlet.responses.ListResponse
import io.skysail.restlet.ScalaListRequestHandler

abstract class ListServerResource2[T:Manifest](cls: Class[_] = null) extends ScalaSkysailServerResource {

  @Get("html|json|yaml|xml|csv|timeline|carbon|standalone|data")
  def getEntities(variant: Variant): ListResponse[T] = {
    val timerMetric = getMetricsCollector().timerFor(this.getClass(), "getEntities");
    val entitiesList = listEntities(variant);
    timerMetric.stop();
    new ListResponse[T](getResponse(), entitiesList);
  }

  private final def listEntities(variant: Variant): List[T] = {
    val requestHandler = new ScalaListRequestHandler[T](variant);
    val responseWrapper = requestHandler.createForList(Method.GET).handle(this, getResponse())
    return responseWrapper.getEntity()
  }

}