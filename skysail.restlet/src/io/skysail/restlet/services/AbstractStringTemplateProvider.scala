package io.skysail.restlet.services

class AbstractStringTemplateProvider extends StringTemplateProvider {
  
  var templates: Map[String,String]

  override def getTemplates(): Map[String, String] = {
    if (templates.size > 0) {
      return templates;
    }
    Enumeration < URL > resources = bundle.findEntries(TEMPLATES_DIR, "*.st", true);
    if (resources == null) {
      templates = Collections.emptyMap();
      return templates;
    }
    while (resources.hasMoreElements()) {
      URL url = resources.nextElement();
      addToTemplates(url);
    }
    return templates;
  }

}