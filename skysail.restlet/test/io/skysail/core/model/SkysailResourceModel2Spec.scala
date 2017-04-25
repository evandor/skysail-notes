package io.skysail.core.model

import collection.mutable.Stack
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SkysailResourceModel2Spec extends FlatSpec with BeforeAndAfterEach {

  var model: SkysailResourceModel2 = null

  override def beforeEach() {
    model = SkysailResourceModel2("/path", classOf[TestResource])
  }

  "A ResourceModel" should "not accept a null value as its path" in {
    assertThrows[IllegalArgumentException] { new SkysailResourceModel2(null, null) }
  }

  "A ResourceModel" should "not accept a null value as its target class" in {
    assertThrows[IllegalArgumentException] { new SkysailResourceModel2("/path", null) }
  }

  "A ResourceModel" should "provide access to the target resource" in {
    assert(model.targetResource.getClass.getName == classOf[TestResource].getName)
  }

  "A ResourceModel" should "provide access to the target entity" in {
    println(model.targetEntity.toString)
    assert(model.targetEntity.toString == "class io.skysail.core.model.TestEntity")
  }

}