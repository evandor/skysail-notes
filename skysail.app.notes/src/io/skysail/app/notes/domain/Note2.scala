package io.skysail.app.notes.domain

import scala.beans.BeanProperty
import io.skysail.core.html.Field
import scala.annotation.meta.field

case class Note2( @(Field @field) @BeanProperty var content: String)