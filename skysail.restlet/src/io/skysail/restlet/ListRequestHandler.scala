package io.skysail.restlet

import org.restlet.data.Method
import io.skysail.server.restlet.filter.DataExtractingListFilter
import io.skysail.server.restlet.filter.AddLinkheadersListFilter
import io.skysail.server.restlet.filter.SetExecutionTimeInListResponseFilter
import io.skysail.server.restlet.filter.RedirectListFilter
import io.skysail.server.restlet.filter.AbstractListResourceFilter
import io.skysail.restlet.filter.ExceptionCatchingListFilter

class ListRequestHandler {
  def createForList(method: Method): ScalaAbstractListResourceFilter = {
    if (method.equals(Method.GET)) {
      return chainForListGet();
    } else if (method.equals(Method.POST)) {
      return null; //chainForListPost();
    }
    return null;
    //  throw new RuntimeException("Method " + method + " is not yet supported");
  }

  private def chainForListGet(): ScalaAbstractListResourceFilter = {
    new ExceptionCatchingListFilter()
//      // .calling(new ExtractStandardQueryParametersResourceFilter<>())
//      .calling(new DataExtractingListFilter())
//      .calling(new AddLinkheadersListFilter())
//      .calling(new SetExecutionTimeInListResponseFilter())
//      .calling(new RedirectListFilter());
  }
}