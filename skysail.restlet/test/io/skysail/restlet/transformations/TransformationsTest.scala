package io.skysail.restlet.transformations

import java.util.Date

import scala.beans.BeanProperty

import org.hamcrest.CoreMatchers.is
import org.json4s.DefaultFormats
import org.json4s.jvalue2extractable
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.restlet.data.Form

import com.fasterxml.jackson.annotation.JsonFormat

import io.skysail.domain.Entity
import io.skysail.domain.html.Field
import io.skysail.server.forms.PostView
import io.skysail.server.forms.PutView
import javax.validation.constraints._
import scala.annotation.meta.field
import scala.beans.BeanProperty
import io.skysail.domain.html._
import io.skysail.server.forms._
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

class FormDeserializerTest {

  implicit val formats = DefaultFormats

  case class TestNote(@(NotNull @field) val content: String)

  @JsonIgnoreProperties(ignoreUnknown = true)
  case class Note(
      @BeanProperty var id: scala.Option[String],
      @(Field @field)@BeanProperty /*@(NotNull @field) @Size(min=1)*/ var content: String) {

    //    @Field(inputType = InputType.DATE)
    //    @PostView(visibility = Visibility.HIDE)
    //    @PutView(visibility = Visibility.HIDE)
    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    //    var created: Date = new Date()
    //    def setCreated(c: Date) = created = c
    //    def getCreated() = created
    //
    //    @Field(inputType = InputType.DATE)
    //    @PostView(visibility = Visibility.HIDE)
    //    @PutView(visibility = Visibility.HIDE)
    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    //    var modified: Date = new Date()
    //    def setModified(c: Date) = modified = c
    //    def getModified() = modified

  }

  var form: Form = null

  @Before
  def setUp() {
    form = new Form()
  }

  @Test
  def content_of_TestNote_can_be_deserialized_from_form() = {
    form.add("testNote|content", "theContent")
    val testNote = Transformations.jsonFrom(form).extract[TestNote]
    assertThat(testNote.content, is("theContent"))
  }

  @Test
  def content_of_Note_can_be_deserialized_from_form() = {
    form.add("testNote|content", "theContent")
    val testNote = Transformations.jsonFrom(form).extract[Note]
    assertThat(testNote.content, is("theContent"))
  }

  @Test
  def content_of_TestNote_can_be_deserialized_from_map() = {
    val map = new scala.collection.mutable.HashMap[String, Object]()
    map += "content" -> "theContent"
    val testNote = Transformations.jsonFrom(map.toMap).extract[TestNote]
    assertThat(testNote.content, is("theContent"))
  }

}