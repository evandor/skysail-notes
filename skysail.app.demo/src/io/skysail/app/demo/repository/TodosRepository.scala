package io.skysail.app.demo.repository

import io.skysail.app.demo.domain.Todo
import io.skysail.core.domain.repo.ScalaDbRepository

import io.skysail.repo.orientdb._

class TodosRepository(db: ScalaDbService) extends OrientDbRepository[Todo](db) with ScalaDbRepository {
  
  db.createWithSuperClass("V", DbClassName.of(classOf[Todo]));
  db.register(classOf[Todo]);
}