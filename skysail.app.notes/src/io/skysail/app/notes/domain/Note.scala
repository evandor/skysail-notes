package io.skysail.app.notes.domain

import io.skysail.domain.Entity
import io.skysail.domain.html.Field
import io.skysail.domain.html.InputType
import javax.validation.constraints.Size

class Note() extends Entity {
  var id:String = null
  def getId(): String = id
  def setId(c: String) = id = c

  @Field(inputType = InputType.TEXTAREA)
  @Size(min=1)
  var content: String = ""
  def setContent(c: String) = content = c
  def getContent() = content
}