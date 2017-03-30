package io.skysail.restlet

import io.skysail.api.responses.SkysailResponse
import io.skysail.api.responses.FormResponse

object FieldsFactory {
  def getFactory(response: SkysailResponse[_]): FieldFactory = {
    if (response.getEntity() == null) {
      return new NoFieldFactory();
    }
    if (response.getEntity().isInstanceOf[List[_]]) {
      null
      //      return new DefaultListFieldFactory();
      //    } else if (response instanceof ConstraintViolationsResponse) {
      //      return entityFactory((ConstraintViolationsResponse<?>) response);
    } else if (response.isInstanceOf[FormResponse[_]]) {
      return entityFactoryForForm(response.asInstanceOf[FormResponse[_]]);
    } else {
      null; //return new DefaultEntityFieldFactory(response.getEntity().getClass());
    }
  }

  private def entityFactoryForForm(source: FormResponse[_]):FieldFactory = {
    new FormResponseEntityFieldFactory(source.getEntity().getClass());
  }
}