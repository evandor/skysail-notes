package io.skysail.app.demo.domain

import javax.validation.constraints._
import com.fasterxml.jackson.annotation._
import java.util.Date

import scala.annotation.meta.field
import scala.beans.BeanProperty

import io.skysail.core.html._
import io.skysail.core.domain.ScalaEntity
import io.skysail.restlet.forms._

@BeanProperty
case class Todo (
    var id: Option[String] = None,
    @BeanProperty @(Field @field) /*@(NotNull @field) @Size(min=1)*/ var title: String = ""
  ) extends ScalaEntity[String] {

}