package io.skysail.core.model

import org.restlet.data.Method
import io.skysail.restlet.SkysailServerResource

case class LinkModel(rat: ResourceAssociationType, resourceClass: Class[_ <: SkysailServerResource[_]]) {
  

    val uri: String = ""
    val title: String = "unknown"
    val alt: String = ""
    val relation: LinkRelation = LinkRelation.CANONICAL
    val verbs: Set[Method] = Set()
    val needsAuth: Boolean = false
    val linkRole: LinkRole = LinkRole.DEFAULT
    val refId: String = null
    val cls: Class[_] = null
 
  
}