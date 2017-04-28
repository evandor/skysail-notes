package io.skysail.restlet.filter

import io.skysail.restlet.ScalaAbstractResourceFilter
import org.slf4j.LoggerFactory
import org.owasp.html.HtmlPolicyBuilder
import io.skysail.restlet.SkysailServerResource
import io.skysail.restlet.Wrapper3
import org.restlet.Request
import scala.collection.JavaConverters._
import org.owasp.html.HtmlSanitizer
import io.skysail.restlet.ScalaResponseWrapper
import io.skysail.restlet.utils.ScalaReflectionUtils

object CheckInvalidInputFilter {
  val noHtmlPolicyBuilder = new HtmlPolicyBuilder();

}

class CheckInvalidInputFilter[T:Manifest](entity: T) extends ScalaAbstractResourceFilter[T] {

  override val log = LoggerFactory.getLogger(classOf[CheckInvalidInputFilter[T]])

  override def doHandle(resource: SkysailServerResource[_], responseWrapper:  Wrapper3) = {
    log.debug("entering {}#doHandle", this.getClass().getSimpleName());

    // do in "before"?
    val response = responseWrapper.getResponse();
    //Form form = (Form) response.getRequest().getAttributes().get(EntityServerResource.SKYSAIL_SERVER_RESTLET_FORM);

    //val entity = response.getRequest().getAttributes().get(SkysailServerResource.SKYSAIL_SERVER_RESTLET_ENTITY).asInstanceOf[T]
    // TODO: check entity, not form
    if (containsInvalidInput(response.getRequest(), resource, entity)) {
      log.info("Input was sanitized");
    }
    super.doHandle(resource, responseWrapper);
    FilterResult.CONTINUE;
  }

  def containsInvalidInput(request: Request, resource: SkysailServerResource[_], entity: T): Boolean = {
    var foundInvalidInput = false;
    val entityAsObject = request.getAttributes().get(SkysailServerResource.SKYSAIL_SERVER_RESTLET_ENTITY);
    if (entityAsObject != null) {
      val fields = ScalaReflectionUtils.getInheritedFields(entity.getClass());
      foundInvalidInput = handleFields(foundInvalidInput, entity, fields.toList);
      return foundInvalidInput;
    }
    false;
  }

  def handleFields(foundInvalidInput: Boolean, entity: T, fields: List[java.lang.reflect.Field]): Boolean = {
    for (field <- fields) {
      val formField = field.getAnnotation(classOf[io.skysail.core.html.Field]);
      if (formField != null) {
        val htmlPolicy = formField.htmlPolicy();
//        val htmlPolicyBuilder = createHtmlPolicyBuilder(htmlPolicy);
//
//        field.setAccessible(true);
//        var originalValue = "";
//        try {
//          val fieldValue = field.get(entity);
//          if (fieldValue.isInstanceOf[String]) {
//            originalValue = fieldValue.asInstanceOf[String]
//            val sb = new java.lang.StringBuilder();
//            val policy = createPolicy(htmlPolicyBuilder, sb);
//            HtmlSanitizer.sanitize(originalValue, policy);
//            val sanitizedHtml = sb.toString();
//            if (!sanitizedHtml.equals(originalValue)) {
//              try {
//                field.set(entity, sanitizedHtml);
//                log.info(s"sanitized '$originalValue' to '$sanitizedHtml'");
//              } catch {
//                case e: Throwable => log.error(e.getMessage(), e);
//              }
//             // foundInvalidInput = true;
//            }
//
//          }
//        } catch {
//          case e: Throwable => log.error(e.getMessage(), e);
//        }

      }
    }
    return foundInvalidInput;
  }

}