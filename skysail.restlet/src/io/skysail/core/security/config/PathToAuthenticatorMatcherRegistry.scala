package io.skysail.core.security.config

class PathToAuthenticatorMatcherRegistry(securityConfigBuilder: SecurityConfigBuilder ) {

    val matchers = scala.collection.mutable.ListBuffer[PathToAuthenticatorMatcher]()
    def getMatchers() = matchers

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

    def  and(): SecurityConfigBuilder = securityConfigBuilder
}