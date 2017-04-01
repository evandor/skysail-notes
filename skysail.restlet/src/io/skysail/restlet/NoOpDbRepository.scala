package io.skysail.restlet

import io.skysail.domain.core.repos.DbRepository
import io.skysail.domain.Entity
import java.util.Optional
import io.skysail.domain.core.ApplicationModel

class NoOpDbRepository[T <: DbRepository] extends DbRepository {
  def delete(x$1: Entity): Unit = {
    ???
  }

  def findOne(x$1: String): Entity = {
    ???
  }

  def findOne(x$1: String, x$2: String): Optional[Entity] = {
    ???
  }

  def getRootEntity(): Class[_ <: io.skysail.domain.Entity] = {
    ???
  }

  def save(x$1: Entity, x$2: ApplicationModel): Object = {
    ???
  }

  def update(x$1: Entity, x$2: ApplicationModel): Object = {
    ???
  }
}