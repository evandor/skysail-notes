package io.skysail.restlet

import io.skysail.api.text.Translation
import io.skysail.restlet.app.SkysailApplication
import io.skysail.restlet.forms.ScalaFormField
import java.util.function.Consumer
import org.restlet.resource.ServerResource
import io.skysail.restlet.utils.ScalaMessagesUtils
import org.restlet.Application
import java.util.Locale
import java.util.Collections
import io.skysail.restlet.utils._
import io.skysail.api.doc.ApiMetadata
import scala.reflect.runtime.universe._
import io.skysail.core.model.ResourceAssociationType
import io.skysail.core.model.LinkRelation
import org.restlet.data.Method
import io.skysail.restlet.resources.ListServerResource
import io.skysail.core.model.ResourceAssociationType
import io.skysail.core.model.ENTITY_RESOURCE_FOR_LIST_RESOURCE
import org.slf4j.LoggerFactory

object SkysailServerResource {
  //val SKYSAIL_SERVER_RESTLET_FORM = "de.twenty11.skysail.server.core.restlet.form";
  val SKYSAIL_SERVER_RESTLET_ENTITY = classOf[SkysailServerResource[_]].getName + ".entity"
  val SKYSAIL_SERVER_RESTLET_VARIANT = classOf[SkysailServerResource[_]].getName + ".variant"

  val FILTER_PARAM_NAME = "_f";
  val PAGE_PARAM_NAME = "_page";
  val SEARCH_PARAM_NAME = "_search";

  val NO_REDIRECTS = "noRedirects";
  val INSPECT_PARAM_NAME = "_inspect";

}

abstract class SkysailServerResource[T] extends ServerResource {
  
  private val log = LoggerFactory.getLogger(this.getClass())

  var entity: AnyRef = null
  def setEntity(e: AnyRef) = entity = e

  def getEntity(): Any

  val stringContextMap = new java.util.HashMap[ResourceContextId, String]()
  
  val associatedResourceClasses = scala.collection.mutable.ListBuffer[Tuple2[ResourceAssociationType, Class[_ <: SkysailServerResource[_]]]]()

  def getSkysailApplication() = getApplication().asInstanceOf[SkysailApplication]
  def getMetricsCollector() = getSkysailApplication().getMetricsCollector()
  def getParameterizedType() = ScalaReflectionUtils.getParameterizedType(getClass());

  /*def getPathSubstitutions(): Consumer[Link] = {
    return l -> {
      val uri = l.getUri();
      l.setUri(LinkUtils.replaceValues(uri, getRequestAttributes()));
    }
  }*/

  def getFromContext(id: ResourceContextId) = stringContextMap.get(id)

  def addToContext(id: ResourceContextId, value: String): Unit = stringContextMap.put(id, value)

  def getMessages(): java.util.Map[String, Translation] = {
    val msgs = new java.util.HashMap[String, Translation]()
    val key = getClass().getName() + ".message";
    val translated = getSkysailApplication().translate(key, "", this);
    //msgs.put("content.header", translated);
    return msgs;
  }

  def getMessages(fields: Iterable[ScalaFormField]): java.util.Map[String, Translation] = {
    val msgs = getMessages();
    if (fields == null) {
      return msgs;
    }

    fields.foreach(f => {
      var entityClass: Class[_] = null;
      if (entity != null) {
        if (entity.isInstanceOf[List[_]] && entity.asInstanceOf[List[_]].length > 0) {
          entityClass = entity.asInstanceOf[List[_]](0).getClass()
        } else {
          entityClass = entity.getClass();
        }
      }

      val baseKey = ScalaMessagesUtils.getBaseKey(entityClass, f); // io.skysail.server.app.notes.Note.title
      val fieldName = ScalaMessagesUtils.getSimpleName(f); // title
      val app = getSkysailApplication()
      addTranslation(msgs, app, baseKey, f.getLabel());
      addTranslation(msgs, app, baseKey + ".info", null);
      addTranslation(msgs, app, baseKey + ".placeholder", null);
      addTranslation(msgs, app, baseKey + ".desc", null);
      addTranslation(msgs, app, baseKey + ".polymerPageContent", null);

      val resourceBaseKey = this.getClass().getName() + "." + fieldName; // io.skysail.server.app.notes.resources.PostNoteResource.content
      addTranslation(msgs, app, resourceBaseKey, fieldName);
      addTranslation(msgs, app, resourceBaseKey + ".desc", null);
      addTranslation(msgs, app, resourceBaseKey + ".placeholder", null);

    })

    return msgs;
  }

  private def addTranslation(
    msgs: java.util.Map[String, Translation],
    application: SkysailApplication,
    key: String,
    defaultMsg: String) = {
    val translation = application.translate(key, defaultMsg, this);
    if (translation != null && translation.value != null) {
      msgs.put(key, translation);
    } else if (defaultMsg != null) {
      msgs.put(key, new Translation(defaultMsg, null, Locale.getDefault(), Seq()));
    }
  }

  def getLinkRelation() = LinkRelation.CANONICAL
  def getVerbs():Set[Method] = Set()
  def getApiMetadata() = ApiMetadata.builder().build()

  def redirectTo(): String = null

  def redirectTo(cls: Class[_ <: SkysailServerResource[_]]): String = {
//    val linkheader = LinkUtils.fromResource(getSkysailApplication(), cls);
//    if (linkheader == null) {
      return null;
//    }
//    //getPathSubstitutions().accept(linkheader);
//    linkheader.uri
  }

 
  
  def linkedResourceClasses():List[Class[_ <: SkysailServerResource[_]]] = List()
  
  protected def addAssociatedResourceClasses(typeAndClassTuples: List[Tuple2[ResourceAssociationType, Class[_ <: SkysailServerResource[_]]]]): Unit = {
    typeAndClassTuples.foreach(tupel => {
      if (tupel._1 == ENTITY_RESOURCE_FOR_LIST_RESOURCE) {
        if (associatedResourceClasses.map(arc => arc._1).filter(theType => theType == ENTITY_RESOURCE_FOR_LIST_RESOURCE).headOption.isDefined) {
          log.warn(s"associated Resource Class for type ENTITY_RESOURCE_FOR_LIST_RESOURCE is already defined. Ignoring new one.")
        }
      }
      associatedResourceClasses += tupel
    })
  }

  
}