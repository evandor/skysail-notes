package io.skysail.core.model

import collection.mutable.Stack
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.slf4j.LoggerFactory
import io.skysail.core.ApiVersion

@RunWith(classOf[JUnitRunner])
class ApplicationModelSpec extends FlatSpec {

  "An ApplicationModel" should "not accept a null value as its name" in {
    assertThrows[IllegalArgumentException] { new ApplicationModel(null,null) }
  }

  "An ApplicationModel" should "not accept an empty value as its name" in {
    assertThrows[IllegalArgumentException] { new ApplicationModel("",null) }
    assertThrows[IllegalArgumentException] { new ApplicationModel(" ",null) }
  }

  "An empty ApplicationModel" should "be created successfully for a given name" in {
    val model = ApplicationModel("appName",null)
    assert(model != null)
    assert(model.name == "appName")
  }

  "An ApplicationModel" should "add a new minimal ResourceModel" in {
    val appModel = ApplicationModel("appName",null)

    appModel.addResourceModel("/path", classOf[TestResource])

    val resourceModel = appModel.resourceModelFor(classOf[TestResource])
    assert(resourceModel.isDefined)
    assert(resourceModel.get.path == "/path")
    assert(resourceModel.get.resource.isInstanceOf[TestResource])
    assert(resourceModel.get.targetResourceClass == classOf[TestResource])
    assert(resourceModel.get.entityClass == classOf[TestEntity])
  }

  "An ApplicationModel" should "add an ResourceModel (identified by its path), only once" in {
    val model = ApplicationModel("appName",null)
    val entityClass1 = model.addResourceModel("/path", classOf[TestResource])
    val entityClass2 = model.addResourceModel("/path", classOf[TestResource])
    assert(entityClass1.isDefined)
    assert(entityClass2.isEmpty)
  }

  "An ApplicationModel" should "return a resourceModel identified by its class" in {
    val model = ApplicationModel("appName",null)
    model.addResourceModel("/path", classOf[TestResource])
    val resourceModel = model.resourceModelFor(classOf[TestResource])
    assert(resourceModel.isDefined)
  }

  "An ApplicationModel" should "retrieve the entity associated with a Resource" in {
    val appModel = ApplicationModel("appName",null)
    appModel.addResourceModel("/path", classOf[TestResource])
    val id = classOf[TestEntity].getName
    assert(appModel.entityModelFor(id).isDefined)
  }

  "An ApplicationModel" should "provide the LinkModel for a resource identified by its class" in {
    val model = ApplicationModel("appName",new ApiVersion(1))
    model.addResourceModel("/list", classOf[TestEntitiesResource])
    model.addResourceModel("/list/", classOf[PostTestEntityResource])
    model.addResourceModel("/list/{id}", classOf[TestEntityResource])
    model.build()
    println(model)
    val links = model.linksFor(classOf[TestEntitiesResource])
    assert(links.size == 1)
    assert(links.head.uri == "/appName/v1/list/")
  }

  "An ApplicationModel" should "provide a decent toString representation" in {
    val model = ApplicationModel("appName",null)
    model.addResourceModel("/list", classOf[TestEntitiesResource])
    model.addResourceModel("/list/{id}", classOf[TestEntityResource])
    model.build()
    println(model)
  }

}