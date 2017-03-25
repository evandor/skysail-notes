package io.skysail.app.notes.domain

import io.skysail.domain.Entity
import io.skysail.domain.html.Field
import io.skysail.domain.html.InputType
import javax.validation.constraints.Size
import io.skysail.server.forms.PostView
import io.skysail.server.forms.PutView
import com.fasterxml.jackson.annotation.JsonFormat
import java.util.Date
import io.skysail.server.forms.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Note() extends Entity {
  
  var id:String = null
  def getId(): String = id
  def setId(c: String) = id = c

  @Field(inputType = InputType.TEXTAREA)
  @Size(min=1)
  var content: String = ""
  def setContent(c: String) = content = c
  def getContent() = content
  
  
  @Field(inputType = InputType.DATE)
  @PostView(visibility = Visibility.HIDE)
  @PutView(visibility = Visibility.HIDE)
  @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  var created: Date = new Date()
  def setCreated(c: Date) = created = c
  def getCreated() = created

  @Field(inputType = InputType.DATE)
  @PostView(visibility = Visibility.HIDE)
  @PutView(visibility = Visibility.HIDE)
  @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  var modified: Date = new Date()
  def setModified(c: Date) = modified = c
  def getModified() = modified

}