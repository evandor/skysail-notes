package io.skysail.restlet.utils

import java.util.Locale
import io.skysail.restlet.ScalaSkysailServerResource
import org.slf4j.LoggerFactory

object ScalaResourceUtils {

  var log = LoggerFactory.getLogger(this.getClass())

  def determineLocale(resource: ScalaSkysailServerResource): Locale = {
    if (resource.getRequest() == null || resource.getRequest().getClientInfo() == null) {
      return Locale.getDefault();
    }
    val acceptedLanguages = resource.getRequest().getClientInfo().getAcceptedLanguages();
    val localeToUse = Locale.getDefault();
    if (!acceptedLanguages.isEmpty()) {
      val languageSplit = acceptedLanguages.get(0).getMetadata().getName().split("-")
      if (languageSplit.length == 1) {
        return new Locale(languageSplit(0))
      } else if (languageSplit.length == 2) {
        return new Locale(languageSplit(0), languageSplit(1))
      }
    }
    return localeToUse;
  }

  def createSkysailServerResources(entityResourceClasses: List[ScalaSkysailServerResource], resource: ScalaSkysailServerResource) = {
    val result = scala.collection.mutable.ListBuffer[ScalaSkysailServerResource]()
    for (cls <- entityResourceClasses) {
      var newInstance: ScalaSkysailServerResource = null
      try {
        newInstance = cls.getClass.newInstance()
        newInstance.init(resource.getContext(), resource.getRequest(), resource.getResponse());
        newInstance.release();
        result += newInstance
      } catch {
        case e: Throwable => log.error(e.getMessage(), e);
      }
    }
    result

  }

}