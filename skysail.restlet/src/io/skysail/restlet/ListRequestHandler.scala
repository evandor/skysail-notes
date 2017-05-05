package io.skysail.restlet

import org.restlet.data.Method
import org.restlet.representation.Variant
import io.skysail.restlet.filter._
import io.skysail.core.model.ApplicationModel

class ScalaListRequestHandler[T: Manifest](variant: Variant, appModel: ApplicationModel) {

  def createForList(method: Method): ScalaAbstractListResourceFilter[T] = {
    if (method.equals(Method.GET)) {
      return chainForListGet();
    } else if (method.equals(Method.POST)) {
      return null; //chainForListPost();
    }
    return null;
    //  throw new RuntimeException("Method " + method + " is not yet supported");
  }

  private def chainForListGet(): ScalaAbstractListResourceFilter[T] = {
    new ScalaExceptionCatchingListFilter[T]()
      // .calling(new ExtractStandardQueryParametersResourceFilter<>())
      .calling(new ScalaDataExtractingListFilter[T]())
      .calling(new AddLinkheadersListFilter[T](appModel))
      //      .calling(new SetExecutionTimeInListResponseFilter())
      .calling(new RedirectListFilter[T]())
      .asInstanceOf[ScalaAbstractListResourceFilter[T]]
  }
}