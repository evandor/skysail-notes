package io.skysail.core.model

import scala.collection.mutable.LinkedHashMap
import org.slf4j.LoggerFactory
import scala.collection.mutable.HashMap
import io.skysail.restlet.SkysailServerResource
import io.skysail.core.ApiVersion

/**
 * This is the root class of skysail's core domain, describing an application,
 * which aggregates resources, their associated entities (together with the entities' fields)
 * and, finally, links between the resources.
 *
 *  @constructor create a new application model, identified by its name.
 *
 *  @param name the model's (unique and descriptive) name
 */
case class ApplicationModel(val name: String, apiVersion: ApiVersion) {

  private val log = LoggerFactory.getLogger(this.getClass())

  require(name != null, "The application's name should be unique and must not be null")
  require(name.trim().length() > 0, "The application's name must not be empty")

  private val resourceModels = scala.collection.mutable.ListBuffer[ResourceModel]()
  private val entityModelsMap: LinkedHashMap[String, EntityModel] = scala.collection.mutable.LinkedHashMap()

  private var built = false // has the build method been called?

  /**
   * adds an non-null resource model identified by its path.
   *
   * If a resource model with the same name exists already, this method
   * returns silently.
   *
   * Otherwise, the resource model will be added to the map of managed resources.
   */
  def addResourceModel(path: String, cls: Class[_ <: io.skysail.restlet.SkysailServerResource[_]]): Option[Class[_]] = {
    val resourceModel = new ResourceModel(path, cls)
    if (resourceModels.filter(rm => rm.path == resourceModel.path).headOption.isDefined) {
      log.info(s"trying to add entity ${resourceModel.path} again, ignoring...")
      return None
    }
    val entityClass = resourceModel.entityClass
    if (!entityModelsMap.get(entityClass.getName).isDefined) {
      entityModelsMap += entityClass.getName -> EntityModel(entityClass)
    }
    resourceModels += resourceModel
    Some(resourceModel.entityClass)
  }

  def build(): Unit = {
    built = true
    resourceModels.foreach {
      resourceModel =>
        {
          var result = scala.collection.mutable.ListBuffer[LinkModel]()
          resourceModel.resource.linkedResourceClasses().foreach {
            lrCls =>
              {
                val res = resourceModelFor(lrCls)
                if (res.isDefined) {
                  result += new LinkModel(appPath() + res.get.path, LINKED_RESOURCE, res.get.resource, lrCls)
                }
              }
          }

          val r = resourceModel.resource.associatedResourceClasses().map(r => resourceModels.filter { resourceModel => resourceModel.targetResourceClass == r }.headOption).toList
          r.foreach { res =>
            {
              if (res.isDefined) {
                result += new LinkModel(appPath() + res.get.path, LINKED_RESOURCE, res.get.resource, res.get.resource.getClass)
              }
            }
          }
          resourceModel.linkModels = result.toList
        }
    }
  }

  def resourceModelFor(cls: Class[_ <: SkysailServerResource[_]]) = resourceModels.filter { model => model.targetResourceClass == cls }.headOption

  def entityModelFor(id: String) = entityModelsMap.get(id)

  def linksFor(resourceClass: Class[_ <: io.skysail.restlet.SkysailServerResource[_]]): List[LinkModel] = {
    val r = resourceModels.filter { resourceModel => resourceModel.resource.getClass == resourceClass }.headOption
    if (r.isDefined) {
      r.get.linkModels
    } else {
      List()
    }
  }

  override def toString() = s"""${this.getClass.getSimpleName}($name) [Build: ${built}]
    Resources: ${resourceModels.map { v => sys.props("line.separator") + " " * 6 + v }.mkString("")}
    Entities: ${printMap(entityModelsMap)}"""

  private def printMap(map: scala.collection.mutable.Map[_, _]) = map.map(v => s"""
      ${v._1} => ${v._2.toString()}""").mkString("")

  private def appPath() = "/" + name + (if (apiVersion != null) apiVersion.getVersionPath() else "")

}