package io.skysail.app.wyt.repository

import io.skysail.core.domain.repo.ScalaDbRepository
import io.skysail.repo.orientdb._
import io.skysail.app.wyt.domain.Pact
import io.skysail.app.wyt.domain.Turn

class WytRepository(db: ScalaDbService) extends OrientDbRepository[Pact](db) with ScalaDbRepository {
  
  db.createWithSuperClass("V", DbClassName.of(classOf[Pact]), DbClassName.of(classOf[Turn]));
  db.register(classOf[Pact], classOf[Turn]);
}