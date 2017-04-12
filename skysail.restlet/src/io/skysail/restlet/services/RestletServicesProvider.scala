package io.skysail.restlet.services

import org.osgi.annotation.versioning.ProviderType;
import org.restlet.service.ConverterService;

@ProviderType
trait ScalaRestletServicesProvider {
  def getConverterSerivce(): ConverterService
}
