package io.skysail.core.security.token

import java.security.Principal
import io.skysail.core.security.Authority

class UsernamePasswordAuthenticationToken(principal: Principal, credentials: Any ) 
extends AbstractAuthenticationToken(Seq[Authority]()) {
	override def getCredentials() = credentials
	override def isAuthenticated() = false
	override def getPrincipal() = principal
	override def setAuthenticated(a: Boolean) {}
}