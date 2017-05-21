package io.skysail.app.notes.resources

import io.skysail.core.ApiVersion
import io.skysail.app.notes.NotesApplication
import io.skysail.core.app.SkysailApplication
import io.skysail.core.app.ScalaServiceListProvider
import io.skysail.api.um._
import io.skysail.core.app.ApplicationConfiguration
import io.skysail.repo.orientdb._
import io.skysail.app.notes.domain.Note
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.slf4j.LoggerFactory
import org.mockito._
import org.mockito.Mockito.{ mock, when }
import org.mockito.MockitoAnnotations
import org.restlet.security.Authenticator
import org.osgi.service.component.ComponentContext
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{ read, write }
import org.restlet.Context
import org.restlet.Request
import org.restlet.Response
import scala.util.Try
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import com.tinkerpop.blueprints.impls.orient.OrientElement

@RunWith(classOf[JUnitRunner])
class NotesResourceSpec extends FlatSpec with BeforeAndAfterEach {

  val serviceListProvider = mock(classOf[ScalaServiceListProvider])
  val authenticationService = mock(classOf[AuthenticationService])
  val authorizationService = mock(classOf[AuthorizationService])

  val dbService = mock(classOf[ScalaDbService])

  var notesResource: NotesResource = _

  implicit val formats = Serialization.formats(NoTypeHints)
  val testNote = parse(write(Note(Some("1"), "test")))

  var app: NotesApplication = _
  
  var context: Context = _
  var request: Request = _
  var response: Response = _

  var requestAttributes:java.util.concurrent.ConcurrentMap[String,Object] = _
  
  override def beforeEach() {
    context = new org.restlet.Context()
    request = mock(classOf[Request])
    response = mock(classOf[Response])
    app = new NotesApplication()
    app.setContext(context)
    app.dbService = dbService
    app.activate(mock(classOf[ApplicationConfiguration]), mock(classOf[ComponentContext]))

    val resourceAuthenticator = mock(classOf[Authenticator])
    val applicationAuthenticator = mock(classOf[Authenticator])
    when(authenticationService.getResourceAuthenticator(context, AuthenticationMode.PERMIT_ALL)).thenReturn(resourceAuthenticator)
    when(authenticationService.getApplicationAuthenticator(context, AuthenticationMode.ANONYMOUS)).thenReturn(applicationAuthenticator)

    when(serviceListProvider.getAuthenticationService()).thenReturn(authenticationService)
    //Mockito.when(serviceListProvider.getAuthorizationService()).thenReturn(authorizationService)
    SkysailApplication.setServiceListProvider(serviceListProvider);

    app.createInboundRoot();
    org.restlet.Application.setCurrent(app)
    
    requestAttributes = new java.util.concurrent.ConcurrentHashMap()

    request = mock(classOf[Request])
    
    when(request.getAttributes).thenReturn(requestAttributes)
  }

  "A NotesResource" should "contain a link to the PostNoteResource" in {
    val classes = new NotesResource().linkedResourceClasses()
    assert(classes.contains(classOf[PostNoteResource]))
  }

  "A NotesResource" should "provide the notes from the repository" in {
    Mockito.when(dbService.findGraphs(classOf[Note], "SELECT * from io_skysail_app_notes_domain_Note", Map())).thenReturn(List(testNote))
    val notes = new NotesResource().getEntity()
    assert(notes.size == 1)
    assert(notes.head.content == "test")
  }

  "A NoteResource" should "provide a note referenced by its id" in {
    when(dbService.findOne("1")).thenReturn(Some(testNote))
    val noteResource = new NoteResource()
    requestAttributes.put("id","1")
    noteResource.init(context, request, response)
    val entity = noteResource.getEntity()
    assert(entity.content == "test")
  }
  
  "A PostNoteResource" should "" in {
    val postNote = Note(None,"postNote")
    val dummy = mock(classOf[OrientVertex])
    when(dummy.getId()).thenReturn("2", Nil: _*)
    when(dbService.persist(postNote, app.getApplicationModel2())).thenReturn(Try(dummy))
    val postNoteResource = new PostNoteResource()
    requestAttributes.put("id","1")
    postNoteResource.init(context, request, response)
    val addedEntity = postNoteResource.addEntity(postNote)
    assert(addedEntity.id.isDefined)
  }

}