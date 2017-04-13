package io.skysail.restlet.services

import org.osgi.annotation.versioning.ProviderType;
import java.util.ResourceBundle

@ProviderType
trait ResourceBundleProvider {
  def getResourceBundles(): List[ResourceBundle]
}