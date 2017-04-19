package io.skysail.restlet.utils

import io.skysail.restlet.RouteBuilder
import io.skysail.domain.Entity

class PathSubstitutions(requestAttributes: java.util.Map[String, Object], routeBuilders: List[RouteBuilder]) {

  var idVariable = "id";
  def getIdVariable = idVariable

  def result = scala.collection.mutable.Map[String, String]()
  var pathVariables = List[String]()

  //addStringValuesFromRequestAttributes(requestAttributes);
  determinePathVariables(requestAttributes, routeBuilders);

  def getFor(theObject: Any): scala.collection.mutable.Map[String, String] = {
    if (!(theObject.isInstanceOf[Entity])) {
      return result;
    }
    val identifiable = theObject.asInstanceOf[Entity]
    if (identifiable.getId() != null) {
      if (pathVariables.size == 1) {
        idVariable = pathVariables(0)
      }
      result += idVariable -> identifiable.getId().replace("#", "")
    }
    return result
  }

  /*def addStringValuesFromRequestAttributes( requestAttributes: Map[String, Object]) {
        requestAttributes.entrySet().stream().forEach(entry -> {
            if (entry.getValue().isInstanceOf[String]) {
                result.put(entry.getKey(), Reference.decode((String)entry.getValue()));
            }
        });
    }*/

  private def determinePathVariables(requestAttributes: java.util.Map[String, Object], routeBuilders: List[RouteBuilder]) {
    pathVariables = routeBuilders
      .map(builder => builder.pathVariables)
      .flatMap(pv => pv)
      .toList

    /*pathVariables.removeIf(pathVariable -> {
            if (requestAttributes.get(pathVariable) != null) {
                result.put(pathVariable, (String)requestAttributes.get(pathVariable));
                return true;
            }
            return false;
        });*/
  }

}