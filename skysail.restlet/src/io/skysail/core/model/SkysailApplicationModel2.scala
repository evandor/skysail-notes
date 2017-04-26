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

  private val resourcesMap: LinkedHashMap[String, SkysailResourceModel2] = scala.collection.mutable.LinkedHashMap()
  private val entitiesMap: LinkedHashMap[String, SkysailEntityModel2] = scala.collection.mutable.LinkedHashMap()

  def resourceModelFor(path: String) = resourcesMap.get(path)
  def entityFor(id: String) = entitiesMap.get(id)
  
  /**
   * adds an non-null resource model identified by its path.
   *
   * If a resource model with the same name exists already, this method
   * returns silently.
   *
   * Otherwise, the resource model will be added to the map of managed resources.
   */
  def addResourceModel[T <: SkysailResourceModel2](resourceModel: T): Unit = {
    if (resourcesMap.get(resourceModel.path).isDefined) {
      log.info(s"trying to add entity ${resourceModel.path} again, ignoring")
      return
    }
    val entityClass = resourceModel.entityClass
    if (!entitiesMap.get(entityClass.getName).isDefined) {
      entitiesMap += entityClass.getName -> SkysailEntityModel2(entityClass)
    }
    resourcesMap += resourceModel.path -> resourceModel
  }
  
  def linksFor(resourceClass: Class[_ <: io.skysail.restlet.SkysailServerResource[_]]): List[LinkModel] = {
    println(resourcesMap)
    val r = resourcesMap.values.filter { resourceModel => resourceModel.resource.getClass == resourceClass }.headOption
    if (r.isDefined) {
      r.get.links
    } else {
    	List()
    }
    
  }

  //override def toString() = s"Name: ${name}, ID: ${id},\\nEntities: ${resources}"

}