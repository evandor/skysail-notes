package io.skysail.app.wyt.repository

import io.skysail.domain.repo.ScalaDbRepository;
import io.skysail.repo.orientdb._
import io.skysail.app.wyt.domain.Pact

class NotesRepository(db: ScalaDbService) extends OrientDbRepository[Pact](db) with ScalaDbRepository {
  
  db.createWithSuperClass("V", DbClassName.of(classOf[Pact]));
  db.register(classOf[Pact]);
}