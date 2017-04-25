package io.skysail.core.model

import collection.mutable.Stack
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SkysailEnttiyModel2Spec extends FlatSpec with BeforeAndAfterEach {

  var model: SkysailEntityModel2 = null

  override def beforeEach() {
    model = SkysailEntityModel2(new TestEntity(Some("id"),"content").getClass())
  }

  "An EntityModel" should "not accept a null entity in constructor" in {
    assertThrows[IllegalArgumentException] { new SkysailEntityModel2(null) }
  }
  
   "An EntityModel" should "have the entities name as its model name" in {
    assert(model.name == "io.skysail.core.model.TestEntity")
  }

  "An EntityModel" should "identify the @Field-Annotated fields of the provided entity class" in {
    println(model.fields)
  }

}