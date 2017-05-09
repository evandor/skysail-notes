package io.skysail.testsupport.authentication;

import org.restlet.resource.ClientResource;

import io.skysail.testsupport.ApplicationClient2;

public interface AuthenticationStrategy2 {

	ClientResource login(ApplicationClient2 client, String username, String password);

}
