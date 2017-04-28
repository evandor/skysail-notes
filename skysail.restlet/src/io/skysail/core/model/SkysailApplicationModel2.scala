package io.skysail.core.model

import scala.collection.mutable.LinkedHashMap
import org.slf4j.LoggerFactory
import scala.collection.mutable.HashMap
import io.skysail.restlet.SkysailServerResource

/**
 * This is the root class of skysail's core domain, describing an application,
 * which aggregates resources and their entities, which in turn aggregate fields.
 *
 *  @constructor create a new application model, identified by its name.
 *
 *  @param name the model's (unique and descriptive) name
 */
case class SkysailApplicationModel2(val name: String) {

  private val log = LoggerFactory.getLogger(this.getClass())

  require(name != null, "The application's name should be unique and must not be null")
  require(name.trim().length() > 0, "The application's name must not be empty")

  private val resourceModelsMap: LinkedHashMap[String, SkysailResourceModel2] = scala.collection.mutable.LinkedHashMap()
  private val entityModelsMap: LinkedHashMap[String, SkysailEntityModel2] = scala.collection.mutable.LinkedHashMap()

  def resourceModelFor(path: String) = resourceModelsMap.get(path)
  def resourceModelFor(cls: Class[_ <: SkysailServerResource[_]]) = resourceModelsMap.values.filter { model => model.targetResource == cls }.headOption
  def entityFor(id: String) = entityModelsMap.get(id)

  /**
   * adds an non-null resource model identified by its path.
   *
   * If a resource model with the same name exists already, this method
   * returns silently.
   *
   * Otherwise, the resource model will be added to the map of managed resources.
   */
  def addResourceModel[T <: SkysailResourceModel2](resourceModel: T): Unit = {
    if (resourceModelsMap.get(resourceModel.path).isDefined) {
      log.info(s"trying to add entity ${resourceModel.path} again, ignoring")
      return
    }
    val entityClass = resourceModel.entityClass
    if (!entityModelsMap.get(entityClass.getName).isDefined) {
      entityModelsMap += entityClass.getName -> SkysailEntityModel2(entityClass)
    }
    resourceModelsMap += resourceModel.path -> resourceModel
  }

  def build(): Unit = {
    //resourceModelsMap.values.foreach { resourceModel => resourceModel.generateLinkModels(resourceModelsMap.toMap) }
    resourceModelsMap.values.foreach {
      resourceModel =>
        {
          var result = scala.collection.mutable.ListBuffer[LinkModel]()
          resourceModel.resource.linkedResourceClasses().foreach { 
            lr => {
              val resModel = resourceModelFor(lr)
              if (resModel.isDefined) {
                result += new LinkModel(resModel.get.path, LINKED_RESOURCE, lr)
              }
            }
          }
          println(result)

          val r = resourceModel.resource.associatedResourceClasses().map(r => resourceModelsMap.values.filter { resourceModel => resourceModel.targetResource == r }.headOption).toList
          r.foreach { res =>
            {
              if (res.isDefined) {
                result += new LinkModel(res.get.path, LINKED_RESOURCE, res.get.resource.getClass)
              }
            }
          }

          println(result)
          resourceModel.linkModels = result.toList
        }
    }
  }

  def linksFor(resourceClass: Class[_ <: io.skysail.restlet.SkysailServerResource[_]]): List[LinkModel] = {
    //println(this)
    val r = resourceModelsMap.values.filter { resourceModel => resourceModel.resource.getClass == resourceClass }.headOption
    if (r.isDefined) {
      println(r.get)
      r.get.linkModels
    } else {
      List()
    }
  }

  override def toString() = s"""${this.getClass.getSimpleName}($name)
    Resources: ${printMap(resourceModelsMap)}
    Entities: ${printMap(entityModelsMap)}"""

  private def printMap(map: scala.collection.mutable.Map[_, _]) = map.map(v => s"""
      ${v._1} => ${v._2.toString()}""").mkString("")
}