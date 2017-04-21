package io.skysail.restlet.resources

import io.skysail.restlet.SkysailServerResource
import org.restlet.resource.Get
import org.restlet.representation.Variant
import io.skysail.restlet.responses.EntityResponse
import io.skysail.restlet.utils.LinkUtils

abstract class RedirectResource[T] extends SkysailServerResource {

  protected def redirectToResource(): SkysailServerResource

  @Get def redirectToEntity(variant: Variant): EntityResponse[T] = {
    val timerMetric = getMetricsCollector().timerFor(this.getClass(), "redirectToEntity")
    //        if (variant != null) {
    //            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
    //        }
    //
    if (redirectToResource() == null) {
      getResponse().redirectSeeOther(redirectTo());
      timerMetric.stop();
      return null;
    }

   // val link = LinkUtils.fromResource(getApplication(), redirectToResource());
    // getPathSubstitutions().accept(link);
  //  getResponse().redirectSeeOther(link.getUri());
    timerMetric.stop()
    EntityResponse[T](getResponse());
  }
}