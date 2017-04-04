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
import org.restlet.Application
import java.util.Locale
import java.util.Collections

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
      addTranslation(msgs, app, baseKey, f.  getLabel());
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
      application: ScalaSkysailApplication, 
      key: String,
      defaultMsg: String) = {
    val translation = application.translate(key, defaultMsg, this);
    if (translation != null && translation.getValue() != null) {
      msgs.put(key, translation);
    } else if (defaultMsg != null) {
      msgs.put(key, new Translation(defaultMsg, null, Locale.getDefault(), Collections.emptySet()));
    }
  }

}