package io.skysail.restlet.model.test

import org.mockito.Mockito
import io.skysail.restlet.SkysailServerResource
import org.restlet.Request
import org.restlet.Response
import org.restlet.data.Reference
import org.junit.Test

import org.junit.Assert._
import org.mockito.Mockito._
import org.hamcrest.CoreMatchers._
import org.restlet.data.Form
import org.restlet.util.Series
import org.junit.runner.RunWith
import org.junit.Ignore
import io.skysail.restlet.model._
import javax.validation.constraints._

import scala.annotation.meta.field
import scala.beans.BeanProperty
import io.skysail.restlet.utils.ScalaReflectionUtils

class SkysailFieldModelTest {

  case class TestNote(@(NotNull @field) val content: String)

  @Test
  def atest2(): Unit = {
    val entityModel = mock(classOf[ScalaSkysailEntityModel])
    val fields = ScalaReflectionUtils.getInheritedFields(classOf[TestNote])
    val ssfm = new ScalaSkysailFieldModel(entityModel, fields(0))
    assertThat(ssfm.isMandatory(), is(true))
  }

}