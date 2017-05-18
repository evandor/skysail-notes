package io.skysail.app.dbviewer.repository

import io.skysail.domain.repo.ScalaDbRepository;
import io.skysail.repo.orientdb._
import io.skysail.app.dbviewer.domain.Connection


class DbViewerRepository(db: ScalaDbService) extends OrientDbRepository[Connection](db) with ScalaDbRepository {
  
  db.createWithSuperClass("V", DbClassName.of(classOf[Connection]));
  db.register(classOf[Connection]);
}