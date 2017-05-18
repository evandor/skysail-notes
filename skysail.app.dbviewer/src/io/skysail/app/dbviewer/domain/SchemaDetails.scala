package io.skysail.app.dbviewer.domain

import javax.validation.constraints._
import com.fasterxml.jackson.annotation._
import java.util.Date

import scala.annotation.meta.field
import scala.beans.BeanProperty

import io.skysail.core.html._
import io.skysail.domain.ddd.ScalaEntity
import io.skysail.restlet.forms._

@BeanProperty
case class SchemaDetails (
    var id: Option[String] = None,
    @(Field @field)(description = "Schema name") 
    var name: String = ""
) extends ScalaEntity[String] {
  
  def getName = if (id.isDefined) id else "undefined"

}