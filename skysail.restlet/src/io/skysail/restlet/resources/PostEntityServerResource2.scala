package io.skysail.restlet.resources

import org.restlet.resource.Post
import org.restlet.representation.Variant
import org.restlet.data.Form
import org.json4s._
import org.json4s.jackson.JsonMethods._
import scala.collection.JavaConversions._
import scala.collection.mutable.MutableList
import org.restlet.resource.Get
import java.util.Arrays
import io.skysail.api.links.Link
import io.skysail.restlet.ScalaSkysailServerResource
import io.skysail.restlet.responses.FormResponse
import io.skysail.restlet.responses.ScalaSkysailResponse
import org.restlet.data.Status
import io.skysail.restlet.ScalaRequestHandler
import io.skysail.restlet.ScalaResponseWrapper
import io.skysail.restlet.resources.helper.FormDeserializer

abstract class PostEntityServerResource2[T: Manifest] extends ScalaSkysailServerResource {

  //implicit val formats = DefaultFormats 

  def createEntityTemplate(): T

  //def getEntity():Note = createEntityTemplate()

//  class FormDeserializer[T](cls: Class[_]) {
//    def createFrom(form: Form): JValue = {
//      println(form)
//      //      val elements = MutableList[JField]()
//      //      form.getNames()
//      //        .filter(_ != "submit")
//      //        .foreach(key => elements += JField(key, JString(form.getFirstValue(key))))
//      //      //val jValue: org.json4s.JsonAST.JValue = JObject(elements.toList)
//      //      JObject(elements.toList)
//      //val json = parse("""{"name":"joe"}""")
//      //json.extract[Note]
//      //jValue.extract[Note]
//      val sb = form.getNames()
//        .filter(_ != "submit")
//        //        .foreach { 
//        //          key => sb.append("\""+key+"\":\"" + form.getFirstValue(key) + "\"") 
//        //        }
//        .map(key => "\"" + key.split("\\|")(1) + "\":\"" + form.getFirstValue(key) + "\"")
//        .mkString(",")
//      println(sb)
//      parse("{" + sb + "}")
//      //json.extract[T]
//    }
//  }

  @Get("htmlform|html")
  def getPostTemplate() = {
    val timerMetric = getMetricsCollector().timerFor(this.getClass(), "getPostTemplate");
    val templatePaths = getSkysailApplication().getTemplatePaths(this.getClass());
    val formTarget = templatePaths.stream().findFirst().orElse(".");
    val links = Arrays.asList(new Link.Builder(formTarget).build());
    //links.stream().forEach(getPathSubstitutions());

    val entity: T = createEntityTemplate();
    //this.setEntity(entity);
    timerMetric.stop();
    new FormResponse[T](getResponse(), entity, links.get(0).getUri());
  }

  @Post("x-www-form-urlencoded:html")
  def post(form: Form, variant: Variant): ScalaSkysailResponse[T] = {
    implicit val formats = DefaultFormats
    val timerMetric = getMetricsCollector().timerFor(this.getClass(), "posthtml");
    val jValue = FormDeserializer.createFrom(form,getParameterizedType());
    val entity = jValue.extract[T]
    val result = jsonPost(entity.asInstanceOf[T], variant)
    timerMetric.stop();
    result
  }

  @Post("json")
  def jsonPost(entity: T, variant: Variant): ScalaSkysailResponse[T] = {
    val timerMetric = getMetricsCollector().timerFor(this.getClass(), "postjson");
    //getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
    val handledRequest = doPost(entity, variant)
    timerMetric.stop();
    //    if (handledRequest.getConstraintViolationsResponse() != null) {
    //      return handledRequest.getConstraintViolationsResponse();
    //    }
    new FormResponse[T](getResponse(), entity /*handledRequest.getEntity()*/ , ".")
  }

  def doPost(entity: T, variant: Variant): ScalaResponseWrapper[T] = {
    //      addToRequestAttributesIfAvailable(SKYSAIL_SERVER_RESTLET_ENTITY, entity);
    //      addToRequestAttributesIfAvailable(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
    val requestHandler = new ScalaRequestHandler[T]() //getSkysailApplication());
    val handler = requestHandler.createForPost(); //AbstractResourceFilter<PostEntityServerResource<T>, T>
    getResponse().setStatus(Status.SUCCESS_CREATED);
    //      handler.handle(this, getResponse());
    null
  }
}