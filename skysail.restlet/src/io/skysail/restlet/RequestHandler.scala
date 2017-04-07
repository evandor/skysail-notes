package io.skysail.restlet

import io.skysail.restlet.filter._

class ScalaRequestHandler[T](entity: T) {

      def createForPost(): ScalaAbstractResourceFilter[T] = { //AbstractResourceFilter<PostEntityServerResource<T>, T> = {

           new ExceptionCatchingFilter[T]()
                .calling(new ExtractStandardQueryParametersResourceFilter[T]())
                .calling(new CheckInvalidInputFilter[T](entity))
                .calling(new FormDataExtractingFilter[T](entity))
                .calling(new CheckBusinessViolationsFilter[T](entity))
                .calling(new PersistEntityFilter[T]())
                .calling(new EntityWasAddedFilter[T]())
                .calling(new AddLinkheadersFilter[T]())
                .calling(new PostRedirectGetFilter[T]());
      }
}