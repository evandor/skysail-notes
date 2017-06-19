package io.skysail.app.checklists

import io.skysail.repo.orientdb._
import io.skysail.core.domain.repo.ScalaDbRepository

class ChecklistsRepository(db: ScalaDbService) extends OrientDbRepository[Checklist](db) with ScalaDbRepository {
  db.createWithSuperClass("V", DbClassName.of(classOf[Checklist]))
  db.register(classOf[Checklist]);
}