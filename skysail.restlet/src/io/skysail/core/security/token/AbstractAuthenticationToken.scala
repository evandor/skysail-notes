package io.skysail.core.security.token

import io.skysail.core.security.Authentication
import java.util.Collection
import io.skysail.core.security.Authority
import java.util.ArrayList

abstract class AbstractAuthenticationToken(authorities: Iterable[_ <: Authority]) extends Authentication {
  require(authorities.filter { a => a == null }.headOption.isEmpty)
  def getAuthorities() = authorities
  override def getName() = if (this.getPrincipal() == null) "" else this.getPrincipal().getName()
}