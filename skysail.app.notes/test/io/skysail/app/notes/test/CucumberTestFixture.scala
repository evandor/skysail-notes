package io.skysail.app.notes.test

import org.mockito.Mockito._

import org.mockito.Mock
import io.skysail.core.app.ServiceListProvider
import io.skysail.api.um.AuthenticationService
import java.util.concurrent.ConcurrentMap
import io.skysail.api.um.AuthorizationService
import org.restlet.security.Authenticator
import org.restlet.Request
import org.restlet.data.Reference
import org.restlet.Context
import io.skysail.core.app.SkysailApplication
import io.skysail.core.resources.SkysailServerResource
import io.skysail.server.testsupport.cucumber.CucumberStepContext
import io.skysail.app.notes.NotesApplication
import org.mockito.MockitoAnnotations
import java.security.Principal
import io.skysail.api.um.AuthenticationMode
import io.skysail.api.validation.DefaultValidationImpl
import io.skysail.api.metrics.NoOpMetricsCollector
import java.util.concurrent.ConcurrentHashMap
import io.skysail.core.app.ApplicationConfiguration
import org.osgi.service.component.ComponentContext
import org.osgi.service.cm.ConfigurationException
import org.slf4j.LoggerFactory
import org.restlet.data.ClientInfo
import org.restlet.Response
import org.hamcrest.Matcher
import io.skysail.app.notes.domain.Note
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.Description
import java.util.Arrays
import io.skysail.core.app.SkysailApplicationService
import io.skysail.core.model.SkysailEntityModel
import io.skysail.core.model.SkysailEntityModel
import io.skysail.core.app.ApplicationListProvider
import io.skysail.core.app.ApplicationList

object CucumberTestFixture {
 def validNoteWith(data: java.util.Map[String, String], keys:String*): Matcher[Note] = {
   
        return new TypeSafeMatcher[Note]() {

            override def describeTo(desc: Description) {
                desc.appendText("expected result: account with non-null id, creationDate");
//                Arrays.stream(keys).forEach(key -> {
//                    desc.appendText(", " + key + " = ")
//                        .appendValue(data.get(key));
//                });
            }

            override def matchesSafely(note: Note):Boolean = {
                if (note.getId() == null) {
                    return false;
                }
                if (!note.content.equals(data.get(classOf[Note].getName() + "|content"))) {
                    return false;
                }
                true;
            }
        }
    }  
}

class CucumberTestFixture {

  @Mock var serviceListProvider: ServiceListProvider = null
  @Mock var authService: AuthenticationService = null
  @Mock var authorizationService: AuthorizationService = null
  @Mock var authenticator: Authenticator = null
  @Mock var request: Request = null
  @Mock var resourceRef: Reference = null
  @Mock var targetRef: Reference = null

  var requestAttributes: ConcurrentMap[String, Object] = null
  var context: Context = null
  var application: SkysailApplication = null
  var resource: SkysailServerResource[_] = null
  var stepContext: CucumberStepContext = null

  def setUp(app: NotesApplication, stepContext: CucumberStepContext): Unit = {
    val log = LoggerFactory.getLogger(classOf[CucumberTestFixture])
    this.stepContext = stepContext;

    MockitoAnnotations.initMocks(this);

    this.application = app;
    val context = new Context();
    val principal = new Principal() { override def getName() = "admin" }
    when(authService.getPrincipal(org.mockito.Matchers.any(classOf[Request]))).thenReturn(principal)
    when(authService.getApplicationAuthenticator(context, AuthenticationMode.AUTHENTICATED)).thenReturn(authenticator)
    
    when(authService.getApplicationAuthenticator(context, AuthenticationMode.ANONYMOUS)).thenReturn(authenticator)
    
    when(serviceListProvider.getAuthenticationService()).thenReturn(authService);
    when(serviceListProvider.getAuthorizationService()).thenReturn(authorizationService);
    when(serviceListProvider.getValidatorService()).thenReturn(new DefaultValidationImpl());
    when(serviceListProvider.getMetricsCollector()).thenReturn(new NoOpMetricsCollector());
    
    
    val skysailApplicationService = new SkysailApplicationService()//mock(classOf[SkysailApplicationService])
    //val entityModel: SkysailEntityModel[Note] = Mockito.mock(classOf[SkysailEntityModel[Note]])//new SkysailEntityModel[Note](null, classOf[Note],putResource)
    //when(skysailApplicationService.getEntityModel(classOf[Note].getName())).thenReturn(entityModel)
    when(serviceListProvider.getSkysailApplicationService()).thenReturn(skysailApplicationService)
    
    val applicationListProvider = new ApplicationList();
    applicationListProvider.addApplicationProvider(application)
    
    val applicationListProviderField = classOf[SkysailApplicationService].getDeclaredField("applicationListProvider")
    applicationListProviderField.setAccessible(true)
    applicationListProviderField.set(skysailApplicationService, applicationListProvider)

        
    requestAttributes = new ConcurrentHashMap[String, Object]()
    SkysailApplication.setServiceListProvider(serviceListProvider)

    try {
      val appConfig = mock(classOf[ApplicationConfiguration]);
      val componentContext = mock(classOf[ComponentContext]);
      application.activate(appConfig, componentContext);
    } catch {
      case e: Throwable => log.error(e.getMessage, e)
    }

    application.setContext(context);
    application.createInboundRoot();

    org.restlet.Application.setCurrent(application);

    when(resourceRef.getTargetRef()).thenReturn(targetRef)
    when(request.getResourceRef()).thenReturn(resourceRef)
    when(request.getAttributes()).thenReturn(requestAttributes)
    when(request.getClientInfo()).thenReturn(new ClientInfo())
    
  }

  def prepareRequest(resource: SkysailServerResource[_]): Unit = {
    val entity = stepContext.getLastResponse().getEntity().asInstanceOf[Note]
    val id = entity.getId().toString();
    requestAttributes.put("id", id.replace("#", ""));
    resource.init(context, request, new Response(request));
  }

//  // <T extends SkysailServerResource<?>> T 
//  def setupResource[T <: SkysailServerResource[_]](resource: T) = {
//    resource.setRequest(request);
//    resource.init(context, request, new Response(request));
//    resource;
//  }
//
//  //    def addEntityClassIdentifier(data: java.util.Map<String, String>): java.util.Map<String, String>  {
//  //        return data.entrySet().stream().collect(Collectors.<Map.Entry, String, String> toMap(
//  //                e -> Account.class.getName() + Constants.CLASS_FIELD_NAMES_SEPARATOR + e.getKey(), e -> e.getValue().toString()));
//  //    }
}