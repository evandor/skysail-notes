package io.skysail.core.security

import io.skysail.core.security.token.AnonymousAuthenticationToken
import org.restlet.security.User

object SecurityContext {
  val ANONYMOUS = "anonymous";
}

class SecurityContext(authentication: Authentication) {
  def this() = this(new AnonymousAuthenticationToken(new User(SecurityContext.ANONYMOUS), Seq[Authority]()))
}