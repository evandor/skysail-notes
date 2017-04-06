package io.skysail.restlet.resources.helper

import org.restlet.data.Form
import org.json4s.JsonAST.JValue
import org.json4s._
import org.json4s.jackson.JsonMethods._
import scala.collection.JavaConversions._

object FormDeserializer {
  def createFrom[T](form: Form, cls: Class[_]): JValue = {
    implicit val formats = DefaultFormats
    println(form)
    //      val elements = MutableList[JField]()
    //      form.getNames()
    //        .filter(_ != "submit")
    //        .foreach(key => elements += JField(key, JString(form.getFirstValue(key))))
    //      //val jValue: org.json4s.JsonAST.JValue = JObject(elements.toList)
    //      JObject(elements.toList)
    //val json = parse("""{"name":"joe"}""")
    //json.extract[Note]
    //jValue.extract[Note]
    val sb = form.getNames()
      .filter(_ != "submit")
      .map(key => "\"" + key.split("\\|")(1) + "\":\"" + form.getFirstValue(key) + "\"")
      .mkString(",")
    println(sb)
    parse("{" + sb + "}")
    //json.extract[T]
    //jValue.extract[T]
  }
}