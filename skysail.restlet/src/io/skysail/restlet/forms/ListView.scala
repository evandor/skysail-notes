package io.skysail.restlet.forms

import scala.annotation.meta._
import io.skysail.restlet.SkysailServerResource

@getter @setter @beanGetter @beanSetter
class ListView(
    val link: Class[SkysailServerResource[_]] = classOf[SkysailServerResource[_]],
    val truncate:Int = -1,
    val hide:Boolean = false,
    val prefix: String = "",
    val format: String = ""
  ) 
  extends scala.annotation.StaticAnnotation {
//   Class<? extends SkysailServerResource> link() default SkysailServerResource.class; //default DEFAULT.class;
//
//    int truncate() default -1;
//
//    boolean hide() default false;
//
//    String colorize() default "";
//
//    String prefix() default "";

 //   def format(): String = ""
}