package io.skysail.restlet.services

import org.restlet.service.StatusService
import org.restlet.Response
import org.restlet.Request
import org.restlet.data.Status
import org.restlet.representation.StringRepresentation
import org.restlet.representation.Representation
import org.restlet.data.MediaType

class SkysailStatusService extends StatusService {

  override def toRepresentation(status: Status, request: Request, response: Response): Representation = {
    val msg = s"""
      <u>Sorry, there was a problem processing your request:</u><br><br>
      ${status.toString()}<br><br><br>
      Proceed <a href='/' onClick='javascript:document.cookie=\"mainpage=index;path=/\";'>here</a> to get back to the application.
      """
    new StringRepresentation(msg, MediaType.TEXT_HTML);
  }
}