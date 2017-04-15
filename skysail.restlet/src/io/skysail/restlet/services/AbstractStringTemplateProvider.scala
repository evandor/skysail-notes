package io.skysail.restlet.services

import org.osgi.framework.Bundle
import java.net.URL
import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import java.io.BufferedReader
import scala.io.Source

object AbstractStringTemplateProvider {
  val TEMPLATES_DIR = "/templates";
}

abstract class AbstractStringTemplateProvider extends StringTemplateProvider {

  val log = LoggerFactory.getLogger(this.getClass)

  protected var templates = Map[String, String]()

  protected var bundle: Bundle = null

  override def getTemplates(): Map[String, String] = {
    if (templates.size > 0) {
      return templates;
    }
    val resources = bundle.findEntries(AbstractStringTemplateProvider.TEMPLATES_DIR, "*.st", true);
    if (resources == null) {
      templates = Map[String, String]()
      return templates;
    }
    while (resources.hasMoreElements()) {
      val url = resources.nextElement();
      addToTemplates(url);
    }
    return templates;
  }

  def addToTemplates(url: URL): Unit = {
//    println(url)
//    val source = Source.fromURI(url.toURI())
    val source = Source.fromInputStream(url.openStream())
//    println(source)
    templates += getIdentifier(url) -> source.mkString
//    try {
//      val inputStream = url.openStream();
//      val in = new BufferedReader(new InputStreamReader(inputStream));
//      val content = new StringBuilder();
//      var inputLine: String = null
//      var line = 0;
//      while ((inputLine = in.readLine()) != null) {
//        content.append(inputLine).append("\n");
//        line+=1
//      }
//      in.close();
//      templates += getIdentifier(url) -> content.toString()
//    } catch {
//      case e: Throwable => log.error(e.getMessage(), e)
//    }
  }
  
  def getIdentifier(url: URL) = url.toString().split(AbstractStringTemplateProvider.TEMPLATES_DIR)(1)

}