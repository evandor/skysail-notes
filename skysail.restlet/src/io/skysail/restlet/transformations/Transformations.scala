package io.skysail.restlet.transformations

import org.restlet.data.Form
import org.json4s.JsonAST.JValue
import org.json4s._
import org.json4s.jackson.JsonMethods._
import scala.collection.JavaConversions._

object Transformations {
  
  def jsonFrom[T](form: Form): JValue = {
    val sb = form.getNames()
      .filter(_ != "submit")
      .map(key => "\"" + key.split("\\|")(1) + "\":\"" + form.getFirstValue(key) + "\"")
      .mkString(",")
    parse("{" + sb + "}")
  }
  
  def jsonFrom[T:Manifest](map: Map[String,Object]): JValue = {
    val sb = map.keys
      .filter(_ != "@class")
      .filter(_ != "@rid")
      .map(key => "\"" + normalize(key) + "\":\"" + map.get(key).get + "\"")
      .mkString(",")
    println(sb)
    parse("{" + sb + "}")
  }

  def normalize(key: String) = 
    key match {
      case "@rid" => "id"
      case k => k
    }
  

}