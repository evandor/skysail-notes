package io.skysail.app.wyt.domain

import javax.validation.constraints._
import com.fasterxml.jackson.annotation._
import java.util.Date

import scala.annotation.meta.field
import scala.beans.BeanProperty

import io.skysail.core.html._
import io.skysail.core.domain.ScalaEntity
import io.skysail.core.forms._

case class Turn (
    @BeanProperty @(Field @field) var nextTurn: String = "initial",
    @BeanProperty @(Field @field) var lastConfirmation: Option[String] = Some("")
  ) 