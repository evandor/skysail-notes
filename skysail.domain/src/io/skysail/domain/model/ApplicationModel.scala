package io.skysail.domain.model

import scala.collection.mutable.LinkedHashMap
import org.slf4j.LoggerFactory
import scala.collection.mutable.HashMap

/**
 * This is the root class of skysail's core domain, describing an application,
 * which aggregates entities which in turn aggregate fields.
 *
 * According to specific needs, the core domain can be adapted by extending the
 * corresponding classes. For example, there's a domain extension dealing with
 * the creation of java source files and classes according to a specific core
 * domain model.
 *
 * This core domain is, in terms of dependencies, kept as clean as possible; not
 * even lombok or a logging framework is being used.
 *
 *  @constructor create a new application model with an id and a name.
 *
 *  @param id a unique identifier, could be a full qualified class name
 *  @param name the model's (descriptive) name, for example a class's simple name
 */
case class ApplicationModel(
    val id: String,
    val name: String,
    val resources: LinkedHashMap[String, ResourceModel] = LinkedHashMap()) {

  require(id != null, "The application id should be unique and must not be null")
  require(id.trim().length() > 0, "The application id must not be empty")

  var log = LoggerFactory.getLogger(this.getClass())

  def this(id: String) = this(id, id)

  /**
   * adds an non-null resource model identified by its path.
   *
   * If a resource model with the same name exists already, this method
   * returns silently.
   *
   * Otherwise, the resource model will be added to the map of managed resources.
   */
  def addOnce(resourceModel: ResourceModel): ApplicationModel = {
    if (resources.get(resourceModel.path).isDefined) {
      log.info(s"trying to add entity ${resourceModel.path} again, ignoring")
      return this
    }
    this.copy(resources = this.resources += entityToMapEntry(resourceModel))
  }
  
  override def toString() = s"Name: ${name}, ID: ${id},\\nEntities: ${resources}"

  private def entityToMapEntry(e: ResourceModel) = e.path -> e//.copy(applicationModel = this)
  
  //    public Set<String> getEntityIds() {
  //        return entities.keySet();
  //    }
  //
  //    /**
  //     * returns the entity model for the given entity name, if existent.
  //     */
  //    public EntityModel<? extends Entity> getEntity(String entityId) {// NOSONAR
  //        return entities.get(entityId);
  //    }
  //
  //    public Collection<EntityModel<? extends Entity>> getEntityValues() {// NOSONAR
  //        return entities.values();
  //    }
  //
  //    public List<EntityModel<? extends Entity>> getRootEntities() {// NOSONAR
  //        return entities.values().stream().filter(e -> e.isAggregate()).collect(Collectors.toList());
  //    }
  //
  //    @Override
  //    public String toString() {
  //        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(": ");
  //        sb.append(name).append("\n");
  //        entitiesToString(sb);
  //        return sb.toString();
  //    }
  //
  //    protected void entitiesToString(StringBuilder sb) {
  //        if (entities.isEmpty()) {
  //            return;
  //        }
  //        sb.append("Entities: \n");
  //        entities.keySet().stream()
  //            .forEach(key -> sb.append(" * ").append(entities.get(key).toString(3)).append("\n"));
  //    }

}
