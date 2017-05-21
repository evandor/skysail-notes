package io.skysail.app.wyt.it

import org.restlet.data._
import org.json.JSONObject
import java.math.BigInteger
import org.slf4j.LoggerFactory
import org.restlet.ext.json.JsonRepresentation
import io.skysail.testsupport._
import java.util.Random
import io.skysail.app.wyt.domain._
import org.restlet.representation.Representation

import ScalaApplicationClient.{TESTTAG => logPrefix} 

class WytBrowser(mediaType: MediaType, port: Integer) extends ScalaApplicationBrowser("wyt", mediaType, port) {

  private val log = LoggerFactory.getLogger(this.getClass())
  private val random = new Random()
  
  def getNextTurn(): Turn = {
    log.info(s"$logPrefix getting next turn")
    navigateToTurnResourceEndpoint()
    //val r = client.get()
    println("xxx: "+ client.currentRepresentation.getText())
    Turn(Some("1"))
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

  def postConfirmation(): Unit = {
    log.info(s"posting confirmation")
    //createEntity(client, entity)
    createWithForm(client, Confirmation(None))
  }

  private def createEntity(client: ScalaApplicationClient, entity: JSONObject) = {
    navigateToPostEntityPage(client)
    // client.post(createForm(entity))
    client.post(new JsonRepresentation(entity), MediaType.APPLICATION_JSON)
    setId(client.getLocation().getLastSegment(true))
  }

  private def navigateToPostEntityPage(client: ScalaApplicationClient) {
    client.gotoAppRoot().followLinkTitle("create")
  }

  private def createWithForm(client: ScalaApplicationClient, entity: Confirmation): Representation = {
    navigateToPostEntityPage(client)
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

  private def navigateToTurnResourceEndpoint() = {
    client.gotoAppRoot().followLinkTitle("show");
  }

}