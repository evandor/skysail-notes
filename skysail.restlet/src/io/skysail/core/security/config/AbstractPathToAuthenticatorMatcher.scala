package io.skysail.core.security.config

import io.skysail.api.um.AuthenticationMode
import io.skysail.api.um.AuthenticationService
import org.restlet.Context

abstract class AbstractPathToAuthenticatorMatcher(securityConfigBuilder: ScalaSecurityConfigBuilder) extends PathToAuthenticatorMatcher {

  var authMode: AuthenticationMode = null

  override def permitAll(): PathToAuthenticatorMatcherRegistry = {
    authMode = AuthenticationMode.PERMIT_ALL;
    return securityConfigBuilder.getPathToAuthenticatorMatcherRegistry();
  }

  override def anonymous() = {
    authMode = AuthenticationMode.ANONYMOUS;
    securityConfigBuilder.getPathToAuthenticatorMatcherRegistry();
  }

  override def authenticated() = {
    authMode = AuthenticationMode.AUTHENTICATED;
    securityConfigBuilder.getPathToAuthenticatorMatcherRegistry();
  }

  override def denyAll() = {
    authMode = AuthenticationMode.DENY_ALL;
    securityConfigBuilder.getPathToAuthenticatorMatcherRegistry();
  }

  override def getAuthenticator(context: Context, authenticationService: AuthenticationService) = {
     authenticationService.getResourceAuthenticator(context, authMode);
  }

}