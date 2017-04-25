package io.skysail.core.model

import collection.mutable.Stack
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SkysailFieldModel2Spec extends FlatSpec with BeforeAndAfterEach {

  var model: SkysailFieldModel2 = null

  override def beforeEach() {
    println(TestEntity.getClass().getDeclaredFields)
    model = SkysailFieldModel2(TestEntity.getClass().getDeclaredField("content"))
  }

  "A FieldModel" should "not accept a null field in constructor" in {
    assertThrows[IllegalArgumentException] { new SkysailFieldModel2(null) }
  }
  
   "A FieldModel" should "have the entities name as its model name" in {
    assert(model.name == "TestEntity")
  }

//  "A FieldModel" should "identify the @Field-Annotated fields of the provided entity class" in {
//    println(model.fields)
//  }

}