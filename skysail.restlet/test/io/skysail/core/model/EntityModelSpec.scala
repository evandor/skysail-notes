package io.skysail.core.model

import collection.mutable.Stack
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EntityModelSpec extends FlatSpec with BeforeAndAfterEach {

  var model: EntityModel = null

  override def beforeEach() {
    model = EntityModel(new TestEntity(Some("id"),"content").getClass())
  }

  "An EntityModel" should "not accept a null entity in constructor" in {
    assertThrows[IllegalArgumentException] { new EntityModel(null) }
  }
  
   "An EntityModel" should "have the entities name as its model name" in {
    assert(model.name == "io.skysail.core.model.TestEntity")
  }

  "An EntityModel" should "identify the @Field-Annotated fields of the provided entity class" in {
    println(model.fields)
  }

}