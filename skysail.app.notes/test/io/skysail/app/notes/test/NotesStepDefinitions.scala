//package io.skysail.app.notes.test
//
//import cucumber.api.java.en.Given
//import io.skysail.app.notes.NotesApplication
//import io.skysail.app.notes.domain.Note
//import io.skysail.server.testsupport.cucumber.CucumberStepContext
//import io.skysail.app.notes.resources.NotesResource
//import io.skysail.app.notes.resources.NoteResource
//import io.skysail.app.notes.resources.PutNoteResource
//import io.skysail.app.notes.resources.PostNoteResource
//import java.security.Principal
//import org.restlet.Response
//import org.junit.Assert.assertThat
//import org.mockito.Mockito
//import org.restlet.Request
//import org.mockito.Matchers.any;
//import cucumber.api.java.en.When
//import collection.JavaConversions._
//import cucumber.api.java.en.Then
//import org.restlet.data.Form
//import org.mockito.Mockito._
//import io.skysail.repo.orientdb.OrientGraphDbService
//import io.skysail.core.restlet.SkysailServerResource
//import io.skysail.core.Constants
//import io.skysail.core.restlet.responses.EntityResponse
//
//class NotesStepDefinitions extends CucumberTestFixture {
//
//  var notesResource: NotesResource = null
//  var noteResource: NoteResource = null
//  var postResource: PostNoteResource = null
//  var putResource: PutNoteResource = null
//
//  var notes: java.util.List[Note] = java.util.Collections.emptyList()
//
//  //var noteResponse: EntityResponse[Note] = null
//
//  // === GIVEN ========================================================================
//
//  @Given("^a running NotesApplication$")
//  def a_running_NoteApplication(): Unit = {
//    val dbService = new OrientGraphDbService();
//    //dbService.activate(new java.util.HashMap[String,String]());
//    val app = new NotesApplication();
//    setDbService(dbService, app);
//    super.setUp(app, new CucumberStepContext(classOf[Note]));
//
//    //notesResource = setupResource(new NotesResource());
//    //noteResource = setupResource(new NoteResource());
//    //postResource = setupResource(new PostNoteResource());
//   // putResource = setupResource(new PutNoteResource());
//    
//  }
//
//  @Given("^I am logged in as '(.+)'$")
//  def i_am_logged_in_as_admin(username: String): Unit = {
//    Mockito.when(authService.getPrincipal(any(classOf[Request]))).thenReturn(new Principal() { override def getName() = username })
//  }
//
//  // === WHENS ========================================================================
//
//  @When("^I add a note like this:$")
//  def postData(data: java.util.Map[String, String]): Unit = {
//    //stepContext.post(postResource, addEntityClassIdentifier(data.toMap));
//  }
//
//  @When("^I query all notes")
//  def i_query_all_notes() = notes = null;//notesResource.getEntities(stepContext.getVariant()).getEntity()
//
//  @When("^I change its '(.+)' to '(.+)'$")
//  def i_change_its_content_to_(attribute: String, newContent: String) {
////    prepareRequest(noteResource);
////    val lastEntity = noteResource.getResource(stepContext.getVariant());
////    val form = new Form();
////    form.add(classOf[Note].getName() + "|id", lastEntity.getEntity().getId());
////    form.add(classOf[Note].getName() + "|content", lastEntity.getEntity().content);
////    prepareRequest(putResource);
////    putResource.put(stepContext.formFor(
////      classOf[Note].getName() + "|id:" + lastEntity.getEntity().getId(),
////      classOf[Note].getName() + "|content:" + newContent // ,
////      // "iban:"+lastEntity.getEntity().getIban()
////      ), stepContext.getVariant());
//  }
//
//  @When("^I open the note page$")
//  def i_open_the_note_page() {
////    prepareRequest(noteResource)
////    noteResponse = noteResource.getResource(stepContext.getVariant())
//  }
//
//  // === THENs ========================================================================
//
//  @Then("^the notes list page contains such a note:$")
//  def the_result_contains_an_account_with(data: java.util.Map[String, String]) {
//    assertThat(notes, org.hamcrest.Matchers.hasItem(CucumberTestFixture.validNoteWith(stepContext.substitute(addEntityClassIdentifier(data.toMap)), "content")));
//  }
//
//  @Then("^I get a 'Created \\((\\d+)\\)' response$")
//  def i_get_a_Created_response(statusCode: String) {
//    assertThat(stepContext.getLastResponse().toString(), org.hamcrest.Matchers.containsString(statusCode));
//  }
//
//  @Then("^I get a response containing '(.+)'$")
//  def i_get_a_certain_response(responseValue: String) {
//    assertThat(stepContext.getLastResponse().toString(), org.hamcrest.Matchers.containsString(responseValue));
//  }
//
//  @Then("^the page contains '(.+)'$")
//  def the_page_contains_theString(name: String) {
//    //assertThat(noteResponse.getEntity.content, org.hamcrest.Matchers.containsString(name));
//  }
//
//  //  @Then("^the page contains '(.+)'$")
//  //  def the_page_contains_content_xxx(data: java.util.Map[String, String]) {
//  //    val i = StepDefinitions.validNoteWith(stepContext.substitute(addEntityClassIdentifier(data.toMap)), "content")
//  //    assertThat(accountAsList(noteResponse), hasItem(i));
//  //  }
//
//  // === Privates ========================================================================
//
//  private def setDbService(dbService: OrientGraphDbService, app: NotesApplication) = {
//    try {
//      val field = classOf[NotesApplication].getDeclaredField("dbService");
//      field.setAccessible(true);
//      field.set(app, dbService);
//    } catch {
//      case e: Throwable => println(e)
//    }
//  }
//
//  private def setupResource[T <: SkysailServerResource[_]](resource: T): T = {
//    resource.setRequest(request);
//    resource.init(context, request, new Response(request));
//    return resource;
//  }
//
//  private def addEntityClassIdentifier(data: Map[String, String]) = {
//    data.map { case (key, value) => classOf[Note].getName() + Constants.CLASS_FIELD_NAMES_SEPARATOR + key -> value }
//  }
//
//  private def accountAsList(entity22: EntityResponse[Note]): java.util.List[Note] = {
//    val list = new java.util.ArrayList[Note]()
//    list.add(entity22.entity);
//    return list;
//  }
//
//}