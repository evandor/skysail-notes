package io.skysail.core.model

import scala.collection.JavaConverters._
import java.lang.reflect.Field
import io.skysail.restlet.utils.ScalaReflectionUtils
import io.skysail.restlet.SkysailServerResource
import io.skysail.restlet.router.ScalaSkysailRouter
import io.skysail.restlet.resources._
import org.slf4j.LoggerFactory

/**
 * A ResourceModel captures the link between a path and a SkysailServerResource, defining
 * which path should be handled by which resource.
 *
 *  @constructor create a new resource model, given a path and a SkysailServerResource.
 *
 *  @param path the uri path relative to the application
 *  @param targetClass a SkysailServerResource class to handle requests to the given path
 */
case class SkysailResourceModel2(val path: String, val targetResourceClass: Class[_ <: SkysailServerResource[_]]) {

  require(path != null, "A ResourceModel's path must not be null")
  require(targetResourceClass != null, "A ResourceModel's target class must not be null")

  private val log = LoggerFactory.getLogger(this.getClass())

  val resource: SkysailServerResource[_] = targetResourceClass.newInstance().asInstanceOf[SkysailServerResource[_]]
  val entityClass: Class[_] = ScalaSkysailRouter.getResourcesGenericType(resource)
  //val associatedResourceModels = scala.collection.mutable.ListBuffer[Tuple2[ResourceAssociationType, Class[_ <: SkysailServerResource[_]]]]()

  var linkModels: List[LinkModel] = List()

  def resourceType() = {
    resource match {
      case _: ListServerResource2[_] => LIST_RESOURCE
      case _: EntityServerResource2[_] => ENTITY_RESOURCE
      case _: PutEntityServerResource2[_] => UPDATE_ENTITY_RESOURCE
      case _: PostEntityServerResource2[_] => CREATE_ENTITY_RESOURCE
      case _ => UNSPECIFIED_RESOURCE
    }
  }

  //  private def links(): List[LinkModel] = {
  //    val l = associatedResourceModels.map(r => {
  //      println(s"Path: $path")
  //      
  //      new LinkModel(path, r._1, r._2)
  //    }).toList
  //    println(l)
  //    l
  //  }

  //  private def associatedResourcesFor(`type`: ResourceAssociationType) = {
  //    associatedResourceModels.filter(p => p._1 == `type`).map(p => p._2).toList
  //  }
  //
  //  private def addAssociatedResourceToModel(inputTuple: Tuple2[ResourceAssociationType, Class[_ <: SkysailServerResource[_]]]) = {
  //    log.info(s"adding associatedResource: $inputTuple")
  //    if (inputTuple._2 != null) {
  //      associatedResourceModels += Tuple2(inputTuple._1, inputTuple._2)
  //    }
  //  }

  override def toString() = s"""${this.getClass.getSimpleName}($path, ${targetResourceClass}, ${entityClass})
        Links: ${printList(linkModels)}"""
  //    Entities: ${printMap(entitiesMap)}"""

  private def printList(list: List[_]) = list.map(v => s"""
          ${v.toString()}""").mkString("")

//  def generateLinkModels(resourceModelsMap: Map[String,SkysailResourceModel2]): Unit = {
//    var result = scala.collection.mutable.ListBuffer[LinkModel]()
//    resource.linkedResourceClasses().foreach { lr => result += new LinkModel(path, LINKED_RESOURCE, lr) }
//    println(result)
//    
//    val r = resource.associatedResourceClasses().map(r => resourceModelsMap.values.filter { resourceModel => resourceModel.targetResource == r }.headOption).toList
//    r.foreach { res => {
//      if (res.isDefined) {
//        result += new LinkModel(res.get.path, LINKED_RESOURCE, res.get.resource.getClass ) }
//      }
//    }
//    
//    println(result)
//    linkModels = result.toList
//  }

}