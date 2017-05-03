package io.skysail.core.model

import io.skysail.restlet.SkysailServerResource
import scala.collection.JavaConverters._
import java.lang.reflect.Field
import io.skysail.restlet.utils.ScalaReflectionUtils

case class EntityModel(entityClass: Class[_]) {
  
  require(entityClass != null, "The provided entity class must not be null")
  
  def name() = entityClass.getName

  val fields = deriveFields()
  
  def fieldFor(id: String): Option[FieldModel] = fields.get(id) 

  private def deriveFields() = {
    ScalaReflectionUtils.getInheritedFields(entityClass)
      .filter { filterFormFields(_) }
      .map { f => new FieldModel(f) }
      .map(m => m.name -> m)
      .toMap
  }

  def toHtml() = s"""${this.getClass.getSimpleName}("$name")<br>
        <br><u>Fields</u>: <ul>${printHtmlMap(fields)}</ul>"""
  
  override def toString() = s"""${this.getClass.getSimpleName}("$name")
        Fields: ${printMap(fields)}"""
  
  private def filterFormFields(f: Field): Boolean = f.getAnnotation(classOf[io.skysail.core.html.Field]) != null

  private def printHtmlMap(map: Map[_, _]) = map.map(v => s"""
          <li>"${v._1}" -> ${v._2.toString()}</li>""").mkString("")

  private def printMap(map: Map[_, _]) = map.map(v => s"""
          "${v._1}" -> ${v._2.toString()}""").mkString("")

}