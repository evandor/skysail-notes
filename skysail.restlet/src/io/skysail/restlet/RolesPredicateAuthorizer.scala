package io.skysail.restlet

import com.google.common.base.Predicate
import org.restlet.security.Authorizer
import org.restlet.Response
import org.restlet.Request

import scala.collection.JavaConverters._

class RolesPredicateAuthorizer(rolesPredicate: Predicate[Array[String]]) extends Authorizer {

  override def authorize(request: Request, response: Response): Boolean = {
    val clientRoles = request.getClientInfo().getRoles().asScala;
    if (clientRoles == null && rolesPredicate == null) {
      return true;
    }
    if (rolesPredicate == null) {
      return true;
    }
    if (clientRoles == null) {
      return false;
    }
    val clientRoleNames = clientRoles.map(cR => cR.getName()).toList
    rolesPredicate.apply(clientRoleNames.toArray);
  }

}