package io.skysail.core.model

import scala.annotation.meta.field
import scala.beans.BeanProperty
import io.skysail.domain.ddd.ScalaEntity
import io.skysail.domain.html.Field

@BeanProperty
case class TestEntity (
    var id: Option[String],
    @BeanProperty @(Field @field) /*@(NotNull @field) @Size(min=1)*/ var content: String
  ) extends ScalaEntity[String] {
}