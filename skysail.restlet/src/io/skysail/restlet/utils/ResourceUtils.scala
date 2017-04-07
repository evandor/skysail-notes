package io.skysail.restlet.utils

import java.util.Locale
import io.skysail.restlet.ScalaSkysailServerResource

object ScalaResourceUtils {
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

}