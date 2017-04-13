package io.skysail.restlet

import java.util.regex.Pattern
import io.skysail.core.ApiVersion
import com.google.common.base.Predicate
import org.restlet.Restlet
import org.restlet.resource.ServerResource

class ScalaRouteBuilder(val pathTemplate: String, val targetClass: Class[_ <: ServerResource]) {

  var restlet: Restlet = null
  
  val pathVariables = extractPathVariables(pathTemplate);
  
  var rolesForAuthorization: Predicate[Array[String]] = null

  var needsAuthentication = true

  val pathVariablesPattern = Pattern.compile("\\{([^\\}])*\\}");

  def this(pathTemplate: String, restlet: Restlet) = {
    this(pathTemplate,null:Class[_ <: ServerResource])
    this.restlet = restlet
  }
  
  private def extractPathVariables(input: String): List[String] = {
    val result = scala.collection.mutable.ListBuffer[String]()
    val m = pathVariablesPattern.matcher(input);
    while (m.find()) {
      result += m.group(0).replace("}", "").replace("{", "")
    }
    result.toList;
  }

  def getPathTemplate(apiVersion: ApiVersion) = if (apiVersion == null) pathTemplate else apiVersion.getVersionPath() + pathTemplate
}