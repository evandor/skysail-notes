package io.skysail.core.model

import org.restlet.data.Method
import io.skysail.restlet.SkysailServerResource
import scala.annotation.meta.field
import scala.beans.BeanProperty

case class LinkModel(uri: String, rat: ResourceAssociationType = null, 
    @BeanProperty val resourceClass: Class[_ <: SkysailServerResource[_]] = null) {

  @BeanProperty val title: String = "unknown"
  @BeanProperty val alt: String = "-"
  @BeanProperty val relation: LinkRelation = LinkRelation.CANONICAL
  @BeanProperty val verbs: Set[Method] = Set()
  @BeanProperty val needsAuth: Boolean = false
  @BeanProperty val linkRole: LinkRole = LinkRole.DEFAULT
  @BeanProperty var refId: String = _
  @BeanProperty var cls: Class[_] = _

}