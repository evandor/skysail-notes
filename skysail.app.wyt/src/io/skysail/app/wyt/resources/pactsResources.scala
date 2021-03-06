package io.skysail.app.wyt.resources

import io.skysail.app.wyt.domain.Pact
import io.skysail.api.doc._
import org.json4s.DefaultFormats
import io.skysail.core.restlet.resources.ListServerResource
import io.skysail.core.restlet.ResourceContextId
import io.skysail.core.restlet.resources.PostEntityServerResource
import io.skysail.app.wyt.services.Services
import io.skysail.queryfilter.filter.Filter
import io.skysail.queryfilter.pagination.Pagination
import io.skysail.app.wyt.domain.Turn
import io.skysail.core.restlet.responses.ScalaSkysailResponse
import org.restlet.representation.Variant
import org.restlet.resource.Post
import org.restlet.data.Form
import io.skysail.core.restlet.transformations.Transformations
import org.json4s.jackson.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{ read, write }
import org.json4s.JsonAST.JObject
import io.skysail.app.wyt.domain.Car

class PactsResource extends ListServerResource[List[Pact]]() {

  setDescription("""resource dealing with retrieving all configured pacts""")

  addToContext(ResourceContextId.LINK_TITLE, "wyt")

  @ApiSummary("list of all pacts")
  override def getEntity() = {
    Services.pacts.find(new Filter(getRequest()), new Pagination(getRequest(), getResponse()))
    //associatedResourceClasses(Array(classOf[TurnResource]))
  }
  override def linkedResourceClasses() = List(
    /*classOf[TurnResource], */ 
    classOf[PostConfirmationResource], 
    classOf[PostPactResource],
    classOf[PostCarResource]
  )

}

class PostPactResource extends PostEntityServerResource[Pact] {

  setDescription("""resource dealing with posting pacts""")
  addToContext(ResourceContextId.LINK_TITLE, "post pact")

  def createEntityTemplate() = Pact(None, "initial", new Turn("initial"))

  @ApiSummary("returns a pact template")
  def addEntity(entity: Pact): Pact = {
    //Services.turns.confirm(entity)
    val pact = Services.pacts.create(entity)
    //Pact(Some("1"), "default pact")
    pact.get
  }

  @Post("x-www-form-urlencoded:html|json")
  override def post(form: Form, variant: Variant): ScalaSkysailResponse[Pact] = {
    //form.add("io.skysail.app.wyt.domain.Turn|nextTurn","i")
    implicit val formats = DefaultFormats
    val timerMetric = getMetricsCollector().timerFor(this.getClass(), "posthtml")
    val json = if (form != null) {
      val j = Transformations.jsonFrom[Pact](form)
      //val p = j ++ JObject()
      println(pretty(render(j)))
      j.extract[Pact]
    } else {
      null.asInstanceOf[Pact]
    }
    val result = jsonPost(json, variant)
    timerMetric.stop()
    result
  }

}

class CarsResource extends ListServerResource[List[Car]]() {

  setDescription("""resource dealing with retrieving all configured pacts""")

  addToContext(ResourceContextId.LINK_TITLE, "wyt")

  @ApiSummary("list of all Cars")
  override def getEntity() = {
    Services.cars.find(new Filter(getRequest()), new Pagination(getRequest(), getResponse()))
    //associatedResourceClasses(Array(classOf[TurnResource]))
  }
  override def linkedResourceClasses() = List(
    /*classOf[TurnResource], */ classOf[PostConfirmationResource], classOf[PostPactResource])

}

class PostCarResource extends PostEntityServerResource[Car] {
  
  addToContext(ResourceContextId.LINK_TITLE, "post car")  

  def createEntityTemplate() = Car("model","20",None)

  @ApiSummary("returns a pact template")
  def addEntity(entity: Car): Car = {
    //Services.turns.confirm(entity)
    val pact = Services.cars.create(entity)
    //Pact(Some("1"), "default pact")
    pact.get
  }
}

