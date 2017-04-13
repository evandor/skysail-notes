package io.skysail.core.security.config

class PathToAuthenticatorMatcherRegistry(securityConfigBuilder: ScalaSecurityConfigBuilder ) {

    val matchers = scala.collection.mutable.ListBuffer[PathToAuthenticatorMatcher]()

    def  startsWithMatcher( startsWith:String):PathToAuthenticatorMatcher= {
        val matcher = new StartsWithExpressionPathToAuthenticatorMatcher(securityConfigBuilder, startsWith);
        matchers += matcher
        return matcher;
    }

    def  equalsMatcher( matcherString: String):PathToAuthenticatorMatcher= {
        val matcher = new EqualsExpressionPathToAuthenticatorMatcher(securityConfigBuilder, matcherString);
        matchers += matcher
        return matcher;
    }

    def  and(): ScalaSecurityConfigBuilder = securityConfigBuilder
}