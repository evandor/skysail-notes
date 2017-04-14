package io.skysail.core.security

import java.security.Principal
import java.util.Collection

trait Authentication extends Principal {
  def getAuthorities(): Iterable[Authority]
  def getCredentials(): Any
  def getPrincipal(): Principal
  def isAuthenticated(): Boolean
  def setAuthenticated(isAuthenticated: Boolean): Unit
}