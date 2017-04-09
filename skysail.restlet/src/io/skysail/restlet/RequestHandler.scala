package io.skysail.restlet

import io.skysail.restlet.filter._
import org.restlet.representation.Variant

class ScalaRequestHandler[T](entity: T, variant: Variant) {

      def createForPost(): ScalaAbstractResourceFilter[T] = {

           new ExceptionCatchingFilter[T]()
                .calling(new ExtractStandardQueryParametersResourceFilter[T]())
                .calling(new CheckInvalidInputFilter[T](entity))
                .calling(new FormDataExtractingFilter[T](entity))
                .calling(new CheckBusinessViolationsFilter[T](entity))
                .calling(new PersistEntityFilter[T](entity))
                .calling(new EntityWasAddedFilter[T](entity))
                .calling(new AddLinkheadersFilter[T]())
                .calling(new PostRedirectGetFilter[T](variant));
      }
}