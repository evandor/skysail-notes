package io.skysail.app.notes.repository

import io.skysail.app.notes.domain.Note
import io.skysail.repo.orientdb._
import io.skysail.core.domain.repo.ScalaDbRepository

class NotesRepository(db: ScalaDbService) extends OrientDbRepository[Note](db) with ScalaDbRepository {
  
  db.createWithSuperClass("V", DbClassName.of(classOf[Note]));
  db.register(classOf[Note]);
}