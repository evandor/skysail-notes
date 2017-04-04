package io.skysail.restlet.model.test

import org.mockito.Mockito
import io.skysail.restlet.ScalaSkysailServerResource
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
import io.skysail.restlet.model.ScalaSkysailFieldModel
import io.skysail.restlet.model.ScalaSkysailEntityModel
import javax.validation.constraints._
import io.skysail.core.utils.ReflectionUtils

import scala.annotation.meta.field
import scala.beans.BeanProperty

class SkysailFieldModelTest {

  case class TestNote(@(NotNull @field) val content: String)

  @Test
  def atest2(): Unit = {
    val entityModel = mock(classOf[ScalaSkysailEntityModel])
    val fields = ReflectionUtils.getInheritedFields(classOf[TestNote])
    val ssfm = new ScalaSkysailFieldModel(entityModel, fields.get(0))
    assertThat(ssfm.isMandatory(), is(true))
  }

}