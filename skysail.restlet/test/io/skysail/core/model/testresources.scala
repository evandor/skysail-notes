package io.skysail.core.model

import io.skysail.restlet.resources.PostEntityServerResource2
import io.skysail.restlet.ResourceContextId
import io.skysail.restlet.resources.ListServerResource2
import io.skysail.restlet.resources.EntityServerResource2

class TestEntitiesResource extends ListServerResource2[TestEntity](classOf[TestEntityResource]) { 
  def getEntity(): Any = "hi"
  override def linkedResourceClasses() = List(classOf[PostTestEntityResource])
}

class TestEntityResource extends EntityServerResource2[TestEntity] { 
  
 
}

class PostTestEntityResource extends PostEntityServerResource2[TestEntity] {
  addToContext(ResourceContextId.LINK_TITLE, "create TestEntity");
  def createEntityTemplate() = TestEntity(Some("1"), "hi")
  override def getEntity() = TestEntity(None, "").asInstanceOf[Nothing]
  def addEntity(entity: TestEntity): Unit = {
  }

  override def redirectTo() = super.redirectTo(classOf[TestEntitiesResource])
}