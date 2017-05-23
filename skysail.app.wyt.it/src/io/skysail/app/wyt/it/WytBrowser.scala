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
//import io.skysail.app.wyt.it.routes._

import ScalaApplicationClient.{ TESTTAG => logPrefix }

class WytBrowser(port: Integer) extends ScalaApplicationBrowser("wyt", port) {

  private val log = LoggerFactory.getLogger(this.getClass())
  private val random = new Random()

  def getPacts(mediaType: MediaType = MediaType.APPLICATION_JSON): Representation = {
    log.info(s"$logPrefix getting pacts")
    //client.gotoRoot().followLinkTitle(appName, mediaType)
    
    //GET on "foo" / * to Application.show
    //val foo = GET.on( "foo"./(*) ).to(Application.shows)
  //  GET root followLink appName
//    val pactsPath = Root --> appName// --> "test"
//    val rep = pactsPath.get(mediaType)
    
    //val home  = GET   on root       to Application.index _
    
    client.gotoAppRoot(mediaType)
    client.currentRepresentation
  }

  def getPostPacts(mediaType: MediaType = MediaType.APPLICATION_JSON): Representation = {
    log.info(s"$logPrefix getting pacts")
    client.gotoAppRoot(mediaType).followLinkTitle("post pact", mediaType)
    client.currentRepresentation
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

  private def createForm(entity: Pact): Form = {
    val form = new Form()
    form.add("io.skysail.app.wyt.domain.Pact|title", entity.getTitle())
    form
  }

  private def getTurn() = {
    client.gotoAppRoot().followLinkTitle("turn")
    client.currentRepresentation
  }

  private def navigateToPostEntityPage(client: ScalaApplicationClient) {
    client.gotoAppRoot().followLinkTitle("post confirmation")
  }

  private def navigateToPostPactPage(client: ScalaApplicationClient) {
    client.gotoAppRoot().followLinkTitle("post pact")
  }

}