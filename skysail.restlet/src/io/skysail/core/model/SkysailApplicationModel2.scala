package io.skysail.core.model

import scala.collection.mutable.LinkedHashMap
import org.slf4j.LoggerFactory
import scala.collection.mutable.HashMap

/**
 * This is the root class of skysail's core domain, describing an application,
 * which aggregates resources and their entities, which in turn aggregate fields.
 *
 *  @constructor create a new application model, identified by its name.
 *
 *  @param name the model's (unique and descriptive) name
 */
case class SkysailApplicationModel2(val name: String) {

  var log = LoggerFactory.getLogger(this.getClass())

  require(name != null, "The application's name should be unique and must not be null")
  require(name.trim().length() > 0, "The application's name must not be empty")

  /**
   * Use the addOnce method to add Resource Models. 
   */
  val resources: LinkedHashMap[String, SkysailResourceModel2] = scala.collection.mutable.LinkedHashMap()

  val entityModels: LinkedHashMap[String, SkysailEntityModel2] = scala.collection.mutable.LinkedHashMap()

  //def this(id: String) = this(id, id)

  /**
   * adds an non-null resource model identified by its path.
   *
   * If a resource model with the same name exists already, this method
   * returns silently.
   *
   * Otherwise, the resource model will be added to the map of managed resources.
   */
  def addOnce[T <: SkysailResourceModel2](resourceModel: T): Unit = {
    if (resources.get(resourceModel.path).isDefined) {
      log.info(s"trying to add entity ${resourceModel.path} again, ignoring")
      return
    }
    val entity = resourceModel.targetEntity
    if (!entityModels.get(entity.getClass.getName).isDefined) {
      entityModels += entity.getClass.getName -> SkysailEntityModel2(entity)
    }
    //log.info(s"trying to add entity ${resourceModel.path} again, ignoring")
    resources += resourceModel.path -> resourceModel
  }

  //override def toString() = s"Name: ${name}, ID: ${id},\\nEntities: ${resources}"

  private def entityToMapEntry(e: SkysailResourceModel2) = e.path -> e //.copy(applicationModel = this)

}