package io.skysail.restlet.app

import org.osgi.service.component.annotations._
import java.util.ArrayList
import io.skysail.core.model.SkysailEntityModel
import scala.collection.JavaConverters._
import io.skysail.restlet.services.EntityApi

@Component(immediate = true, service = Array(classOf[SkysailApplicationService]))
class SkysailApplicationService {

  var entityApis: java.util.List[EntityApi] = new java.util.ArrayList[EntityApi]()

  /** --- mandatory reference ---------------------------*/
  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  var applicationListProvider: ApplicationListProvider = null

  @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
  def addEntityApi(api: EntityApi): Unit = entityApis.add(api)

  def removeEntityApi(api: EntityApi): Unit = entityApis.remove(api)

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
  def getEntityModel(name: String): SkysailEntityModel = {
    getEntityValues
      .filter { entityModel => entityModel.getId.equals(name) }
      .map { e => e.asInstanceOf[SkysailEntityModel] }
      .head

  }

  def getEntityModels(): List[SkysailEntityModel] = {
    getEntityValues()
      .map { e => e.asInstanceOf[SkysailEntityModel] }
      .toList
  }
  
  private def getEntityValues() = {
     applicationListProvider.getApplications()
      .map { a => a.getApplicationModel() }
      .map { m => m.getEntityValues }
      .flatten { e => e.asScala }
  }

}