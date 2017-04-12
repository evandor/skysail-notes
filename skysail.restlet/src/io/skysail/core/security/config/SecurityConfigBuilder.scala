package io.skysail.core.security.config

import io.skysail.core.ApiVersion
import io.skysail.api.um.AuthenticationService

class ScalaSecurityConfigBuilder(apiVersion: ApiVersion) {
//  @Getter
//	private PathToAuthenticatorMatcherRegistry pathToAuthenticatorMatcherRegistry = new PathToAuthenticatorMatcherRegistry(this);
//
	var authenticationService: AuthenticationService = null
	def setAuthenticationService(a: AuthenticationService) = authenticationService = a
//
//	@Getter
//	private ApiVersion apiVersion;
//
//	public SecurityConfigBuilder(ApiVersion apiVersion) {
//		this.apiVersion = apiVersion;
//	}
//
//	public PathToAuthenticatorMatcherRegistry authorizeRequests() {
//		return pathToAuthenticatorMatcherRegistry;
//	}
//
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