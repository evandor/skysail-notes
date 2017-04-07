package io.skysail.app.notes.repository

import io.skysail.app.notes.domain.Note
import io.skysail.domain.repo.ScalaDbRepository;
import io.skysail.repo.orientdb._

class NotesRepository(db: ScalaDbService) extends OrientDbRepository[Note](db) with ScalaDbRepository {
  
  db.createWithSuperClass("V", DbClassName.of(classOf[Note]));
  db.register(classOf[Note]);
}