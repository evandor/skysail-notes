package io.skysail.core.model

import io.skysail.restlet.ResourceContextId
import io.skysail.restlet.resources._

class TestEntitiesResource extends ListServerResource[TestEntity](classOf[TestEntityResource]) { 
  def getEntity(): Any = "hi"
  override def linkedResourceClasses() = List(classOf[PostTestEntityResource])
}

class TestEntityResource extends EntityServerResource[TestEntity] { 
  override def linkedResourceClasses() = List(classOf[PutTestEntityResource])
}

class PostTestEntityResource extends PostEntityServerResource[TestEntity] {
  addToContext(ResourceContextId.LINK_TITLE, "create TestEntity");
  def createEntityTemplate() = TestEntity(Some("1"), "hi")
  override def getEntity() = TestEntity(None, "").asInstanceOf[Nothing]
  def addEntity(entity: TestEntity): Unit = {  }
  override def redirectTo() = super.redirectTo(classOf[TestEntitiesResource])
}

class PutTestEntityResource extends PutEntityServerResource[TestEntity] {
  
}