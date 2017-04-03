package io.skysail.restlet

import io.skysail.api.text.Translation
import io.skysail.core.app.SkysailApplication
import io.skysail.core.utils._
import io.skysail.api.links.Link
import io.skysail.restlet.app.ScalaSkysailApplication
import io.skysail.server.ResourceContextId
import io.skysail.restlet.forms.ScalaFormField
import java.util.function.Consumer
import org.restlet.resource.ServerResource
import io.skysail.server.forms.MessagesUtils
import io.skysail.restlet.utils.ScalaMessagesUtils

abstract class ScalaSkysailServerResource extends ServerResource {

  var entity: AnyRef = null
  def setEntity(e: AnyRef) = entity = e

  val stringContextMap = new java.util.HashMap[ResourceContextId, String]()

  def getSkysailApplication() = getApplication().asInstanceOf[ScalaSkysailApplication]
  def getMetricsCollector() = getSkysailApplication().getMetricsCollector()
  def getParameterizedType() = ReflectionUtils.getParameterizedType(getClass());

  /*def getPathSubstitutions(): Consumer[Link] = {
    return l -> {
      val uri = l.getUri();
      l.setUri(LinkUtils.replaceValues(uri, getRequestAttributes()));
    }
  }*/

  def getFromContext(id: ResourceContextId) = stringContextMap.get(id)

  def addToContext(id: ResourceContextId, value: String): Unit = stringContextMap.put(id, value)

  def getMessages(): Map[String, Translation] = {
    val msgs = Map[String, Translation]()
    val key = getClass().getName() + ".message";
    val translated = getSkysailApplication().translate(key, "", this);
    //msgs.put("content.header", translated);
    return msgs;
  }

  def getMessages(fields: Map[String, ScalaFormField]): Map[String, Translation] = {
    val msgs = getMessages();
    if (fields == null) {
      return msgs;
    }

    fields.foreach(f => {
      var entityClass:Class[_] = null;
      if (entity != null) {
        if (entity.isInstanceOf[List[_]] && entity.asInstanceOf[List[_]].length > 0) {
          entityClass = entity.asInstanceOf[List[_]](0).getClass()
        } else {
          entityClass = entity.getClass();
        }
      }
      
      val baseKey = ScalaMessagesUtils.getBaseKey(entityClass, f._2); // io.skysail.server.app.notes.Note.title
      val fieldName = ScalaMessagesUtils.getSimpleName(f._2); // title
//                addTranslation(msgs, application, baseKey, f.getLabel());
//                addTranslation(msgs, application, baseKey + ".info", null);
//                addTranslation(msgs, application, baseKey + ".placeholder", null);
//                addTranslation(msgs, application, baseKey + ".desc", null);
//                addTranslation(msgs, application, baseKey + ".polymerPageContent", null);
//    
//                String resourceBaseKey = this.getClass().getName() + "." + fieldName; // io.skysail.server.app.notes.resources.PostNoteResource.content
//                addTranslation(msgs, application, resourceBaseKey, fieldName);
//                addTranslation(msgs, application, resourceBaseKey + ".desc", null);
//                addTranslation(msgs, application, resourceBaseKey + ".placeholder", null);


    })

    return msgs;
  }

}