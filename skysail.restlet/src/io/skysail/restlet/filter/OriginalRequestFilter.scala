package io.skysail.restlet.filter

import org.restlet.routing.Filter
import org.restlet.Context
import org.restlet.Response
import org.restlet.Request

class OriginalRequestFilter(context: Context) extends Filter(context) {

  override def beforeHandle(request: Request, response: Response): Int = FilterResult.CONTINUE.ordinal()

}