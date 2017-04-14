package io.skysail.core.security.token

import java.security.Principal
import io.skysail.core.security.Authority

class AnonymousAuthenticationToken(principal: Principal, authorities: Iterable[_ <: Authority] ) 
extends AbstractAuthenticationToken(authorities) {
	override def getCredentials() = ""
	override def isAuthenticated() = false
	override def getPrincipal() = principal
	override def setAuthenticated(a: Boolean) {}
}