package io.skysail.app.checklists

import io.skysail.core.html.Field
import scala.beans.BeanProperty
import io.skysail.core.domain.ScalaEntity
import scala.annotation.meta.field

case class Checklist(
  var id: Option[String] = None,
  @BeanProperty @(Field @field) var title: String,
  @BeanProperty @(Field @field) var elements: List[String] = List()
) extends ScalaEntity[String]
