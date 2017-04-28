package io.skysail.core.model

import collection.mutable.Stack
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.slf4j.LoggerFactory

@RunWith(classOf[JUnitRunner])
class SkysailApplicationModel2Spec extends FlatSpec {

  "An ApplicationModel" should "not accept a null value as its name" in {
    assertThrows[IllegalArgumentException] {
      new SkysailApplicationModel2(null)
    }
  }

  "An ApplicationModel" should "not accept an empty value as its name" in {
    assertThrows[IllegalArgumentException] { new SkysailApplicationModel2("") }
    assertThrows[IllegalArgumentException] { new SkysailApplicationModel2(" ") }
  }

  "An empty ApplicationModel" should "be created successfully for a given name" in {
    val model = SkysailApplicationModel2("appName")
    assert(model != null)
    assert(model.name == "appName")
  }

  "An ApplicationModel" should "add a new minimal ResourceModel" in {
    val model = SkysailApplicationModel2("appName")
    val resourceModel = new SkysailResourceModel2("/path", classOf[TestResource])
    model.addResourceModel(resourceModel)
    val resourceModelFromAppModel = model.resourceModelFor("/path").get
    // assert(model.resourcesMap.size == 1)
    assert(resourceModelFromAppModel == resourceModel)
  }

  /*"An ApplicationModel" should "add an ResourceModel (identified by its path), only once" in {
    val model = SkysailApplicationModel2("appName")
    model.addResource(new SkysailResourceModel2("/path", classOf[TestResource]))
    model.addResource(new SkysailResourceModel2("/path", classOf[TestResource]))
    assert(model.resourcesMap.size == 1)
  }*/

  "An ApplicationModel" should "provide the entity from a ResourceModel" in {
    val model = SkysailApplicationModel2("appName")
    val resourceModel = new SkysailResourceModel2("/path", classOf[TestResource])
    model.addResourceModel(resourceModel)
    val id = classOf[TestEntity].getName
    println(s"trying to retrieve entity with id '$id'")
    val entityModelsFromAppModel = model.entityFor(id).get
    //assert(model.entitiesMap.size == 1)
  }

  "An ApplicationModel" should "provide the LinkModel for a resource identified by its class" in {
    val model = SkysailApplicationModel2("appName")
    val resourceModel = new SkysailResourceModel2("/path", classOf[TestListResource])
    model.addResourceModel(resourceModel)
    val links = model.linksFor(classOf[TestListResource])
    assert(links.size == 2)
  }

  "An ApplicationModel" should "provide a decent toString representation" in {
    val appModel = SkysailApplicationModel2("appName")
    val resourceModel1 = new SkysailResourceModel2("/path1", classOf[TestListResource])
    val resourceModel2 = new SkysailResourceModel2("/path2", classOf[TestListResource])
    appModel.addResourceModel(resourceModel1)
    appModel.addResourceModel(resourceModel2)
    println(appModel)
  }

}