package io.skysail.restlet.resources

import io.skysail.api.responses.SkysailResponse
import io.skysail.restlet.SkysailServerResource
import org.restlet.data.Method
import io.skysail.core.model.LinkRelation
import io.skysail.restlet.ResourceContextId
import org.restlet.resource.Get
import org.restlet.representation.Variant
import io.skysail.restlet.responses.EntityResponse
import io.skysail.restlet.ScalaListRequestHandler
import io.skysail.restlet.ScalaRequestHandler

class EntityServerResource[T:Manifest] extends SkysailServerResource {
  
  addToContext(ResourceContextId.LINK_TITLE, "show");

  def eraseEntity() = new SkysailResponse[T]()

  override def getVerbs(): Set[Method] = Set(Method.GET)

  override def getLinkRelation() = LinkRelation.ITEM
  
  
      // input: html|json|..., output: html|json|...
    /**
     * @return the response
     */
    @Get("html|json|eventstream|treeform|txt|csv|yaml|mailto|data")
    def getResource( variant: Variant):EntityResponse[T]  = {
        val timerMetric = getMetricsCollector().timerFor(this.getClass(), "getResource");
//        if (variant != null) {
//            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
//        }
        val entity = getEntity3();
        timerMetric.stop();
        new EntityResponse[T](getResponse(), entity);
    }

//    @Get("htmlform")
//    def SkysailResponse<T> getDeleteForm() {
//        return new FormResponse<>(getResponse(), getEntity("dummy"), ".", "/");
//    }

//    @Delete("x-www-form-urlencoded:html|html|json")
//    public EntityServerResponse<T> deleteEntity(Variant variant) {
//        TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "deleteEntity");
//        if (variant != null) {
//            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
//        }
//        RequestHandler<T> requestHandler = new RequestHandler<>(getApplication());
//        AbstractResourceFilter<EntityServerResource<T>, T> handler = requestHandler.createForEntity(Method.DELETE);
//        T entity = handler.handle(this, getResponse()).getEntity();
//        timerMetric.stop();
//        return new EntityServerResponse<>(getResponse(), entity);
//    }

  
    final def getEntity3():T = {
          val appModel = getSkysailApplication().getApplicationModel2()
      val requestHandler = new ScalaRequestHandler[T](null.asInstanceOf[T])//, appModel)
      val responseWrapper = requestHandler.createForGet(Method.GET).handle(this, getResponse())
      return responseWrapper.getEntity()

    //        val requestHandler = new RequestHandler<>(getApplication());
    //        AbstractResourceFilter<EntityServerResource<T>, T> chain = requestHandler.createForEntity(Method.GET);
    //        ResponseWrapper<T> wrapper = chain.handle(this, getResponse());
    //        return wrapper.getEntity();
  }

  def getEntity(): Any = {
    ???
  }
}