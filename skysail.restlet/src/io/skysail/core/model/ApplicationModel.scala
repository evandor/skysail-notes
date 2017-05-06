package io.skysail.core.model

import scala.collection.mutable.LinkedHashMap
import org.slf4j.LoggerFactory
import scala.collection.mutable.HashMap
import io.skysail.restlet.SkysailServerResource
import io.skysail.core.ApiVersion
import scala.None
import org.restlet.Request

/**
 * This is the root class of skysail's core domain, describing an application,
 * which aggregates resources, their associated entities (together with the entities' fields)
 * and, finally, links between the resources.
 *
 *  @constructor create a new application model, identified by its name.
 *
 *  @param name the model's (unique and descriptive) name
 */
case class ApplicationModel(
    val name: String,
    apiVersion: ApiVersion,
    associatedResourceClasses: List[Tuple2[ResourceAssociationType, Class[_ <: SkysailServerResource[_]]]] = List()) {

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
    resourceModels.foreach {
      resourceModel =>
        {
          resourceModel.linkModel = new LinkModel(appPath(), resourceModel.path, APPLICATION_CONTEXT_RESOURCE, resourceModel.resource, resourceModel.resource.getClass)
          var result = scala.collection.mutable.ListBuffer[LinkModel]()
          resourceModel.resource.linkedResourceClasses().foreach {
            lrCls =>
              {
                val res = resourceModelFor(lrCls)
                if (res.isDefined) {
                  result += new LinkModel(appPath(), res.get.path, LINKED_RESOURCE, res.get.resource, lrCls)
                }
              }
          }
          val associatedResourceModels = resourceModel.resource.associatedResourceClasses
            .map(r => resourceModelFor(r._2))
            .filter(r => r.isDefined)
            .map(r => r.get)
            .toList

          associatedResourceModels.foreach { resModel =>
            {
              result += new LinkModel(appPath(), resModel.path, LINKED_RESOURCE, resModel.resource, resModel.resource.getClass)
            }
          }
          resourceModel.linkModels = result.toList
        }
    }
    built = true
  }

  def resourceModelFor(cls: Class[_ <: SkysailServerResource[_]]) = {
    log.info(s"resourceModelFor($cls): checking ${resourceModels.map(m => m.targetResourceClass).mkString(";")}")
    resourceModels.filter { model => model.targetResourceClass == cls }.headOption
  }

  def entityModelFor(id: String) = entityModelsMap.get(id)

  def entityModelFor(resourceClass: Class[_ <: io.skysail.restlet.SkysailServerResource[_]]): Option[EntityModel] = {
    val resModel = resourceModelFor(resourceClass)
    if (resModel.isDefined) {
      entityModelFor(resModel.get.entityClass.getName)
    } else {
      None
    }
  }

  def linksFor(resourceClass: Class[_ <: io.skysail.restlet.SkysailServerResource[_]]): List[LinkModel] = {
    val r = resourceModels.filter { resourceModel => resourceModel.resource.getClass == resourceClass }.headOption
    if (r.isDefined) {
      r.get.linkModels
    } else {
      List()
    }
  }

  def toHtml(request: Request) = s"""<b>${this.getClass.getSimpleName}</b>("$name","$apiVersion") [Built: ${built}]<br><br>
    &nbsp;&nbsp;&nbsp;<u>Entities</u>: <ul>${printHtmlMap(entityModelsMap)}</ul>
    &nbsp;&nbsp;&nbsp;<u>Resources</u>: <ul>${resourceModels.map { v => "<li>" + v.toHtml(name, apiVersion, request) + "</li>" }.mkString("")}</ul>"""

  override def toString() = s"""${this.getClass.getSimpleName}("$name","$apiVersion") [Built: ${built}]
    Resources: ${resourceModels.map { v => sys.props("line.separator") + " " * 6 + " - " + v }.mkString("")}
    Entities: ${printMap(entityModelsMap)}"""

  private def printHtmlMap(map: scala.collection.mutable.Map[String, EntityModel]) = map.map(v => s"""
      <li>"${v._1}" -> ${v._2.toHtml}</li>""").mkString("")

  private def printMap(map: scala.collection.mutable.Map[String, EntityModel]) = map.map(v => s"""
      "${v._1}" -> ${v._2.toHtml}""").mkString("")

  private def appPath() = "/" + name + (if (apiVersion != null) apiVersion.getVersionPath() else "")

}