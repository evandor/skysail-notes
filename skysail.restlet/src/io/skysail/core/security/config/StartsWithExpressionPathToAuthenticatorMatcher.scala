package io.skysail.core.security.config

class StartsWithExpressionPathToAuthenticatorMatcher(
  securityConfigBuilder: SecurityConfigBuilder,
  var startsWith: String)
    extends AbstractPathToAuthenticatorMatcher(
      securityConfigBuilder) {

  val apiVersion = securityConfigBuilder.apiVersion
  startsWith = if (apiVersion == null) startsWith else apiVersion.getVersionPath() + startsWith

  override def `match`(path: String) = path.startsWith(startsWith)

}