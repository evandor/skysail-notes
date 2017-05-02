package io.skysail.ui.polymer

import io.skysail.restlet.app.SkysailApplication
import org.osgi.service.component.annotations.Component
import io.skysail.restlet.app.ApplicationProvider

@Component(
  immediate = true,
  service = Array(classOf[ApplicationProvider]))

class PolymerStaticWebapp("") extends SkysailApplication {
  
}