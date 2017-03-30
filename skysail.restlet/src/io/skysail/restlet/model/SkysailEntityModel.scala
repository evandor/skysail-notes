package io.skysail.restlet.model

import io.skysail.core.model.SkysailEntityModel
import io.skysail.domain.core.EntityModel

class ScalaSkysailEntityModel(identifiableClass: Class[_]) extends EntityModel(identifiableClass.getName()) {
  def identifiableClass():Class[_] = identifiableClass
}