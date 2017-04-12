package io.skysail.core.security.config

import io.skysail.api.um.AuthenticationService

class ScalaSecurityConfig(authenticationService: AuthenticationService ) {
  
   val matchers = List[PathToAuthenticatorMatcher]() 

//    private AuthenticationService authenticationService;
//
//    public SecurityConfig() {
//        this.authenticationService = authenticationService;
//    }
//
//    public Authenticator authenticatorFor(Context context, String path) {
//        Authenticator authenticator = matchers.stream()
//                .filter(matcher -> matcher.match(path))
//                .findFirst()
//                .map(matcher -> matcher.getAuthenticator(context, authenticationService))
//                .orElse(new NeverAuthenticatedAuthenticator(context));
//        log.debug("matched authenticators against path '{}', found '{}'", path,
//                authenticator.getClass().getSimpleName());
//        return authenticator;
//    }
//
//    public void addPathToAuthenticatorMatcher(PathToAuthenticatorMatcher pathToAuthenticatorMatcher) {
//        matchers.add(pathToAuthenticatorMatcher);
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(":\n");
//        matchers.stream().forEach(m -> sb.append("  ").append(m.toString()).append("\n"));
//        return sb.toString();
//    }
}