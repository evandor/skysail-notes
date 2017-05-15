package io.skysail.app.notes.domain

import javax.validation.constraints._
import com.fasterxml.jackson.annotation._
import java.util.Date

import scala.annotation.meta.field
import scala.beans.BeanProperty

import io.skysail.core.html._
import io.skysail.domain.ddd.ScalaEntity
import io.skysail.restlet.forms._

//@JsonIgnoreProperties(ignoreUnknown = true)
@BeanProperty
case class Note (
    var id: Option[String] = None,
    
    @(Field @field)(description = "The note's content") 
    /*@(NotNull @field) @Size(min=1)*/ 
    @BeanProperty var content: String = ""
  
) extends ScalaEntity[String] {
  
  @Field(inputType = InputType.DATE, description = "auto-set field containing the note's creation date")
  @PostView(visibility = Visibility.HIDE)
  @PutView(visibility = Visibility.HIDE)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  var created: Date = new Date()
  def setCreated(c: Date) = created = c
  def getCreated() = created

  @Field(inputType = InputType.DATE, description = "auto-set field containing the note's modified date, if any.")
  @PostView(visibility = Visibility.HIDE)
  @PutView(visibility = Visibility.HIDE)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  var modified: Date = new Date()
  def setModified(c: Date) = modified = c
  def getModified() = modified

}