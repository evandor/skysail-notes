package io.skysail.app.notes.domain

import io.skysail.domain.Entity
import io.skysail.domain.html._
import javax.validation.constraints.Size
import io.skysail.server.forms.PostView
import io.skysail.server.forms.PutView
import com.fasterxml.jackson.annotation.JsonFormat
import java.util.Date
import io.skysail.server.forms.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import scala.annotation.meta.field
import scala.beans.BeanProperty

import javax.validation.constraints._

//@JsonIgnoreProperties(ignoreUnknown = true)
@BeanProperty
case class Note(
    var id: Option[String],
    @(Field @field) /*@(NotNull @field) @Size(min=1)*/ var content: String) {

//  def this() = this("", "")
//  def getId(): String = id
  
  //def getContent() = content

  @Field(inputType = InputType.DATE)
  @PostView(visibility = Visibility.HIDE)
  @PutView(visibility = Visibility.HIDE)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  var created: Date = new Date()
  def setCreated(c: Date) = created = c
  def getCreated() = created

  @Field(inputType = InputType.DATE)
  @PostView(visibility = Visibility.HIDE)
  @PutView(visibility = Visibility.HIDE)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  var modified: Date = new Date()
  def setModified(c: Date) = modified = c
  def getModified() = modified

}