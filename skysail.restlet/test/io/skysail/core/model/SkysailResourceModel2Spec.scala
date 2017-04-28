package io.skysail.core.model

import collection.mutable.Stack
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SkysailResourceModel2Spec extends FlatSpec with BeforeAndAfterEach {

  "A ResourceModel" should "not accept a null value as its path" in {
    assertThrows[IllegalArgumentException] { new SkysailResourceModel2(null, null) }
  }

  "A ResourceModel" should "not accept a null value as its target class" in {
    assertThrows[IllegalArgumentException] { new SkysailResourceModel2("/path", null) }
  }

  "A ResourceModel" should "provide access to the target resource" in {
    val model = SkysailResourceModel2("/path", classOf[TestResource])
    assert(model.resource.getClass.getName == classOf[TestResource].getName)
  }

  "A ResourceModel" should "provide access to the target entity" in {
    val model = SkysailResourceModel2("/path", classOf[TestResource])
    assert(model.entityClass.toString == "class io.skysail.core.model.TestEntity")
  }
  
  "A ResourceModel" should "provide the resource type" in {
    val model = SkysailResourceModel2("/path", classOf[TestResource])
    assert(model.resourceType == UNSPECIFIED_RESOURCE)
  }
  
//  "A ResourceModel of a ListResource" should "be associated with its related EntityResource" in {
//    val model = SkysailResourceModel2("/path", classOf[TestListResource])
//    assert(model.associatedResourcesFor(ENTITY_RESOURCE_FOR_LIST_RESOURCE).head.getName == "io.skysail.core.model.TestEntityResource")
//  }
//  
//  "A ResourceModel" should "be associcated with its resources' 'linkedResources'" in {
//    val linkedResources = SkysailResourceModel2("/path", classOf[TestListResource]).associatedResourcesFor(LINKED_RESOURCE)
//    assert(linkedResources.size == 1)    
//    assert(linkedResources.head.getName == "io.skysail.core.model.TestEntityResource")
//  }
  
  "An ResourceModel" should "provide a decent toString representation" in {
    val model = new SkysailResourceModel2("/path1", classOf[TestListResource])
    println(model)
  }

}