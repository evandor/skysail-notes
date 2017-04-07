package io.skysail.restlet.filter

import io.skysail.restlet._
import io.skysail.restlet.Wrapper3
import org.slf4j.LoggerFactory
import io.skysail.domain.html._;
import org.owasp.html._;
import io.skysail.core.resources.SkysailServerResource
import io.skysail.server.restlet.filter.FilterResult

object ExtractStandardQueryParametersResourceFilter {

  var log = LoggerFactory.getLogger(classOf[ExtractStandardQueryParametersResourceFilter[_]])

  def createPolicy(htmlPolicyBuilder: HtmlPolicyBuilder, sb: java.lang.StringBuilder): HtmlSanitizer.Policy = {
    return htmlPolicyBuilder.build(HtmlStreamRenderer.create(sb, new Handler[String]() {
      override def handle(x: String) = {
        log.info(this.getClass().getName() + ": " + x);
      }
    }));
  }
}

class ExtractStandardQueryParametersResourceFilter[T] extends ScalaAbstractResourceFilter[T] {

  override val log = LoggerFactory.getLogger(classOf[ExtractStandardQueryParametersResourceFilter[T]])

  override def beforeHandle(resource: ScalaSkysailServerResource, responseWrapper: Wrapper3) = {
    addToAttributes(resource, SkysailServerResource.FILTER_PARAM_NAME);
    addToAttributes(resource, SkysailServerResource.PAGE_PARAM_NAME);
    addToAttributes(resource, SkysailServerResource.INSPECT_PARAM_NAME);
    addToAttributes(resource, SkysailServerResource.SEARCH_PARAM_NAME);

    //adjustSearchFilter(resource);

    FilterResult.CONTINUE;
  }

  private def addToAttributes(resource: ScalaSkysailServerResource, queryKeyName: String) = {
    val queryValue = resource.getQueryValue(queryKeyName);
    if (queryValue != null && queryValue.trim().length() > 0) {
      val sanitizedValue = sanitize(queryValue.trim());
      resource.getRequest().getAttributes().put(queryKeyName, sanitizedValue);
    }
  }

  // duplication with CheckInvalidInputFilter
  private def sanitize(originalValue: String): String = {
    val sb = new java.lang.StringBuilder();
    val policy = ExtractStandardQueryParametersResourceFilter.createPolicy(createHtmlPolicyBuilder(HtmlPolicy.NO_HTML), sb);
    HtmlSanitizer.sanitize(originalValue, policy);
    val sanitizedHtml = sb.toString();
    if (!sanitizedHtml.equals(originalValue)) {
      log.info(originalValue);
      log.info(sanitizedHtml);
    }
    sanitizedHtml;
  }

  private def createHtmlPolicyBuilder(htmlPolicy: HtmlPolicy) = {
    val htmlPolicyBuilder = new HtmlPolicyBuilder();
    val allowedElements = htmlPolicy.getAllowedElements();
    val allowedAttributes = htmlPolicy.getAllowedAttributes();
    //        htmlPolicyBuilder.allowElements(allowedElements.toArray(new Array[String](allowedElements.size())));
    //
    //        for (att <- allowedAttributes) {
    //            htmlPolicyBuilder.allowAttributes(att.getName()).onElements(att.getForElements());
    //        }
    htmlPolicyBuilder.allowUrlProtocols("http", "https", "file");
    htmlPolicyBuilder;
  }

}
