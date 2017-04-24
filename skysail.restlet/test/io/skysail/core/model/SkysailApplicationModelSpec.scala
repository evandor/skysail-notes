package io.skysail.domain.model

import collection.mutable.Stack
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import io.skysail.core.model.SkysailApplicationModel2
import io.skysail.core.model.SkysailResourceModel

@RunWith(classOf[JUnitRunner])
class SkysailApplicationModelSpec extends FlatSpec with BeforeAndAfterEach {

  var model: SkysailApplicationModel2 = null

  override def beforeEach() {
    model = SkysailApplicationModel2("appName")
  }

  "An ApplicationModel" should "not accept a null value as its id" in {
    assertThrows[IllegalArgumentException] {
      new SkysailApplicationModel2(null)
    }
  }

  "An ApplicationModel" should "not accept an empty value as its id" in {
    assertThrows[IllegalArgumentException] { new SkysailApplicationModel2("") }
    assertThrows[IllegalArgumentException] { new SkysailApplicationModel2(" ") }
  }

  "An empty ApplicationModel" should "be created successfully for a given id and name" in {
    assert(model != null)
    //assert(model.id == "qualifier.appId")
    assert(model.name == "appName")
  }

  "An simple ApplicationModel" should "be created successfully for a given id without name" in {
    val model = new SkysailApplicationModel2("onlyId")
    assert(model != null)
    //assert(model.id == "onlyId")
    assert(model.name == "onlyId")
   // assert(model.toString() == "Name: onlyId, ID: onlyId,\nEntities: Map()")
  }

  "An ApplicationModel" should "add a new minimal ResourceModel" in {
    val resourceModel = new SkysailResourceModel("/path", classOf[String])
    model.addOnce(resourceModel)
    val resourceModelFromAppModel = model.resources.get("/path").get
    assert(model.resources.size == 1)
    assert(resourceModelFromAppModel == resourceModel)
  }

  "An ApplicationModel" should "add an ResourceModel (identified by its id), only once" in {
    model.addOnce(new SkysailResourceModel("/path", classOf[String]))
    model.addOnce(new SkysailResourceModel("/path", classOf[String]))
    assert(model.resources.size == 1)
  }

}