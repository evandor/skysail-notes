package io.skysail.core.model

import org.restlet.data.Method
import io.skysail.restlet.SkysailServerResource
import scala.annotation.meta.field
import scala.beans.BeanProperty
import io.skysail.restlet.ResourceContextId

case class LinkModel(
    @BeanProperty uri: String,
    @BeanProperty rat: ResourceAssociationType = null,
    resource: SkysailServerResource[_],
    @BeanProperty val resourceClass: Class[_ <: SkysailServerResource[_]] = null) {

  @BeanProperty val relation: LinkRelation = resource.getLinkRelation()
  @BeanProperty val verbs = resource.getVerbs()
  @BeanProperty val title = determineTitle()
  @BeanProperty val alt: String = "-"
  @BeanProperty val needsAuth: Boolean = false
  @BeanProperty val linkRole: LinkRole = LinkRole.DEFAULT
  @BeanProperty var refId: String = _
  @BeanProperty var cls: Class[_] = _

  override def toString() = s"${uri}: ${resourceClass.getSimpleName} ($rat)"

  def asLinkheaderElement(): String = {
    val sb = new StringBuilder().append("<").append(getUri()).append(">");
    sb.append("; rel=\"").append(relation.getName()).append("\"");
    if (getTitle() != null) {
      sb.append("; title=\"").append(getTitle()).append("\"");
    }
    if (getRefId() != null) {
      sb.append("; refId=\"").append(getRefId()).append("\"");
    }
    sb.append("; verbs=\"")
      .append(verbs.map(verb => verb.getName()).mkString(",")).append("\"");
    return sb.toString();
  }
  
  def determineTitle(): String = {
    val title = resource.getFromContext(ResourceContextId.LINK_TITLE)
    if (title == null) "unknown" else title
  }

}