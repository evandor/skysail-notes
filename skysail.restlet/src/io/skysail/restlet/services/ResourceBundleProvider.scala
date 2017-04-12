package io.skysail.restlet.services

import org.osgi.annotation.versioning.ProviderType;
import java.util.ResourceBundle

@ProviderType
trait ScalaResourceBundleProvider {
  def getResourceBundles(): List[ResourceBundle]
}