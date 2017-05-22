package io.skysail.app.wyt.domain

import javax.validation.constraints._
import com.fasterxml.jackson.annotation._
import java.util.Date

import scala.annotation.meta.field
import scala.beans.BeanProperty

import io.skysail.core.html._
import io.skysail.domain.ddd.ScalaEntity
import io.skysail.restlet.forms._

case class Pact (
    var id: Option[String] = None,
    @BeanProperty @(Field @field) /*@(NotNull @field) @Size(min=1)*/ var title: String = "",
    @BeanProperty var turn: Turn = null //new Turn(None, "test")
  ) extends ScalaEntity[String] {

  // title, selectionStrategy & confirmationS
  
  
}