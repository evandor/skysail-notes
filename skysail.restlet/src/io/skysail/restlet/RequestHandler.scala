package io.skysail.restlet

import org.restlet.data.Method
import io.skysail.server.restlet.filter.DataExtractingListFilter
import io.skysail.server.restlet.filter.AddLinkheadersListFilter
import io.skysail.server.restlet.filter.SetExecutionTimeInListResponseFilter
import io.skysail.server.restlet.filter.RedirectListFilter
import io.skysail.server.restlet.filter.AbstractListResourceFilter
import io.skysail.restlet.filter.ExceptionCatchingListFilter

class ScalaRequestHandler[T] {

      def createForPost(): ScalaAbstractResourceFilter[T] = { //AbstractResourceFilter<PostEntityServerResource<T>, T> = {

           new ExceptionCatchingFilter[PostEntityServerResource2]()
//                .calling(new ExtractStandardQueryParametersResourceFilter[]())
//                .calling(new CheckInvalidInputFilter[](application))
//                .calling(new FormDataExtractingFilter[]())
//                .calling(new CheckBusinessViolationsFilter[](application))
//                .calling(new PersistEntityFilter[](application))
//                .calling(new EntityWasAddedFilter[](application))
//                .calling(new AddLinkheadersFilter[]())
//                .calling(new PostRedirectGetFilter[]());
      }
}