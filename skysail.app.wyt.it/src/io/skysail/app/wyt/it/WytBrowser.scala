package io.skysail.app.wyt.it

import org.restlet.data._
import org.restlet.ext.json.JsonRepresentation
import org.json.JSONObject
import io.skysail.testsupport._
import io.skysail.app.wyt.domain._
import java.math.BigInteger
import java.util.Random
import org.slf4j.LoggerFactory
import org.restlet.representation.Representation
import io.skysail.testsupport.PathDsl._
import org.json4s._
import org.json4s.native.JsonMethods._

import ScalaApplicationClient.{ TESTTAG => logPrefix }
import org.json4s.DefaultFormats

class WytBrowser(port: Integer) extends ScalaApplicationBrowser("wyt", port) {

  private val log = LoggerFactory.getLogger(this.getClass())
  private val random = new Random()
  private implicit val formats = DefaultFormats

  def getPacts(mediaType: MediaType = MediaType.APPLICATION_JSON) = {
    log.info(s"$logPrefix getting pacts")
    client.get("/" --> appName, mediaType)
  }

  def getPostPacts(mediaType: MediaType = MediaType.APPLICATION_JSON): Representation = {
    log.info(s"$logPrefix getting pacts")
    client.get("/" --> appName --> "post pact", mediaType)
  }

  def postToPostPacts(pact: Pact, mediaType: MediaType = MediaType.APPLICATION_JSON) = {
    log.info(s"$logPrefix posting form to pacts")
    val rep = client.post(createForm(pact), "/" --> appName --> "post pact", mediaType).getText
    parse(rep).extract[Pact]
  }

  def postToPostCars(car: Car, mediaType: MediaType = MediaType.APPLICATION_JSON) = {
    log.info(s"$logPrefix posting form to cars")
    val rep = client.post(createForm(car), "/" --> appName --> "post car", mediaType).getText
    parse(rep).extract[Car]
  }

  def getNextTurn(mediaType: MediaType = MediaType.APPLICATION_JSON): Representation = {
    log.info(s"$logPrefix getting next turn")
    getTurn()
  }

  def postConfirmation(): Unit = {
    log.info(s"$logPrefix posting confirmation")
    createWithForm(client, Confirmation(None))
  }

  def createPact() = {
    log.info(s"$logPrefix posting new pact")
    createWithForm(client, Pact(None, "test pact"))
  }

  def createRandomEntity(): JSONObject = {
    val jo = new JSONObject()
    try {
      jo.put("content", "note_" + new BigInteger(130, random).toString(32))
      //jo.put("iban", "DE00000000000000000000")
    } catch {
      case _: Any =>
      //log.error(e.getMessage(),e)
    }
    return jo
  }

  override def loginAs(username: String, password: String): WytBrowser = {
    super.loginAs(username, password)
    this
  }

  def create(entity: JSONObject) = {
    log.info(s"sometag: creating new AnEntity $entity")
    // login()
    createEntity(client, entity)
  }

  def createForm(x$1: String): Form = {
    ???
  }

  private def createEntity(client: ScalaApplicationClient, entity: JSONObject) = {
    navigateToPostEntityPage(client)
    // client.post(createForm(entity))
    client.post(new JsonRepresentation(entity), MediaType.APPLICATION_JSON)
    setId(client.getLocation().getLastSegment(true))
  }

  private def createWithForm(client: ScalaApplicationClient, entity: Confirmation): Representation = {
    navigateToPostEntityPage(client)
    val form = createForm(entity)
    val result = client.post(form, MediaType.APPLICATION_WWW_FORM)
    val loc = client.getLocation()
    if (loc != null) setId(loc.getLastSegment(true))
    return result
  }

  private def createWithForm(client: ScalaApplicationClient, entity: Pact): Representation = {
    navigateToPostPactPage(client)
    val form = createForm(entity)
    val result = client.post(form, MediaType.APPLICATION_WWW_FORM)
    val loc = client.getLocation()
    if (loc != null) setId(loc.getLastSegment(true))
    return result
  }

  private def createForm(entity: Confirmation): Form = {
    val form = new Form()
    //form.add("io.skysail.app.notes.domain.Note|content", entity.getContent())
    form
  }

  private def getTurn() = {
    client.gotoAppRoot().followLinkTitle(Method.GET,"", "turn")
    client.currentRepresentation
  }

  private def navigateToPostEntityPage(client: ScalaApplicationClient) {
    client.gotoAppRoot().followLinkTitle(Method.GET,"", "post confirmation")
  }

  private def navigateToPostPactPage(client: ScalaApplicationClient) {
    client.gotoAppRoot().followLinkTitle(Method.GET,"", "post pact")
  }
  
  private def createForm(pact: Pact) = {
    val form = new Form()
    form.add("io.skysail.app.wyt.domain.Pact|title", pact.title)
    form
  }

  private def createForm(car: Car) = {
    val form = new Form()
    form.add("io.skysail.app.wyt.domain.Car|model", car.model)
    form.add("io.skysail.app.wyt.domain.Car|year", "12")
    form
  }
}