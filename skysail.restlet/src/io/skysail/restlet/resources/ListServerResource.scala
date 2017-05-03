package io.skysail.restlet.resources

import org.restlet.representation.Variant
import org.restlet.resource.Get
import org.restlet.data.Method
import io.skysail.api.responses.SkysailResponse
import io.skysail.restlet.SkysailServerResource
import io.skysail.restlet.responses.ScalaSkysailResponse
import io.skysail.restlet.responses.ListResponse
import io.skysail.restlet.ScalaListRequestHandler
import io.skysail.core.model.ENTITY_RESOURCE_FOR_LIST_RESOURCE
import io.skysail.core.model.ResourceAssociationType

abstract class ListServerResource[T: Manifest](associatedEntityResource: Class[_ <: EntityServerResource[_]] = null) extends SkysailServerResource {
  
  addAssociatedResourceClasses(List((ENTITY_RESOURCE_FOR_LIST_RESOURCE, associatedEntityResource)))

  override def getVerbs(): Set[Method] = Set(Method.GET)

  @Get("html|json|yaml|xml|csv|timeline|carbon|standalone|data")
  def getEntities(variant: Variant): ListResponse[T] = {
    val timerMetric = getMetricsCollector().timerFor(this.getClass(), "getEntities");
    val entitiesList = listEntities(variant);
    timerMetric.stop();
    new ListResponse[T](getResponse(), entitiesList);
  }

  private final def listEntities(variant: Variant): List[T] = {
    val appModel = getSkysailApplication().getApplicationModel2()
    val requestHandler = new ScalaListRequestHandler[T](variant, appModel)
    val responseWrapper = requestHandler.createForList(Method.GET).handle(this, getResponse())
    return responseWrapper.getEntity()
  }

}