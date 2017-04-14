package io.skysail.core.security

final object SecurityContextHolder {
  
  val  contextHolder = new ThreadLocal[SecurityContext]() 

	def clearContext() = contextHolder.remove()
//
//	public static SecurityContext getContext() {
//		SecurityContext ctx = contextHolder.get();
//
//		if (ctx == null) {
//			ctx = createEmptyContext();
//			contextHolder.set(ctx);
//		}
//
//		return ctx;
//	}
	
	def setContext(context: SecurityContext) = contextHolder.set(context)

//	public static SecurityContext createEmptyContext() {
//		return new SecurityContext();
//	}

}