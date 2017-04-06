package io.skysail.app.notes.repository

import io.skysail.app.notes.domain.Note
import io.skysail.server.db.DbClassName
import io.skysail.repo.orientdb._
import io.skysail.domain.core.ScalaDbRepository

class NotesRepository(db: DbService) extends OrientDbRepository[Note](db) with ScalaDbRepository {
  
//  dbService.createWithSuperClass("V", DbClassName.of(classOf[Note]));
//  dbService.register(classOf[Note]);
}