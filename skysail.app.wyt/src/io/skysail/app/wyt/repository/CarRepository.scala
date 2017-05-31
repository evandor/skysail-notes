package io.skysail.app.wyt.repository

import io.skysail.domain.repo.ScalaDbRepository;
import io.skysail.repo.orientdb._
import io.skysail.app.wyt.domain.Car

class CarRepository(db: ScalaDbService) extends OrientDbRepository[Car](db) with ScalaDbRepository {
  
  db.createWithSuperClass("V", DbClassName.of(classOf[Car]))
  db.register(classOf[Car]);
}