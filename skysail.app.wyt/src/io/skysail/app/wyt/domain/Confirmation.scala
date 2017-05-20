package io.skysail.app.wyt.domain

import javax.validation.constraints._
import com.fasterxml.jackson.annotation._
import java.util.Date

import scala.annotation.meta.field
import scala.beans.BeanProperty

import io.skysail.core.html._
import io.skysail.domain.ddd.ScalaEntity
import io.skysail.restlet.forms._

case class Confirmation(var id: Option[String] = None) extends ScalaEntity[String] 