package io.skysail.api.um

import java.security.Principal;

import org.osgi.annotation.versioning.ProviderType;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.security.Authenticator;

/**
 * Defines the contract for authentication handling in skysail applications.
 */
@ProviderType
trait AuthenticationService {

  /**
   * @return a restlet authenticator the application is authenticated against.
   */
  def getApplicationAuthenticator(context: Context, authMode: AuthenticationMode): Authenticator

  /**
   * @param authMode
   * @return a restlet authenticator the applications' resources are
   *         authenticated against.
   */
  def getResourceAuthenticator(context: Context, authMode: AuthenticationMode): Authenticator

  /**
   * @return whether or not the current request is authenticated.
   */
  def isAuthenticated(request: Request): Boolean

  /**
   * @return the associated principal, i.e. the currently authenticated user.
   */
  def getPrincipal(request: Request): Principal

  /**
   * skysail can be set up to run with different authentication services, which
   * typically provide a login path to which the generic "/_login" path will
   * be redirected.
   */
  def getLoginPath(): String

  /**
   * skysail can be set up to run with different authentication services, which
   * typically provide a logout path to which the generic "/_logout" path will
   * be redirected.
   */
  def getLogoutPath(): String
}
