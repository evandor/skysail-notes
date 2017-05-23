package io.skysail.app.notes.it

import org.restlet.data.MediaType
import org.restlet.data.Form
import org.json.JSONObject
import java.math.BigInteger
import org.slf4j.LoggerFactory
import org.restlet.ext.json.JsonRepresentation
import io.skysail.testsupport.ScalaApplicationBrowser
import io.skysail.testsupport.ScalaApplicationClient
import java.util.Random
import io.skysail.app.notes.domain.Note
import org.restlet.representation.Representation

class NotesBrowser(port: Integer) extends ScalaApplicationBrowser("notes", port) {

  private val log = LoggerFactory.getLogger(this.getClass())

  private val random = new Random()

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

  def postForm(entity: Note): Representation = {
    log.info(s"{}creating new AnEntity $entity");
    createWithForm(client, entity);
  }

  def createForm(entity: Note): Form = {
    val form = new Form()
    //[[io.skysail.app.notes.domain.Note|content=test8], [io.skysail.app.notes.domain.Note|created=], [io.skysail.app.notes.domain.Note|modified=], [submit=submit]]
    form.add("io.skysail.app.notes.domain.Note|content", entity.getContent())
    form
  }

  private def createEntity(client: ScalaApplicationClient, entity: JSONObject) = {
    navigateToPostEntityPage(client);
    // client.post(createForm(entity));
    client.post(new JsonRepresentation(entity), MediaType.APPLICATION_JSON);
    setId(client.getLocation().getLastSegment(true));
  }

  private def navigateToPostEntityPage(client: ScalaApplicationClient) {
    client.gotoAppRoot().followLinkTitle("create");
  }

  private def createWithForm(client: ScalaApplicationClient, entity: Note): Representation = {
    navigateToPostEntityPage(client);
    val form = createForm(entity)
    val result = client.post(form, MediaType.APPLICATION_WWW_FORM)
    val loc = client.getLocation()
    if (loc != null) setId(loc.getLastSegment(true));
    return result;
  }

}