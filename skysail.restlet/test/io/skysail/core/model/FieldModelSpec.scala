package io.skysail.core.model

import collection.mutable.Stack
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SkysailFieldModel2Spec extends FlatSpec {

  "A FieldModel" should "not accept a null field in constructor" in {
    
//    typeOf[TestEntity].members
    
    val model = FieldModel(classOf[TestEntity].getDeclaredField("content"))
    assertThrows[IllegalArgumentException] { new FieldModel(null) }
  }

  "A FieldModel" should "have the entities name as its model name" in {
    val model = FieldModel(classOf[TestEntity].getDeclaredField("content"))
    assert(model.name == "content")
  }

  //  "A FieldModel" should "identify the @Field-Annotated fields of the provided entity class" in {
  //    println(model.fields)
  //  }

}