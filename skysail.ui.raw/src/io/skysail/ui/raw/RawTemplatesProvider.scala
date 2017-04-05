package io.skysail.ui.raw

import io.skysail.server.services.AbstractStringTemplateProvider
import io.skysail.server.services.StringTemplateProvider
import org.osgi.service.component.annotations._
import org.osgi.service.component.ComponentContext

object RawTemplatesProvider {
  def NAMESPACE = "raw";
  def SHORTNAME = "raw";
}

@Component(immediate = true, service = Array(classOf[StringTemplateProvider]))
class RawTemplatesProvider extends AbstractStringTemplateProvider with StringTemplateProvider {

  def getNamespace(): String = RawTemplatesProvider.NAMESPACE
  def getShortName(): String = RawTemplatesProvider.SHORTNAME

  @Activate def activate(componentContext: ComponentContext) {
    bundle = componentContext.getBundleContext().getBundle()
  }

  @Deactivate
  def deactivate(componentContext: ComponentContext) {
    this.bundle = null;
  }
}