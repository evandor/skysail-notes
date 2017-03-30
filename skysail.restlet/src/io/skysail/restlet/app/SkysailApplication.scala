package io.skysail.restlet.app

import io.skysail.core.app.ApplicationProvider
import io.skysail.server.services.ResourceBundleProvider
import io.skysail.domain.Entity
import io.skysail.core.app.ApiVersion
import java.util.ResourceBundle
import java.util.Collections

abstract class ScalaSkysailApplication(
    name: String,
    apiVersion: ApiVersion,
    entityClasses: List[Class[_ <: Entity]]) 
  extends org.restlet.Application
    with ScalaApplicationProvider with ResourceBundleProvider {

  def this(name: String, apiVersion: ApiVersion) {
    this(name, apiVersion, List())
  }

  def this(name: String) {
    this(name, new ApiVersion(1))
  }

  def getResourceBundles(): java.util.List[ResourceBundle] = {
    Collections.emptyList()
  }
  
   def getTemplatePaths[T](x$1: Class[T]): java.util.List[String] = {
    Collections.emptyList()
  }

}