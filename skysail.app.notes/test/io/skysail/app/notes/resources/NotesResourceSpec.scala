package io.skysail.app.notes.resources

import collection.mutable.Stack
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.slf4j.LoggerFactory
import io.skysail.core.ApiVersion
import io.skysail.app.notes.NotesApplication
import io.skysail.restlet.app.SkysailApplication
import org.mockito._
import org.mockito.MockitoAnnotations
import io.skysail.restlet.app.ScalaServiceListProvider
import io.skysail.api.um.AuthenticationService
import io.skysail.api.um.AuthorizationService
import io.skysail.api.um.AuthenticationMode
import org.restlet.security.Authenticator

@RunWith(classOf[JUnitRunner])
class NotesResourceSpec extends FlatSpec {

  val serviceListProvider = Mockito.mock(classOf[ScalaServiceListProvider])
  val authenticationService = Mockito.mock(classOf[AuthenticationService])
  val authorizationService = Mockito.mock(classOf[AuthorizationService])

  /*"A NotesResource" should "contain a link to the PostNoteResource" in {
    val classes = new NotesResource().linkedResourceClasses()
    assert(classes.contains(classOf[PostNoteResource]))
  }*/

  "A NotesResource" should "" in {

    val context = new org.restlet.Context()
    val app = new NotesApplication()
    app.setContext(context)
    
    val resourceAuthenticator = Mockito.mock(classOf[Authenticator])
    Mockito.when(authenticationService.getResourceAuthenticator(context, AuthenticationMode.ANONYMOUS)).thenReturn(resourceAuthenticator)

    Mockito.when(serviceListProvider.getAuthenticationService()).thenReturn(authenticationService)
    //Mockito.when(serviceListProvider.getAuthorizationService()).thenReturn(authorizationService)
    SkysailApplication.setServiceListProvider(serviceListProvider);

    app.createInboundRoot();
    org.restlet.Application.setCurrent(app)

    val notesResource = new NotesResource()
    val notes = notesResource.getEntity()
    assert(notes.size == 0)
  }
}