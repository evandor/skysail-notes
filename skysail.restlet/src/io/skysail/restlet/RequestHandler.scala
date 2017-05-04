package io.skysail.restlet

import io.skysail.restlet.filter._
import org.restlet.representation.Variant

class ScalaRequestHandler[T: Manifest](entity: T, variant: Variant) {

  def createForPost(): ScalaAbstractResourceFilter[T] = {

    new ExceptionCatchingFilter[T]()
      .calling(new ExtractStandardQueryParametersResourceFilter[T]())
      .calling(new CheckInvalidInputFilter[T](entity))
      .calling(new FormDataExtractingFilter[T](entity))
      .calling(new CheckBusinessViolationsFilter[T](entity))
      .calling(new PersistEntityFilter[T](entity))
      .calling(new EntityWasAddedFilter[T](entity))
      .calling(new AddLinkheadersFilter[T]())
      .calling(new PostRedirectGetFilter[T](variant))
      .asInstanceOf[ScalaAbstractResourceFilter[T]]
  }

  def createForGet(): ScalaAbstractResourceFilter[T] = {
    new ExceptionCatchingFilter[T]()
      .calling(new ExtractStandardQueryParametersResourceFilter[T]())
      .calling(new DataExtractingFilter[T]())
      //.calling(new AddReferrerCookieFilter[T]())
      .calling(new AddLinkheadersFilter[T]())
      .asInstanceOf[ScalaAbstractResourceFilter[T]]
    
    
//     return new ExceptionCatchingFilter<EntityServerResource<T>, T>(application)
//                .calling(new ExtractStandardQueryParametersResourceFilter<>())
//                .calling(new DataExtractingFilter<>())
//                .calling(new AddReferrerCookieFilter<>())
//                .calling(new AddLinkheadersFilter<>());
  }

}