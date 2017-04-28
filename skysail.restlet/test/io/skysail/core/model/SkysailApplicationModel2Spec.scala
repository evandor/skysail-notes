package io.skysail.core.model

import collection.mutable.Stack
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.slf4j.LoggerFactory

@RunWith(classOf[JUnitRunner])
class SkysailApplicationModel2Spec extends FlatSpec {

  "An ApplicationModel" should "not accept a null value as its name" in {
    assertThrows[IllegalArgumentException] { new SkysailApplicationModel2(null) }
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
    val appModel = SkysailApplicationModel2("appName")

    appModel.addResourceModel("/path", classOf[TestResource])

    val resourceModel = appModel.resourceModelFor(classOf[TestResource])
    assert(resourceModel.isDefined)
    assert(resourceModel.get.path == "/path")
    assert(resourceModel.get.resource.isInstanceOf[TestResource])
    assert(resourceModel.get.targetResourceClass == classOf[TestResource])
    assert(resourceModel.get.entityClass == classOf[TestEntity])
  }

  "An ApplicationModel" should "add an ResourceModel (identified by its path), only once" in {
    val model = SkysailApplicationModel2("appName")
    val entityClass1 = model.addResourceModel("/path", classOf[TestResource])
    val entityClass2 = model.addResourceModel("/path", classOf[TestResource])
    assert(entityClass1.isDefined)
    assert(entityClass2.isEmpty)
  }

  "An ApplicationModel" should "return a resourceModel identified by its class" in {
    val model = SkysailApplicationModel2("appName")
    model.addResourceModel("/path", classOf[TestResource])
    val resourceModel = model.resourceModelFor(classOf[TestResource])
    assert(resourceModel.isDefined)
  }

  "An ApplicationModel" should "retrieve the entity associated with a Resource" in {
    val appModel = SkysailApplicationModel2("appName")
    appModel.addResourceModel("/path", classOf[TestResource])
    val id = classOf[TestEntity].getName
    assert(appModel.entityModelFor(id).isDefined)
  }

  "An ApplicationModel" should "provide the LinkModel for a resource identified by its class" in {
    val model = SkysailApplicationModel2("appName")
    model.addResourceModel("/list", classOf[TestListResource])
    model.addResourceModel("/list/{id}", classOf[TestEntityResource])
    model.build()
    val links = model.linksFor(classOf[TestListResource])
    assert(links.size == 1)
  }

  "An ApplicationModel" should "provide a decent toString representation" in {
    val appModel = SkysailApplicationModel2("appName")
    appModel.addResourceModel("/path1", classOf[TestListResource])
    appModel.addResourceModel("/path2", classOf[TestListResource])
    appModel.build()
    println(appModel)
  }

}