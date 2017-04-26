package io.skysail.core.model

import io.skysail.domain.core.EntityModel
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
case class SkysailResourceModel2(val path: String, val targetResource: Class[_ <: SkysailServerResource[_]]) {

  private val log = LoggerFactory.getLogger(this.getClass())
  
  require(path != null, "A ResourceModel's path must not be null")
  require(path.trim().length() > 0, "A ResourceModel's path must not be empty")
  require(targetResource != null, "A ResourceModel's target class must not be null")

  val resource: SkysailServerResource[_] = determineTargetResource()
  val entityClass: Class[_] = determineTargetEntity()
  
  resource.linkedResources.foreach { 
    linkedResource => resource.addAssociatedResource((LINKED_RESOURCE, linkedResource)) 
  }

  def resourceType() = {
    resource match {
      case _: ListServerResource2[_] => LIST_RESOURCE
      case _: EntityServerResource2[_] => ENTITY_RESOURCE
      case _: PutEntityServerResource2[_] => UPDATE_ENTITY_RESOURCE
      case _: PostEntityServerResource2[_] => CREATE_ENTITY_RESOURCE
      case _ => UNSPECIFIED_RESOURCE
    }
  }
  
  def getAssociatedResources(`type`: ResourceAssociationType) = {
    resource.associatedResources.filter(p => p._1 == `type`).map(p => p._2).toList
  }
  
  def links():List[LinkModel] = {
    resource.associatedResources.map(r => LinkModel(r._1,r._2)).toList
  }

  private def determineTargetResource() = targetResource.newInstance().asInstanceOf[SkysailServerResource[_]]
  private def determineTargetEntity() = ScalaSkysailRouter.getResourcesGenericType(resource)

}