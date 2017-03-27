package io.skysail.app.notes.resources

import org.restlet.resource.Post
import org.restlet.representation.Variant
import org.restlet.data.Form
import io.skysail.api.metrics.TimerMetric
import java.util.LinkedHashMap

import org.json4s._
import org.json4s.jackson.JsonMethods._
//import org.json4s.JsonAST._

import io.skysail.app.notes.domain.Note
import scala.collection.JavaConversions._
import scala.collection.mutable.MutableList
import org.restlet.resource.Get
import java.util.Arrays
import io.skysail.api.links.Link
import io.skysail.api.responses.FormResponse

abstract class PostEntityServerResource2[T/*: Manifest*/] extends ScalaSkysailServerResource[T] {

  //implicit val formats = DefaultFormats 
  
  def createEntityTemplate():Note
  
  //def getEntity():Note = createEntityTemplate()

  class FormDeserializer[T](cls: Class[_]) {
    def createFrom(form: Form): Unit = {
      val elements = MutableList[JField]()
      form.getNames().foreach(key => elements += JField(key, JString("a")))
      val jValue: org.json4s.JsonAST.JValue = JObject(elements.toList)
      val json = parse("""{"name"="joe"}""")
      //json.extract[Note]
    }
  }

  @Get("htmlform|html")
  def getPostTemplate() = {
    val timerMetric = getMetricsCollector().timerFor(this.getClass(), "getPostTemplate");
    val templatePaths = getSkysailApplication().getTemplatePaths(this.getClass());
    val formTarget = templatePaths.stream().findFirst().orElse(".");
    val links = Arrays.asList(new Link.Builder(formTarget).build());
    //links.stream().forEach(getPathSubstitutions());
    
    val entity:Note = createEntityTemplate();
    this.setEntity(entity);
    timerMetric.stop();
    new FormResponse[Note](getResponse(), entity, links.get(0).getUri());
  }

  @Post("x-www-form-urlencoded:html")
  def post(form: Form, variant: Variant) = {
    val timerMetric = getMetricsCollector().timerFor(this.getClass(), "posthtml");
    //val entity = new FormDeserializer[T](getParameterizedType()).createFrom(form);
    //post(entity, variant);
    timerMetric.stop();

  }
}