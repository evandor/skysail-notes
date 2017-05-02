package io.skysail.restlet

import org.restlet.Response
import io.skysail.restlet.responses.ListResponse
import org.slf4j.LoggerFactory
import org.restlet.Request
import io.skysail.restlet.resources._

abstract class ScalaAbstractResourceFilter[T:Manifest] extends ScalaResourceFilter[T] {

  override val log = LoggerFactory.getLogger(this.getClass())

  final def handle(resource: SkysailServerResource[_], response: Response): ScalaResponseWrapper[T] = {
    val responseWrapper = new ScalaResponseWrapper[T](response)
    handleMe(resource, responseWrapper)
    responseWrapper
  }

  protected def getDataFromRequest(request: Request, resource: SkysailServerResource[_]): Any = {
    val entityAsObject = request.getAttributes().get(SkysailServerResource.SKYSAIL_SERVER_RESTLET_ENTITY).asInstanceOf[T]
    if (entityAsObject != null) {
      if (resource.isInstanceOf[EntityServerResource[T]]) {
      } else if (resource.isInstanceOf[PostEntityServerResource[T]]) {
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


}