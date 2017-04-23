package io.skysail.domain.model

import collection.mutable.Stack
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ApplicationModelSpec extends FlatSpec with BeforeAndAfterEach {

  var model: ApplicationModel = null

  override def beforeEach() {
    model = ApplicationModel("qualifier.appId", "appName")
  }

  "An ApplicationModel" should "not accept a null value as its id" in {
    assertThrows[IllegalArgumentException] {
      new ApplicationModel(null)
    }
  }

  "An ApplicationModel" should "not accept an empty value as its id" in {
    assertThrows[IllegalArgumentException] { new ApplicationModel("") }
    assertThrows[IllegalArgumentException] { new ApplicationModel(" ") }
  }

  "An empty ApplicationModel" should "be created successfully for a given id and name" in {
    assert(model != null)
    assert(model.id == "qualifier.appId")
    assert(model.name == "appName")
  }

  "An simple ApplicationModel" should "be created successfully for a given id without name" in {
    val model = new ApplicationModel("onlyId")
    assert(model != null)
    assert(model.id == "onlyId")
    assert(model.name == "onlyId")
   // assert(model.toString() == "Name: onlyId, ID: onlyId,\nEntities: Map()")
  }

  "An ApplicationModel" should "add a new minimal ResourceModel" in {
    val resourceModel = new ResourceModel("/path", classOf[String])
    val appModel = model.addOnce(resourceModel)
    val resourceModelFromAppModel = appModel.resources.get("/path").get
    assert(appModel.resources.size == 1)
    assert(resourceModelFromAppModel == resourceModel)
  }

  "An ApplicationModel" should "add an ResourceModel (identified by its id), only once" in {
    model = model.addOnce(new ResourceModel("/path", classOf[String]))
    model = model.addOnce(new ResourceModel("/path", classOf[String]))
    assert(model.resources.size == 1)
  }

}