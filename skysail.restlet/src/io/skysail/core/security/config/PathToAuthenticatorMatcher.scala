package io.skysail.core.security.config

import org.restlet.Context
import io.skysail.api.um.AuthenticationService
import org.restlet.security.Authenticator

trait PathToAuthenticatorMatcher {
  def permitAll(): PathToAuthenticatorMatcherRegistry
  def denyAll(): PathToAuthenticatorMatcherRegistry
  def authenticated(): PathToAuthenticatorMatcherRegistry
  def anonymous(): PathToAuthenticatorMatcherRegistry

  def `match`(path: String): Boolean

  def getAuthenticator(context: Context, authenticationService: AuthenticationService): Authenticator
}