package io.skysail.app.notes.it

import io.skysail.testsupport.ApplicationBrowser2
import org.restlet.data.MediaType
import org.restlet.data.Form
import org.json.JSONObject
import java.math.BigInteger
import org.slf4j.LoggerFactory
import io.skysail.testsupport.ApplicationClient2
import org.restlet.ext.json.JsonRepresentation

class NotesBrowser(mediaType: MediaType, port: Integer) extends ApplicationBrowser2("notes", mediaType, port) {

  private val log = LoggerFactory.getLogger(this.getClass())

  def createRandomEntity(): JSONObject = {
    val jo = new JSONObject();
    try {
      jo.put("content", "note_" + new BigInteger(130, random).toString(32));
      //jo.put("iban", "DE00000000000000000000");
    } catch {
      case _: Any =>
      //log.error(e.getMessage(),e);
    }
    return jo;
  }

  override def loginAs(username: String, password: String): NotesBrowser = {
    super.loginAs(username, password);
    this;
  }

  def create(entity: JSONObject) = {
    log.info(s"sometag: creating new AnEntity $entity")
    // login();
    createEntity(client, entity);
  }

  def createForm(x$1: String): Form = {
    ???
  }

  private def createEntity(client: ApplicationClient2, entity: JSONObject) = {
    navigateToPostEntityPage(client);
    // client.post(createForm(entity));
    client.post(new JsonRepresentation(entity));
    setId(client.getLocation().getLastSegment(true));
  }

  private def navigateToPostEntityPage(client: ApplicationClient2) {
    client.gotoAppRoot().followLinkTitle("create");
  }

}