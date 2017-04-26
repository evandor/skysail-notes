package io.skysail.core.model

import org.restlet.data.Method
import io.skysail.restlet.SkysailServerResource

case class LinkModel(uri: String, rat: ResourceAssociationType, resourceClass: Class[_ <: SkysailServerResource[_]]) {

  
  val title: String = "unknown"
  val alt: String = ""
  val relation: LinkRelation = LinkRelation.CANONICAL
  val verbs: Set[Method] = Set()
  val needsAuth: Boolean = false
  val linkRole: LinkRole = LinkRole.DEFAULT
  var refId: String = _
  var cls: Class[_] = _

}