package io.skysail.app.wyt.domain

import javax.validation.constraints._
import com.fasterxml.jackson.annotation._
import java.util.Date

import scala.annotation.meta.field
import scala.beans.BeanProperty

import io.skysail.core.html._
import io.skysail.restlet.forms._
import io.skysail.core.domain.ScalaEntity

@JsonIgnoreProperties(ignoreUnknown = true)
case class Pact(
    var id: Option[String] = None,
    @BeanProperty @(Field @field) var title: String = "",
    @BeanProperty var turn: Turn = new Turn("test")) extends ScalaEntity[String] {

  // title, selectionStrategy & confirmationS

}