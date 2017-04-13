package io.skysail.core.security.config

import io.skysail.core.ApiVersion
import io.skysail.api.um.AuthenticationService

class ScalaSecurityConfigBuilder(val apiVersion: ApiVersion) {

  val pathToAuthenticatorMatcherRegistry = new PathToAuthenticatorMatcherRegistry(this);
  def getPathToAuthenticatorMatcherRegistry() = pathToAuthenticatorMatcherRegistry

  var authenticationService: AuthenticationService = null
  def setAuthenticationService(a: AuthenticationService) = authenticationService = a

  def authorizeRequests() = pathToAuthenticatorMatcherRegistry

  def build(): ScalaSecurityConfig = {
    val securityConfig = new ScalaSecurityConfig(authenticationService);
    //		 if (pathToAuthenticatorMatcherRegistry != null) {
    //			 List<PathToAuthenticatorMatcher> matchers = pathToAuthenticatorMatcherRegistry.getMatchers();
    //			 for (PathToAuthenticatorMatcher matcher : matchers) {
    //				securityConfig.addPathToAuthenticatorMatcher(matcher);
    //			}
    //		 }
    securityConfig;
  }
}