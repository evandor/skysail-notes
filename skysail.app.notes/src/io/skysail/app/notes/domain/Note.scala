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

//@JsonIgnoreProperties(ignoreUnknown = true)
case class Note(
    id:String,
    content:String
) extends Entity {
    def this() = this("","")
    def getId():String = id
    
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