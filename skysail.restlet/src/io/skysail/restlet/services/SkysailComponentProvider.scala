package io.skysail.restlet.services

import io.skysail.core.SkysailComponent;

trait SkysailComponentProvider {

    def getSkysailComponent(): SkysailComponent
}
