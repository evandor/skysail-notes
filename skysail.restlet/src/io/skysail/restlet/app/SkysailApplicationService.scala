package io.skysail.restlet.app

import org.osgi.service.component.annotations._
import io.skysail.core.app.SkysailApplicationService
import java.util.ArrayList
import io.skysail.server.services.EntityApi
import io.skysail.restlet.model.ScalaSkysailEntityModel

import scala.collection.JavaConverters._
import io.skysail.restlet.model.ScalaSkysailEntityModel
import io.skysail.restlet.model.ScalaSkysailEntityModel

@Component(immediate = true, service = Array(classOf[ScalaSkysailApplicationService]))
class ScalaSkysailApplicationService {

  var entityApis: java.util.List[EntityApi[_]] = new java.util.ArrayList[EntityApi[_]]()

  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  var applicationListProvider: ApplicationListProvider = null

  @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
  def addEntityApi(api: EntityApi[_]): Unit = entityApis.add(api)

  def removeEntityApi(api: EntityApi[_]): Unit = entityApis.remove(api)

  //      public String pathForEntityResource(String className, String type) {
  //        for (SkysailApplication app : applicationListProvider.getApplications()) {
  //            Optional<EntityModel<? extends Entity>> entity = app.getApplicationModel().getEntityValues().stream()
  //                    .filter(e -> e.getId().equals(className))
  //                    .findFirst();
  //            if (entity.isPresent()) {
  //                SkysailEntityModel sem = (SkysailEntityModel) entity.get();
  //                Class postResourceClass = sem.getPostResourceClass();
  //                List<RouteBuilder> routeBuilders = app.getRouteBuildersForResource(postResourceClass);
  //                if (routeBuilders.size() > 0) {
  //                    return "/" + app.getName() + "/" + app.getApiVersion() + routeBuilders.get(0).getPathTemplate();
  //                }
  //            }
  //        }
  //        return "";
  //    }
  //
  //    public EntityApi<?> getEntityApi(String entityName) {
  //        return entityApis.stream()
  //                .filter(api -> api.getEntityClass().getName().equals(entityName))
  //                .findFirst()
  //                .orElse(new NoOpEntityApi());
  //    }
  //
  //    public SkysailApplicationModel getApplicationModel(String appName) {
  //        return applicationListProvider.getApplications().stream()
  //                .filter(a -> a.getName().equals(appName))
  //                .map(SkysailApplication::getApplicationModel)
  //                .findFirst().orElse(new SkysailApplicationModel("unknown"));
  //    }
  //
  def getEntityModel(name: String): ScalaSkysailEntityModel = {
    getEntityValues
      .filter { entityModel => entityModel.getId.equals(name) }
      .map { e => e.asInstanceOf[ScalaSkysailEntityModel] }
      .head

  }

  def getEntityModels(): List[ScalaSkysailEntityModel] = {
    getEntityValues()
      .map { e => e.asInstanceOf[ScalaSkysailEntityModel] }
      .toList
  }
  
  private def getEntityValues() = {
     applicationListProvider.getApplications()
      .map { a => a.getApplicationModel() }
      .map { m => m.getEntityValues }
      .flatten { e => e.asScala }
  }

}