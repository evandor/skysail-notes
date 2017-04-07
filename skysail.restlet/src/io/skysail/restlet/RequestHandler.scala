package io.skysail.restlet

import io.skysail.restlet.filter._

class ScalaRequestHandler[T] {

      def createForPost(): ScalaAbstractResourceFilter[T] = { //AbstractResourceFilter<PostEntityServerResource<T>, T> = {

           new ExceptionCatchingFilter[T]()
                .calling(new ExtractStandardQueryParametersResourceFilter[T]())
                .calling(new CheckInvalidInputFilter[T]())
                .calling(new FormDataExtractingFilter[T]())
//                .calling(new CheckBusinessViolationsFilter[](application))
//                .calling(new PersistEntityFilter[](application))
//                .calling(new EntityWasAddedFilter[](application))
//                .calling(new AddLinkheadersFilter[]())
//                .calling(new PostRedirectGetFilter[]());
      }
}