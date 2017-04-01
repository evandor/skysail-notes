package io.skysail.restlet.app

import io.skysail.core.app.SkysailApplication
import io.skysail.core.SkysailComponent
import org.osgi.service.component.annotations._
import io.skysail.core.app.ApplicationProvider
import io.skysail.server.SkysailStatusService

import scala.collection.JavaConverters._
import org.restlet.Application
import io.skysail.server.app.SkysailRootApplication
import org.slf4j.LoggerFactory
import java.text.DecimalFormat
import scala.collection.mutable.ListBuffer

@org.osgi.annotation.versioning.ProviderType
trait ApplicationListProvider {
  def getApplications(): List[ScalaSkysailApplication]
  def attach(skysailComponent: SkysailComponent)
  def detach(skysailComponent: SkysailComponent)
}

class NoOpApplicationListProvider extends ApplicationListProvider {
  def attach(skysailComponent: SkysailComponent): Unit = {  }
  def detach(skysailComponent: SkysailComponent): Unit = {  }
  def getApplications(): List[ScalaSkysailApplication] = List()
}

object ApplicationList {
  def getApplication(provider: ScalaApplicationProvider): ScalaSkysailApplication = {
    val application = provider.getSkysailApplication();
    require(application != null, "application from applicationProvider must not be null");
    return application;
  }
  def formatSize(list: ListBuffer[_]) = new DecimalFormat("00").format(list.length)
}

@Component(immediate = true)
class ApplicationList extends ApplicationListProvider {

  val applications = scala.collection.mutable.ListBuffer[ScalaSkysailApplication]();
  val log = LoggerFactory.getLogger(classOf[ApplicationList])

  var skysailComponent: SkysailComponent = null

  def attach(skysailComponent: SkysailComponent): Unit = {
    this.skysailComponent = skysailComponent;
    if (skysailComponent == null) {
      return ;
    }
    getApplications().foreach { a => attachToComponent(a) }
  }

  def detach(skysailComponent: SkysailComponent): Unit = {
    this.skysailComponent = skysailComponent;
    if (skysailComponent == null) {
      return ;
    }
    getApplications().foreach(app => detach(app, skysailComponent))
  }

  def getApplications(): List[ScalaSkysailApplication] = applications.toList

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
  def addApplicationProvider(provider: ScalaApplicationProvider): Unit = {
    val application = ApplicationList.getApplication(provider);
    application.setStatusService(new SkysailStatusService());
    checkExistingApplications(application);
    applications += application
    attachToComponent(application);
    log.info("(+ ApplicationModel) (#{}) with name '{}'", ApplicationList.formatSize(applications), application.getName(): Any);
  }

  def removeApplicationProvider(provider: ScalaApplicationProvider): Unit = {
    val application = ApplicationList.getApplication(provider);
    detachFromComponent(application);
    applications -= application
    log.info("(- ApplicationModel) name '{}', count is {} now", application.getName(), ApplicationList.formatSize(applications): Any);
  }

  def detachFromComponent(application: ScalaSkysailApplication): Unit = {
    if (skysailComponent == null) {
      return ;
    }
    if (skysailComponent.getDefaultHost() != null) {
      skysailComponent.getDefaultHost().detach(application);
    }
    if (skysailComponent.getInternalRouter() != null) {
      skysailComponent.getInternalRouter().detach(application);
    }
  }

  def checkExistingApplications(application: ScalaSkysailApplication): Unit = {
    if (applications.filter(a => a.getName().equals(application.getName())).headOption.isDefined) {
      log.error("about to add application '{}' the second time!", application.getName());
      throw new IllegalStateException("application was added a second time!");
    }
  }

  private def attachToComponent(application: Application): Unit = {
    if (skysailComponent == null) {
      log.debug("could not attach application '{}' to component, as component not (yet) available", application.getName)
      return ;
    }
    //    if (application.isInstanceOf[SkysailRootApplication]) {
    //      rootApplication = (SkysailRootApplication) application;
    //    }
    val skysailApplication = application.asInstanceOf[ScalaSkysailApplication];
    // http://stackoverflow.com/questions/6810128/restlet-riap-protocol-deployed-in-java-app-server
    skysailComponent.getDefaultHost().attach("/" + skysailApplication.getName(), application);
    skysailComponent.getInternalRouter().attach("/" + skysailApplication.getName(), application);
    // skysailComponent.getServers().add(riapServer);

    //    if (rootApplication != null) {
    //      skysailComponent.getDefaultHost().attachDefault(rootApplication);
    //    }
  }

  private def detach(app: ScalaSkysailApplication, restletComponent: SkysailComponent) = {
    log.debug(" >>> unsetting ServerConfiguration");
    // TODO make nicer
    log.debug(" >>> attaching skysailApplication '{}' to defaultHost", "/" + app.getName());
    if (app.getName() != null) {
      // http://stackoverflow.com/questions/6810128/restlet-riap-protocol-deployed-in-java-app-server
      restletComponent.getDefaultHost().detach(app);
      restletComponent.getInternalRouter().detach(app);
      //restletComponent.getServers().remove(riapServer);
    } else {
      // http://stackoverflow.com/questions/6810128/restlet-riap-protocol-deployed-in-java-app-server
      restletComponent.getDefaultHost().detach(app);
      restletComponent.getInternalRouter().detach(app);
    }
  }

}
