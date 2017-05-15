package io.skysail.app.notes.it

import io.skysail.testsupport.ApplicationBrowser2
import org.restlet.data.MediaType
import org.restlet.data.Form

class NotesBrowser(mediaType: MediaType, port: Integer) extends ApplicationBrowser2("notesit",mediaType, port) { 
  
  def createForm(x$1: String): Form = {
    ???
  }
}