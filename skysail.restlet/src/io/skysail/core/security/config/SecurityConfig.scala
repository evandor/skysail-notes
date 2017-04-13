package io.skysail.core.security.config

import io.skysail.api.um.AuthenticationService
import org.restlet.Context
import org.restlet.security.Authenticator
import org.slf4j.LoggerFactory
import io.skysail.api.um.NeverAuthenticatedAuthenticator

class ScalaSecurityConfig(authenticationService: AuthenticationService) {

  var log = LoggerFactory.getLogger(this.getClass())

  val matchers = scala.collection.mutable.ListBuffer[PathToAuthenticatorMatcher]()

  def authenticatorFor(context: Context, path: String): Authenticator = {
    val authenticator = matchers
      .filter(matcher => matcher.`match`(path))
      .headOption
      .map(matcher => matcher.getAuthenticator(context, authenticationService))
      .getOrElse(new NeverAuthenticatedAuthenticator(context))
    log.debug(s"matched authenticators against path '$path', found '${authenticator.getClass().getSimpleName()}'")
    return authenticator;
  }

  def addPathToAuthenticatorMatcher(pathToAuthenticatorMatcher: PathToAuthenticatorMatcher) = {
    matchers += pathToAuthenticatorMatcher
  }

  //    @Override
  //    public String toString() {
  //        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(":\n");
  //        matchers.stream().forEach(m -> sb.append("  ").append(m.toString()).append("\n"));
  //        return sb.toString();
  //    }
}