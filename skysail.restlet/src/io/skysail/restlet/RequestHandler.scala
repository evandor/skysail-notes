package io.skysail.restlet

import io.skysail.restlet.filter.ExceptionCatchingFilter


class ScalaRequestHandler[T] {

      def createForPost(): ScalaAbstractResourceFilter[T] = { //AbstractResourceFilter<PostEntityServerResource<T>, T> = {

           new ExceptionCatchingFilter[T]()
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